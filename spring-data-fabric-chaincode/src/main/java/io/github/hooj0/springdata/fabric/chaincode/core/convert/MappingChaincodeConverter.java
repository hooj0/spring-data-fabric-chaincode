package io.github.hooj0.springdata.fabric.chaincode.core.convert;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.SimpleChaincodeMappingContext;
import io.github.hooj0.springdata.fabric.chaincode.core.serialize.ChaincodeEntitySerialization;
import io.github.hooj0.springdata.fabric.chaincode.core.serialize.GsonChaincodeEntitySerialization;

/**
 * Chaincode 智能合约 实体、属性转换映射上下文
 * @author hoojo
 * @createDate 2018年7月17日 下午4:21:36
 * @file MappingChaincodeConverter.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.convert
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class MappingChaincodeConverter extends AbstractChaincodeConverter implements ApplicationContextAware {

	private final MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
	private final ChaincodeEntitySerialization serialization;
	private ApplicationContext applicationContext;
	
	public MappingChaincodeConverter() {
		this(newDefaultMappingContext(), newDefaultEntitySerialization());
	}
	
	public MappingChaincodeConverter(MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext) {
		this(newDefaultMappingContext(), newDefaultEntitySerialization());
	}

	public MappingChaincodeConverter(MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext, ChaincodeEntitySerialization serialization) {
		super(new DefaultConversionService());
		Assert.notNull(mappingContext, "MappingContext must not be null!");
		
		this.mappingContext = mappingContext;
		this.serialization = serialization;
	}
	
	private static SimpleChaincodeMappingContext newDefaultMappingContext() {
		SimpleChaincodeMappingContext mappingContext = new SimpleChaincodeMappingContext();
		
		return mappingContext;
	}

	private static ChaincodeEntitySerialization newDefaultEntitySerialization() {

		return GsonChaincodeEntitySerialization.INSTANCE;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;
		
		if (mappingContext instanceof ApplicationContextAware) {
			((ApplicationContextAware) mappingContext).setApplicationContext(applicationContext);
		}
	}

	@Override
	public MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> getMappingContext() {
		return this.mappingContext;
	}
	
	@Override
	public ChaincodeEntitySerialization getChaincodeEntitySerialization() {
		return this.serialization;
	}
}
