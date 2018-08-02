package io.github.hooj0.springdata.fabric.chaincode.core.convert;

import java.util.Collections;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.EntityInstantiators;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * based abstract chaincode converter support
 * @author hoojo
 * @createDate 2018年7月17日 下午5:24:27
 * @file AbstractChaincodeConverter.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.convert
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractChaincodeConverter implements ChaincodeConverter, InitializingBean {

	protected CustomConversions conversions = new ChaincodeCustomConversions(Collections.emptyList());
	protected EntityInstantiators instantiators = new EntityInstantiators();
	protected final ConversionService conversionService;

	public AbstractChaincodeConverter(ConversionService conversionService) {
		this.conversionService = conversionService == null ? new DefaultConversionService() : conversionService;
	}
	
	/**
	 * Registers {@link EntityInstantiators} to customize entity instantiation.
	 * @param instantiators
	 */
	public void setInstantiators(EntityInstantiators instantiators) {
		this.instantiators = instantiators == null ? new EntityInstantiators() : instantiators;
	}

	@NonNull
	@Override
	public ConversionService getConversionService() {
		return this.conversionService;
	}

	/**
	 * Registers the given custom conversions with the converter.
	 */
	public void setCustomConversions(CustomConversions conversions) {
		Assert.notNull(conversions, "Conversions must not be null!");
		
		this.conversions = conversions;
	}

	@Override
	public CustomConversions getCustomConversions() {
		return this.conversions;
	}

	@Override
	public void afterPropertiesSet() {
		initializeConverters();
	}

	/**
	 * Registers additional converters that will be available when using the {@link ConversionService} directly (e.g. for
	 * id conversion). These converters are not custom conversions as they'd introduce unwanted conversions.
	 */
	private void initializeConverters() {
		ConversionService conversionService = getConversionService();

		/*
		conversionService.addConverter(ObjectIdToStringConverter.INSTANCE);
		conversionService.addConverter(StringToObjectIdConverter.INSTANCE);

		if (!conversionService.canConvert(ObjectId.class, BigInteger.class)) {
			conversionService.addConverter(ObjectIdToBigIntegerConverter.INSTANCE);
		}

		if (!conversionService.canConvert(BigInteger.class, ObjectId.class)) {
			conversionService.addConverter(BigIntegerToObjectIdConverter.INSTANCE);
		}
		*/
		
		if (conversionService instanceof GenericConversionService) {
			getCustomConversions().registerConvertersIn((GenericConversionService) conversionService);
		}
	}
}
