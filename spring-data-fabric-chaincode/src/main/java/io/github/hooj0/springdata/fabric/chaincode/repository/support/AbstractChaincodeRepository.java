package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.springframework.util.Assert;

import com.google.common.io.Files;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
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
public class AbstractChaincodeRepository<T> implements ChaincodeRepository<T>, DeployChaincodeRepository<T> {

	protected ChaincodeEntityInformation<T, ?> entityInformation;
	protected ChaincodeOperations operations;
	protected Criteria globalCriteria;
	protected Class<T> entityClass;

	public AbstractChaincodeRepository(ChaincodeOperations operations) {
		this.operations = operations;
		Assert.notNull(operations, "ChaincodeOperations must not be null!");
		
		System.err.println("调用----------->>>AbstractChaincodeRepository");
	}
	
	public AbstractChaincodeRepository(Criteria globalCriteria, ChaincodeEntityInformation<T, ?> entityInformation, ChaincodeOperations operations) {
		this(operations);
		Assert.notNull(globalCriteria, "globalCriteria must not be null!");

		this.entityInformation = entityInformation;
		this.globalCriteria = globalCriteria;
		
		System.err.println("调用----------->>>AbstractChaincodeRepository");
		log.debug("globalCriteria: {}", globalCriteria);
		
		// setChannelName
		// setOrgName
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
	
	public String invoke(String func, String... args) {
		log.debug("execution chaincode repository invoke -> func: {}, args: {}", func, args);
		
		System.err.println(this.globalCriteria);
		
		return "success";
	}

	public String query(String func, String... args) {
		log.debug("execution chaincode repository query -> func: {}, args: {}", func, args);
		
		System.err.println(this.globalCriteria);
		return "success";
	}
	
	@Override
	public void install(File chaincodeSourceFile) {
		log.debug("execution chaincode repository invoke -> chaincodeSourceFile: {}", chaincodeSourceFile);
		
		System.err.println(this.globalCriteria);
	}

	@Override
	public void install(InputStream chaincodeInputStream) {
		log.debug("execution chaincode repository install -> chaincodeInputStream: {}", chaincodeInputStream);
		
		System.err.println(this.globalCriteria);
	}

	@Override
	public String instantiate(byte[] policyAsBytes, String func, String... args) {
		log.debug("execution chaincode repository instantiate -> policyAsBytes: {}, func: {}, args: {}", policyAsBytes.length, func, args);
	
		System.err.println(this.globalCriteria);
		return "success";
	}

	@Override
	public String instantiate(File policyFile, String func, String... args) {
		log.debug("execution chaincode repository instantiate -> policyFile: {}, func: {}, args: {}", policyFile.length(), func, args);
		
		if (policyFile != null) {
			String suffix = Files.getFileExtension(policyFile.getName());
			if ("yaml".equalsIgnoreCase(suffix) || "yml".equalsIgnoreCase(suffix)) { // YAML 
				System.out.println("yaml");
			} else { // FILE
				System.out.println("file");
			}
		}
		
		System.err.println(this.globalCriteria);
		return "success";
	}

	@Override
	public String instantiate(InputStream policyInputStream, String func, String... args) {
		log.debug("execution chaincode repository instantiate -> policyInputStream: {}, func: {}, args: {}", policyInputStream, func, args);
		
		System.err.println(this.globalCriteria);
		return "success";		
	}

	@Override
	public String upgrade(byte[] policyAsBytes, String version, String func, String... args) {
		log.debug("execution chaincode repository upgrade -> policyAsBytes: {}, func: {}, args: {}", policyAsBytes.length, func, args);
		
		System.err.println(this.globalCriteria);
		return "success";				
	}

	@Override
	public String upgrade(File policyFile, String version, String func, String... args) {
		log.debug("execution chaincode repository upgrade -> policyFile: {}, func: {}, args: {}", policyFile.length(), func, args);
		
		if (policyFile != null) {
			String suffix = Files.getFileExtension(policyFile.getName());
			if ("yaml".equalsIgnoreCase(suffix) || "yml".equalsIgnoreCase(suffix)) { // YAML 
				System.out.println("yaml");
			} else { // FILE
				System.out.println("file");
			}
		}
		
		System.err.println(this.globalCriteria);
		return "success";
	}

	@Override
	public String upgrade(InputStream policyInputStream, String version, String func, String... args) {
		log.debug("execution chaincode repository upgrade -> policyInputStream: {}, func: {}, args: {}", policyInputStream, func, args);
		
		System.err.println(this.globalCriteria);
		return "success";
	}

	@Override
	public ResultSet invoke(String func) {
		
		operations.invoke(globalCriteria, func);
		return null;
	}

	@Override
	public ResultSet invoke(String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet invoke(String func, LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(String func, LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent invokeFor(String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent invokeFor(String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransactionEvent invokeFor(String func, LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String query(String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String query(String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String query(String func, LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet queryFor(String func) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet queryFor(String func, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet queryFor(String func, LinkedHashMap<String, Object> args) {
		// TODO Auto-generated method stub
		return null;
	}
}
