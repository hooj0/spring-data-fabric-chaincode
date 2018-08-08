package io.github.hooj0.springdata.fabric.chaincode.core;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ProposalResponse;

import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstallCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstantiateCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InvokeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.QueryCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.UpgradeCriteria;

/**
 * chaincode operations `install & invoke & instantiate & query & upgrade` interface
 * 
 * @author hoojo
 * @createDate 2018年7月17日 上午10:28:28
 * @file ChaincodeOperations.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeOperations {

	ChaincodeConverter getConverter();
	
	ChaincodeDeployOperations getChaincodeDeployOperations(Criteria criteria);

	ChaincodeTransactionOperations getChaincodeTransactionOperations(Criteria criteria);

	Organization getOrganization(Criteria criteria);

	FabricConfiguration getConfig(Criteria criteria);

	
	// install
	
	Collection<ProposalResponse> install(InstallCriteria criteria, String chaincodeSourceLocation);

	Collection<ProposalResponse> install(InstallCriteria criteria, File chaincodeSourceFile);
	
	Collection<ProposalResponse> install(InstallCriteria criteria, InputStream chaincodeInputStream);
	
	// install return result set

	ResultSet installFor(InstallCriteria criteria, String chaincodeSourceLocation);

	ResultSet installFor(InstallCriteria criteria, File chaincodeSourceFile);

	ResultSet installFor(InstallCriteria criteria, InputStream chaincodeInputStream);
	
	
	// instantiate
	
	ResultSet instantiate(InstantiateCriteria criteria, String func);
	
	ResultSet instantiate(InstantiateCriteria criteria, String func, Object... args);

	ResultSet instantiate(InstantiateCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	// instantiate async
	
	CompletableFuture<TransactionEvent> instantiateAsync(InstantiateCriteria criteria, String func);
	
	CompletableFuture<TransactionEvent> instantiateAsync(InstantiateCriteria criteria, String func, Object... args);

	CompletableFuture<TransactionEvent> instantiateAsync(InstantiateCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	// instantiate async return event
	
	TransactionEvent instantiateFor(InstantiateCriteria criteria, String func);
	
	TransactionEvent instantiateFor(InstantiateCriteria criteria, String func, Object... args);

	TransactionEvent instantiateFor(InstantiateCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	
	
	// upgrade
	
	ResultSet upgrade(UpgradeCriteria criteria, String func);
	
	ResultSet upgrade(UpgradeCriteria criteria, String func, Object... args);

	ResultSet upgrade(UpgradeCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	// upgrade async
	
	CompletableFuture<TransactionEvent> upgradeAsync(UpgradeCriteria criteria, String func);
	
	CompletableFuture<TransactionEvent> upgradeAsync(UpgradeCriteria criteria, String func, Object... args);

	CompletableFuture<TransactionEvent> upgradeAsync(UpgradeCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	// upgrade async return event
	
	TransactionEvent upgradeFor(UpgradeCriteria criteria, String func);
	
	TransactionEvent upgradeFor(UpgradeCriteria criteria, String func, Object... args);

	TransactionEvent upgradeFor(UpgradeCriteria criteria, String func, LinkedHashMap<String, Object> args);

	
	
	// invoke

	ResultSet invoke(InvokeCriteria criteria, String func);

	ResultSet invoke(InvokeCriteria criteria, String func, Object... args);

	ResultSet invoke(InvokeCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	// invoke async
	
	CompletableFuture<TransactionEvent> invokeAsync(InvokeCriteria criteria, String func);
	
	CompletableFuture<TransactionEvent> invokeAsync(InvokeCriteria criteria, String func, Object... args);

	CompletableFuture<TransactionEvent> invokeAsync(InvokeCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	// invoke async return event
	
	TransactionEvent invokeFor(InvokeCriteria criteria, String func);
	
	TransactionEvent invokeFor(InvokeCriteria criteria, String func, Object... args);

	TransactionEvent invokeFor(InvokeCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	
	
	// query
	
	String query(QueryCriteria criteria, String func);
	
	String query(QueryCriteria criteria, String func, Object... args);

	String query(QueryCriteria criteria, String func, LinkedHashMap<String, Object> args);
	
	// query async return event
	
	ResultSet queryFor(QueryCriteria criteria, String func);
	
	ResultSet queryFor(QueryCriteria criteria, String func, Object... args);

	ResultSet queryFor(QueryCriteria criteria, String func, LinkedHashMap<String, Object> args);
}
