package io.github.hooj0.springdata.fabric.chaincode.core;

import org.hyperledger.fabric.sdk.HFClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.MappingContext;

import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>function:</b> Chaincode Operations Template
 * @author hoojo
 * @createDate 2018年7月17日 上午10:32:32
 * @file ChaincodeTemplate.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class ChaincodeTemplate implements ChaincodeOperations, ApplicationContextAware {

	private ApplicationContext applicationContext;
	private MappingContext mappingContext;
	private ChaincodeConverter converter;
	private HFClient client;
	
	public ChaincodeTemplate() {
		this(newDefaultConverter());
	}
	
	public ChaincodeTemplate(ChaincodeConverter converter) {
		this.converter = converter;
		this.mappingContext = converter.getMappingContext();
		
		this.client = HFClient.createNewInstance();
	}

	@Override
	public void install() {
		
	}
	
	@Override
	public HFClient getClient() {
		return this.client;
	}

	@Override
	public ChaincodeConverter getConverter() {
		return this.converter;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;
		
		if (converter instanceof ApplicationContextAware) {
			((ApplicationContextAware) converter).setApplicationContext(context);
		}
	}
	
	private static MappingChaincodeConverter newDefaultConverter() {
		MappingChaincodeConverter converter = new MappingChaincodeConverter();
		converter.afterPropertiesSet();
		
		return converter;
	}
}
