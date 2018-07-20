package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;

/**
 * 实现 ChaincodeEntityInformationCreator 接口，完成对象实体元数据填充
 * @author hoojo
 * @createDate 2018年7月17日 下午6:55:56
 * @file ChaincodeEntityInformationCreatorImpl.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeEntityInformationCreatorImpl implements ChaincodeEntityInformationCreator {

	private final MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
	
	public ChaincodeEntityInformationCreatorImpl(MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext) {
		Assert.notNull(mappingContext, "MappingContext must not be null!");

		this.mappingContext = mappingContext;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T, ID> ChaincodeEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		ChaincodePersistentEntity<T> persistentEntity = (ChaincodePersistentEntity<T>) mappingContext.getRequiredPersistentEntity(domainClass);

		Assert.notNull(persistentEntity, String.format("Unable to obtain mapping metadata for %s!", domainClass));
		Assert.notNull(persistentEntity.getIdProperty(), String.format("No id property found for %s!", domainClass));

		MappingChaincodeEntityInformation<T, ID> entityInformation = new MappingChaincodeEntityInformation<>(persistentEntity, new MappingChaincodeConverter(mappingContext));
		return entityInformation;
	}
}
