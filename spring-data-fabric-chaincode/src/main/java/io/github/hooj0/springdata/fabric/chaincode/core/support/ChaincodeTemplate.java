package io.github.hooj0.springdata.fabric.chaincode.core.support;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.MappingContext;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.fabric.sdk.commons.core.support.ChaincodeTransactionTemplate;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import lombok.extern.slf4j.Slf4j;

/**
 * chaincode operations `install & invoke & instantiate & query & upgrade` template
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
	private final MappingContext mappingContext;
	private final ChaincodeConverter converter;
	
	private final FabricConfiguration config;
	private final FabricKeyValueStore store;
	
	private Map<String, ChaincodeDeployOperations> deployOperationsCache = Maps.newConcurrentMap();
	private Map<String, ChaincodeTransactionOperations> transactionOperationsCache = Maps.newConcurrentMap();
	
	public ChaincodeTemplate() {
		this(newDefaultConverter());
	}
	
	public ChaincodeTemplate(ChaincodeConverter converter) {
		this(converter, null, null);
	}
	
	public ChaincodeTemplate(ChaincodeConverter converter, FabricConfiguration config, FabricKeyValueStore store) {
		this.converter = converter;
		this.mappingContext = converter.getMappingContext();
		
		this.config = Optional.fromNullable(config).or(DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration());
		this.store = Optional.fromNullable(store).or(config.getDefaultKeyValueStore());
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

	@Override
	public ChaincodeDeployOperations getChaincodeDeployOperations(Criteria criteria) {
		return deployOperationsCache.get(criteria.getChannel() + "." + criteria.getOrg());
	}

	@Override
	public ChaincodeTransactionOperations getChaincodeTransactionOperations(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet invoke(Criteria criteria, String func) {
		
		ChaincodeTransactionOperations transactionOperations = new ChaincodeTransactionTemplate(criteria.getChannel(), criteria.getOrg(), config, store);
		
		InvokeOptions options = new InvokeOptions();
		options.setChaincodeId(criteria.getChaincodeID());
		
		return transactionOperations.invoke(options, func);
	}

	@Override
	public ResultSet invoke(Criteria criteria, String func, Object... args) {
		return null;
	}

	@Override
	public ResultSet invoke(Criteria criteria, String func, LinkedHashMap<String, Object> args) {
		return null;
	}
}
