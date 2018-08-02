package io.github.hooj0.springdata.fabric.chaincode.core.convert;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mapping.context.MappingContext;

import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.core.serialize.ChaincodeEntitySerialization;

/**
 * Chaincode Converter 转换器接口，转换属性或实体对象，
 * 可以完成复杂类型到底层类型的转换，已达到自动映射
 * @author hoojo
 * @createDate 2018年7月17日 上午11:02:41
 * @file ChaincodeConverter.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.convert
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeConverter {

	public ChaincodeEntitySerialization getChaincodeEntitySerialization();
	
	/**
	 * 对象类型转换服务 
	 * @author hoojo
	 * @createDate 2018年7月5日 上午11:25:16
	 */
	ConversionService getConversionService();
	
	/**
	 * 实体、属性映射上下文
	 * @author hoojo
	 * @createDate 2018年7月5日 上午11:25:55
	 */
	MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> getMappingContext();

	/**
	 * 获取自定义转换服务
	 * @author hoojo
	 * @createDate 2018年7月17日 下午6:08:15
	 */
	CustomConversions getCustomConversions();
}
