package io.github.hooj0.springdata.fabric.chaincode.core.convert;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class MappingChaincodeConverter extends AbstractChaincodeConverter implements ChaincodeConverter, ApplicationContextAware {

	private final MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
	private ApplicationContext applicationContext;
	
	public MappingChaincodeConverter(MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext) {
		super(new DefaultConversionService());
		Assert.notNull(mappingContext, "MappingContext must not be null!");
		
		this.mappingContext = mappingContext;
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
	
	@SuppressWarnings("unchecked")
	public <S> S read(TypeInformation<S> targetTypeInformation, Object source) {
		
		if (source == null) {
			return null;
		}
		Assert.notNull(targetTypeInformation, "TargetTypeInformation must not be null!");
		Class<S> rawType = targetTypeInformation.getType();

		// in case there's a custom conversion for the Object
		if (conversions.hasCustomReadTarget(source.getClass(), rawType)) {
			return getConversionService().convert(source, rawType);
		}

		ChaincodePersistentEntity<S> entity = (ChaincodePersistentEntity<S>) mappingContext.getRequiredPersistentEntity(rawType);
		entity.getClass();
		return null;
	}
	
	@Override
	public <R extends BaseEntity> R read(Class<R> clazz, String json) {
		log.debug("clazz: {}", clazz);
		log.debug("json: {}", json);
		return null;
	}

	@Override
	public void write(BaseEntity entity, String json) {
		log.debug("entity: {}", entity);
		log.debug("json: {}", json);
		
		if (null == entity) {
			return;
		}

		TypeInformation<? extends Object> type = ClassTypeInformation.from(entity.getClass());
		conversions.hasCustomWriteTarget(type.getClass(), BaseEntity.class);
	}
}
