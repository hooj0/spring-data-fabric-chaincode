package io.github.hooj0.springdata.fabric.chaincode.config;

import org.springframework.context.annotation.Bean;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeTemplate;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.SimpleChaincodeMappingContext;

/**
 * 配置需要注入的 Bean 对象
 * @author hoojo
 * @createDate 2018年7月18日 下午6:27:13
 * @file SimpleChaincodeConfiguration.java
 * @package io.github.hooj0.springdata.fabric.chaincode.config
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SimpleChaincodeConfiguration extends AbstractChaincodeConfiguration {

	@Bean
	public SimpleChaincodeMappingContext mappingContext() throws ClassNotFoundException {
		SimpleChaincodeMappingContext mappingContext = new SimpleChaincodeMappingContext();
		mappingContext.setInitialEntitySet(getInitialEntitySet());

		return mappingContext;
	}
	
	@Bean
	public MappingChaincodeConverter mappingConverter() throws ClassNotFoundException {
		
		MappingChaincodeConverter converter = new MappingChaincodeConverter(mappingContext());
		return converter;
	}
	
	@Bean
	public ChaincodeTemplate chaincodeTemplate() {
		return new ChaincodeTemplate();
	}
	
	@Bean
	public ChaincodeOperations chaincodeOperations() throws ClassNotFoundException {
		
		return null;
		//return this.chaincodeTemplate();
	}
}
