package io.github.hooj0.springdata.fabric.chaincode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mapping.context.MappingContext;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeCustomConversions;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.SimpleChaincodeMappingContext;
import io.github.hooj0.springdata.fabric.chaincode.core.support.ChaincodeTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * simple chaincode configuration injection bean object
 * @author hoojo
 * @createDate 2018年7月18日 下午6:27:13
 * @file SimpleChaincodeConfiguration.java
 * @package io.github.hooj0.springdata.fabric.chaincode.config
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
@Deprecated
@SuppressWarnings("all")
public final class SimpleChaincodeConfiguration extends AbstractChaincodeConfiguration {

	@Bean
	public MappingContext mappingContext() throws ClassNotFoundException {
		log.debug("create chaincode configuration \"MappingContext\" instance");
		
		SimpleChaincodeMappingContext mappingContext = new SimpleChaincodeMappingContext();
		mappingContext.setInitialEntitySet(getInitialEntitySet());

		return mappingContext;
	}
	
	@Bean
	public ChaincodeConverter mappingConverter() throws ClassNotFoundException {
		log.debug("create chaincode configuration \"ChaincodeConverter\" instance");
		
		MappingChaincodeConverter converter = new MappingChaincodeConverter(mappingContext());
		return converter;
	}
	
	@Bean
	public CustomConversions customConversions() throws ClassNotFoundException {
		log.debug("create chaincode configuration \"CustomConversions\" instance");
		
		return new ChaincodeCustomConversions();
	}
	
	@Bean
	public ChaincodeOperations chaincodeOperations() throws ClassNotFoundException {
		log.debug("create chaincode configuration \"ChaincodeOperations\" instance");
		
		return new ChaincodeTemplate(this.mappingConverter());
	}
}
