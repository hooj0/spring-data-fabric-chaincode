package io.github.hooj0.springdata.fabric.chaincode.core.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import io.github.hooj0.fabric.sdk.commons.config.DefaultFabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.support.ChaincodeDeployTemplate;
import io.github.hooj0.fabric.sdk.commons.core.support.ChaincodeTransactionTemplate;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.cache.ChaincodeOperationBeanCache;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.QueryCriteria;

/**
 * abstract base chaincode template implements
 * @author hoojo
 * @createDate 2018年7月29日 下午4:38:56
 * @file AbstractChaincodeTemplate.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public abstract class AbstractChaincodeTemplate implements ChaincodeOperations, ApplicationContextAware {

	protected final MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
	protected ApplicationContext applicationContext;
	protected final ChaincodeConverter converter;
	
	protected final FabricConfiguration config;
	protected final FabricKeyValueStore store;
	protected final ChaincodeOperationBeanCache beanCache;
	
	public AbstractChaincodeTemplate() {
		this(newDefaultConverter());
	}
	
	public AbstractChaincodeTemplate(ChaincodeConverter converter) {
		this(converter, null, null);
	}
	
	public AbstractChaincodeTemplate(ChaincodeConverter converter, FabricConfiguration config, FabricKeyValueStore store) {
		this.converter = converter;
		this.mappingContext = converter.getMappingContext();
		this.beanCache = new ChaincodeOperationBeanCache();
		
		this.config = config != null ? config : DefaultFabricConfiguration.INSTANCE.getPropertiesConfiguration();
		this.store = store != null ? store : this.config.getDefaultKeyValueStore();
		
		Assert.notNull(this.config, "FabricConfiguration is null.");
		Assert.notNull(this.store, "FabricKeyValueStore is null.");
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
	public ChaincodeConverter getConverter() {
		return this.converter;
	}
	
	private void checkCriteria(Criteria criteria) {
		Assert.notNull(criteria, "criteria not null!");
		Assert.hasText(criteria.getChannel(), "criteria 'channel' property not null!");
		Assert.hasText(criteria.getOrg(), "criteria 'org' property not null!");
	}
	
	protected ChaincodeTransactionOperations createTransactionOperations(Criteria criteria) {
		checkCriteria(criteria);
		
		if (this.beanCache.containsTransactionOperations(criteria)) {
			return this.beanCache.getTransactionOperationCache(criteria);
		}
		ChaincodeTransactionOperations operations = new ChaincodeTransactionTemplate(criteria.getChannel(), criteria.getOrg(), config, store);
		
		this.beanCache.setTransactionOperationsCache(criteria, operations);
		return operations;
	}
	
	protected ChaincodeDeployOperations createDeployOperations(Criteria criteria) {
		checkCriteria(criteria);
		
		if (this.beanCache.containsTransactionOperations(criteria)) {
			return this.beanCache.getDeployOperationCache(criteria);
		}
		ChaincodeDeployOperations operations = new ChaincodeDeployTemplate(criteria.getChannel(), criteria.getOrg(), config, store);
		
		this.beanCache.setDeployOperationsCache(criteria, operations);
		return operations;
	}
	
	@Override
	public ChaincodeDeployOperations getChaincodeDeployOperations(Criteria criteria) {
		if (this.beanCache.containsDeployOperations(criteria)) {
			return this.beanCache.getDeployOperationCache(criteria);
		}
		
		return createDeployOperations(criteria);
	}

	@Override
	public ChaincodeTransactionOperations getChaincodeTransactionOperations(Criteria criteria) {
		if (this.beanCache.containsTransactionOperations(criteria)) {
			return this.beanCache.getTransactionOperationCache(criteria);
		}
		
		return createTransactionOperations(criteria);
	}
	
	public FabricConfiguration getConfig(Criteria criteria) {
		return this.getChaincodeDeployOperations(criteria).getConfig();
	}

	public Organization getOrganization(Criteria criteria) {
		Assert.hasText(criteria.getOrg(), "criteria.org property is empty!");
		
		return getConfig(criteria).getOrganization(criteria.getOrg());
	}
	
	@SuppressWarnings("unused")
	protected <T extends Options> void afterCriteriaSet(T target) {
		Assert.notNull(target, "parameter is null!");
		
		if (target instanceof QueryCriteria) {
			QueryCriteria criteria = (QueryCriteria) target;
			//System.out.println("criteria --> " + criteria);
		}
	}
}
