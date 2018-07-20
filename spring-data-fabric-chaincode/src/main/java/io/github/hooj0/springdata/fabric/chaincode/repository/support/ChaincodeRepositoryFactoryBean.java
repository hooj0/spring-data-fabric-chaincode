package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;

/**
 * {@link FactoryBean} to create {@link ChaincodeRepository} instances. 
 * @author hoojo
 * @createDate 2018年7月17日 下午7:11:51
 * @file ChaincodeRepositoryFactoryBean.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeRepositoryFactoryBean<T extends Repository<S, ID>, S, ID> extends RepositoryFactoryBeanSupport<T, S, ID> {

	private final Class<? extends T> repositoryInterface;
	private @Nullable ChaincodeOperations operations;
	
	protected ChaincodeRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
		
		this.repositoryInterface = repositoryInterface;
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory() {

		return new ChaincodeRepositoryFactory(repositoryInterface, operations);
	}
	
	public void setChaincodeOperations(ChaincodeOperations operations) {
		Assert.notNull(operations, "ChaincodeOperations must not be null!");
		
		this.operations = operations;
		setMappingContext(operations.getConverter().getMappingContext());
	}
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		Assert.notNull(operations, "ChaincodeOperations must not be null!");
	}
}
