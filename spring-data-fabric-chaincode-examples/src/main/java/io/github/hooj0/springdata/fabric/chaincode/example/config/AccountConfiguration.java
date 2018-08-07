package io.github.hooj0.springdata.fabric.chaincode.example.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.fabric.sdk.commons.store.support.FileSystemKeyValueStore;
import io.github.hooj0.springdata.fabric.chaincode.config.AbstractChaincodeConfiguration;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.support.ChaincodeTemplate;
import io.github.hooj0.springdata.fabric.chaincode.repository.config.EnableChaincodeRepositories;

/**
 * spring data fabric chaincode account configuration
 * @author hoojo
 * @createDate 2018年8月7日 上午11:29:48
 * @file AccountConfiguration.java
 * @package io.github.hooj0.springdata.fabric.chaincode.example.config
 * @project spring-data-fabric-chaincode-examples
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackages = "io.github.hooj0.springdata.fabric.chaincode.example")
@EnableChaincodeRepositories(basePackages = "io.github.hooj0.springdata.fabric.chaincode.example.repository", considerNestedRepositories = true)
public class AccountConfiguration extends AbstractChaincodeConfiguration {

	private final static Logger log = LoggerFactory.getLogger(AccountConfiguration.class);
	
	@Autowired
	private MappingChaincodeConverter mappingConverter;
	
	@Bean
	public ChaincodeTemplate chaincodeTemplate() throws ClassNotFoundException {
		log.debug("create chaincode configuration \"ChaincodeTemplate\" instance");
		
		FabricKeyValueStore store =  new FileSystemKeyValueStore(new File("src/main/resources/fabric-kv-store.properties"));
		return new ChaincodeTemplate(mappingConverter, DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration(), store);
	}
}
