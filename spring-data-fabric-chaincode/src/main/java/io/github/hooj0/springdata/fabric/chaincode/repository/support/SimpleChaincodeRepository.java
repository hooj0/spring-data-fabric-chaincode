package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;

/**
 * Chaincode Repository Simple Implements
 * @author hoojo
 * @createDate 2018年7月18日 上午9:31:41
 * @file SimpleChaincodeRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class SimpleChaincodeRepository<T> extends AbstractChaincodeRepository<T> {

	private Class<?> entityType;
	
	public SimpleChaincodeRepository(ChaincodeEntityInformation<T, ?> metadata, ChaincodeOperations operations) {
		super(metadata, operations);
		
		this.entityType = this.entityInformation.getJavaType();
		// metadata.getChaincodeName();
	}

	public SimpleChaincodeRepository(ChaincodeOperations operations) {
		super(operations);
	}
}
