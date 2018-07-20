package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.DeployChaincodeRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ChaincodeRepository Base abstract repository
 * @author hoojo
 * @createDate 2018年7月18日 上午9:18:34
 * @file AbstractChaincodeRepositoryQuery.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@NoArgsConstructor
@Slf4j
public class AbstractChaincodeRepository<T> implements ChaincodeRepository<T>/*, DeployChaincodeRepository<T>*/ {

	protected ChaincodeEntityInformation<T, ?> entityInformation;
	protected ChaincodeOperations operations;
	
	protected Class<T> entityClass;
	
	public AbstractChaincodeRepository(ChaincodeOperations operations) {
		this.operations = operations;
	}
	
	public AbstractChaincodeRepository(ChaincodeEntityInformation<T, ?> entityInformation, ChaincodeOperations operations) {
		log.debug("RepositoryAannotaitonInformation: {}", entityInformation.getRepositoryAannotaitonInformation());

		this.entityInformation = entityInformation;
		this.operations = operations;
	}
	
	@Override
	public String invoke(String func, String... args) {
		log.debug("execution chaincode repository invoke -> func: {}, args: {}", func, args);
		
		return "success";
	}

	@Override
	public String query(String func, String... args) {
		log.debug("execution chaincode repository invoke -> func: {}, args: {}", func, args);
		
		return "success";
	}
	
	@Override
	public Class<T> getEntityClass() {
		if (!isEntityClassSet()) {
			try {
				this.entityClass = resolveReturnedClassFromGenericType();
			} catch (Exception e) {
				throw new RuntimeException("Unable to resolve EntityClass. Please use according setter!", e);
			}
		}
		return entityClass;
	}
	
	private boolean isEntityClassSet() {
		return entityClass != null;
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> resolveReturnedClassFromGenericType() {
		ParameterizedType parameterizedType = resolveReturnedClassFromGenericType(getClass());
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	
	private ParameterizedType resolveReturnedClassFromGenericType(Class<?> clazz) {
		Object genericSuperclass = clazz.getGenericSuperclass();
		
		if (genericSuperclass instanceof ParameterizedType) {
			
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
			Type rawtype = parameterizedType.getRawType();
			if (SimpleChaincodeRepository.class.equals(rawtype)) {
				return parameterizedType;
			}
		}
		return resolveReturnedClassFromGenericType(clazz.getSuperclass());
	}
}
