package io.github.hooj0.springdata.fabric.chaincode.repository;

import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;


/**
 * chaincode 智能合约 repo
 * @author hoojo
 * @createDate 2018年7月17日 上午9:34:18
 * @file ChaincodeRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@NoRepositoryBean
public interface ChaincodeRepository<T> extends Repository<T, Object> {

	// invoke
	ResultSet invoke(String func);

	ResultSet invoke(String func, Object... args);

	ResultSet invoke(String func, LinkedHashMap<String, Object> args);


	// invoke async

	CompletableFuture<TransactionEvent> invokeAsync(String func);

	CompletableFuture<TransactionEvent> invokeAsync(String func, Object... args);

	CompletableFuture<TransactionEvent> invokeAsync(String func, LinkedHashMap<String, Object> args);

	// invoke async return event

	TransactionEvent invokeFor(String func);

	TransactionEvent invokeFor(String func, Object... args);

	TransactionEvent invokeFor(String func, LinkedHashMap<String, Object> args);

	
	// query
	String query(String func);

	String query(String func, Object... args);

	String query(String func, LinkedHashMap<String, Object> args);

	
	// query async return event

	ResultSet queryFor(String func);

	ResultSet queryFor(String func, Object... args);

	ResultSet queryFor(String func, LinkedHashMap<String, Object> args);


	Class<T> getEntityClass();
}
