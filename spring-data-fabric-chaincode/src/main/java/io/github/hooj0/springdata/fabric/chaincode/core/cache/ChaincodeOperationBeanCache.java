package io.github.hooj0.springdata.fabric.chaincode.core.cache;

import java.util.Map;

import org.springframework.util.Assert;

import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import lombok.extern.slf4j.Slf4j;

/**
 * chaincode operations `transaction & deploy` bean cache
 * @author hoojo
 * @createDate 2018年7月29日 下午4:37:58
 * @file ChaincodeOperationBeanCache.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.cache
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class ChaincodeOperationBeanCache {

	private Map<String, ChaincodeDeployOperations> deployOperationsCache = Maps.newConcurrentMap();
	private Map<String, ChaincodeTransactionOperations> transactionOperationsCache = Maps.newConcurrentMap();
	
	public void setDeployOperationsCache(Criteria criteria, ChaincodeDeployOperations deployOperations) {
		Assert.notNull(deployOperations, "ChaincodeDeployOperations cache bean is null.");
		
		if (!deployOperationsCache.containsKey(getKey(criteria))) {
			deployOperationsCache.put(getKey(criteria), deployOperations);
		} else {
			log.warn("ChaincodeDeployOperations beans key '{}' is contains.", getKey(criteria));
		}
	}
	
	public ChaincodeDeployOperations getDeployOperationCache(Criteria criteria) {
		return deployOperationsCache.get(getKey(criteria));
	}
	
	public boolean containsDeployOperations(Criteria criteria) {
		return deployOperationsCache.containsKey(getKey(criteria));
	}
	
	public void setTransactionOperationsCache(Criteria criteria, ChaincodeTransactionOperations transactionOperations) {
		Assert.notNull(transactionOperations, "ChaincodeTransactionOperations cache bean is null.");
		
		if (!transactionOperationsCache.containsKey(getKey(criteria))) {
			transactionOperationsCache.put(getKey(criteria), transactionOperations);
		} else {
			log.warn("ChaincodeTransactionOperations beans key '{}' is contains.", getKey(criteria));
		}
	}
	
	public ChaincodeTransactionOperations getTransactionOperationCache(Criteria criteria) {
		return transactionOperationsCache.get(getKey(criteria));
	}
	
	public boolean containsTransactionOperations(Criteria criteria) {
		return transactionOperationsCache.containsKey(getKey(criteria));
	}
	
	private String getKey(Criteria criteria) {
		Assert.notNull(criteria, "Criteria is not null!");
		Assert.hasText(criteria.getChannel(), "Criteria.channel property is null!");
		Assert.hasText(criteria.getOrg(), "Criteria.org property is null!");
		
		return criteria.getChannel() + "_" + criteria.getOrg();
	}
}
