package io.github.hooj0.springdata.fabric.chaincode.core.support;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.springframework.util.Assert;

import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.fabric.sdk.commons.store.FabricKeyValueStore;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstallCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstantiateCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InvokeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.QueryCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.UpgradeCriteria;
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
public class ChaincodeTemplate extends AbstractChaincodeTemplate {

	public ChaincodeTemplate() {
		super();
	}

	public ChaincodeTemplate(ChaincodeConverter converter) {
		super(converter);
	}
	
	public ChaincodeTemplate(ChaincodeConverter converter, FabricConfiguration config, FabricKeyValueStore store) {
		super(converter, config, store);
	}

	@Override
	public ResultSet invoke(InvokeCriteria criteria, String func) {
		log.debug("chaincode template exec invoke, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).invoke(criteria, func);
	}

	@Override
	public ResultSet invoke(InvokeCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec invoke, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);
		
		return createTransactionOperations(criteria.getCriteria()).invoke(criteria, func, args);
	}

	@Override
	public ResultSet invoke(InvokeCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec invoke, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).invoke(criteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeCriteria criteria, String func) {
		log.debug("chaincode template exec invokeAsync, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).invokeAsync(criteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec invokeAsync, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).invokeAsync(criteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec invokeAsync, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).invokeAsync(criteria, func, args);
	}

	@Override
	public TransactionEvent invokeFor(InvokeCriteria criteria, String func) {
		log.debug("chaincode template exec invokeFor, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).invokeFor(criteria, func);
	}

	@Override
	public TransactionEvent invokeFor(InvokeCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec invokeFor, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).invokeFor(criteria, func, args);
	}

	@Override
	public TransactionEvent invokeFor(InvokeCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec invokeFor, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).invokeFor(criteria, func, args);
	}

	@Override
	public String query(QueryCriteria criteria, String func) {
		log.debug("chaincode template exec query, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).query(criteria, func);
	}

	@Override
	public String query(QueryCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec query, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).query(criteria, func, args);
	}

	@Override
	public String query(QueryCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec query, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).query(criteria, func, args);
	}

	@Override
	public ResultSet queryFor(QueryCriteria criteria, String func) {
		log.debug("chaincode template exec queryFor, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).queryFor(criteria, func);
	}

	@Override
	public ResultSet queryFor(QueryCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec queryFor, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).queryFor(criteria, func, args);
	}

	@Override
	public ResultSet queryFor(QueryCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec queryFor, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createTransactionOperations(criteria.getCriteria()).queryFor(criteria, func, args);
	}

	@Override
	public Collection<ProposalResponse> install(InstallCriteria criteria, String chaincodeSourceLocation) {
		log.debug("chaincode template exec install, criteria: {}", criteria);
		
		Assert.hasText(chaincodeSourceLocation, "chaincodeSourceLocation is null!");
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).install(criteria, chaincodeSourceLocation);
	}

	@Override
	public Collection<ProposalResponse> install(InstallCriteria criteria, File chaincodeSourceFile) {
		log.debug("chaincode template exec install, criteria: {}", criteria);
		
		Assert.notNull(chaincodeSourceFile, "chaincodeSourceFile is null!");
		Assert.state(chaincodeSourceFile.exists(), "chaincodeSourceFile file is exists");
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).install(criteria, chaincodeSourceFile);
	}

	@Override
	public Collection<ProposalResponse> install(InstallCriteria criteria, InputStream chaincodeInputStream) {
		log.debug("chaincode template exec install, criteria: {}", criteria);
		
		Assert.notNull(chaincodeInputStream, "chaincodeInputStream is null!");
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).install(criteria, chaincodeInputStream);
	}

	@Override
	public ResultSet instantiate(InstantiateCriteria criteria, String func) {
		log.debug("chaincode template exec instantiate, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiate(criteria, func);
	}

	@Override
	public ResultSet instantiate(InstantiateCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec instantiate, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiate(criteria, func, args);
	}

	@Override
	public ResultSet instantiate(InstantiateCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec instantiate, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiate(criteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateCriteria criteria, String func) {
		log.debug("chaincode template exec instantiateAsync, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiateAsync(criteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec instantiateAsync, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiateAsync(criteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec instantiateAsync, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiateAsync(criteria, func, args);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateCriteria criteria, String func) {
		log.debug("chaincode template exec instantiateFor, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiateFor(criteria, func);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec instantiateFor, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiateFor(criteria, func, args);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec instantiateFor, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).instantiateFor(criteria, func, args);
	}

	@Override
	public ResultSet upgrade(UpgradeCriteria criteria, String func) {
		log.debug("chaincode template exec upgrade, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgrade(criteria, func);
	}

	@Override
	public ResultSet upgrade(UpgradeCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec upgrade, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgrade(criteria, func, args);
	}

	@Override
	public ResultSet upgrade(UpgradeCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec upgrade, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgrade(criteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeCriteria criteria, String func) {
		log.debug("chaincode template exec upgradeAsync, criteria: {}, func: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgradeAsync(criteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec upgradeAsync, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgradeAsync(criteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec upgradeAsync, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgradeAsync(criteria, func, args);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeCriteria criteria, String func) {
		log.debug("chaincode template exec upgradeFor, criteria: {}, func: {}, args: {}", criteria, func);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgradeFor(criteria, func);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeCriteria criteria, String func, Object... args) {
		log.debug("chaincode template exec upgradeFor, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgradeFor(criteria, func, args);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeCriteria criteria, String func, LinkedHashMap<String, Object> args) {
		log.debug("chaincode template exec upgradeFor, criteria: {}, func: {}, args: {}", criteria, func, args);
		
		afterCriteriaSet(criteria);

		return createDeployOperations(criteria.getCriteria()).upgradeFor(criteria, func, args);
	}
}
