package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;

/**
 * Implementation of {@link ChaincodeEntityMetadata} based on the type and {@link ChaincodePersistentEntity}.
 * @author hoojo
 * @createDate 2018年7月18日 上午10:01:36
 * @file SimpleChaincodeEntityMetadata.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SimpleChaincodeEntityMetadata<T> implements ChaincodeEntityMetadata<T> {

	private final ChaincodePersistentEntity<?> entity;
	private final Class<T> type;
	
	SimpleChaincodeEntityMetadata(Class<T> type, ChaincodePersistentEntity<?> entity) {
		Assert.notNull(type, "Type must not be null");
		Assert.notNull(entity, "Collection entity must not be null or empty");

		this.type = type;
		this.entity = entity;
	}
	
	@Override
	public Class<T> getJavaType() {
		return type;
	}

	@Override
	public String getEntityName() {
		return this.entity.getName();
	}
}
