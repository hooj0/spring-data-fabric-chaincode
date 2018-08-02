package io.github.hooj0.springdata.fabric.chaincode.repository;

import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.ProposalBuilder.InvokeProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.ProposalBuilder.QueryProposal;


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
	ResultSet invoke(InvokeProposal proposal, String func);

	ResultSet invoke(InvokeProposal proposal, String func, Object... args);

	ResultSet invoke(InvokeProposal proposal, String func, LinkedHashMap<String, Object> args);


	// invoke async

	CompletableFuture<TransactionEvent> invokeAsync(InvokeProposal proposal, String func);

	CompletableFuture<TransactionEvent> invokeAsync(InvokeProposal proposal, String func, Object... args);

	CompletableFuture<TransactionEvent> invokeAsync(InvokeProposal proposal, String func, LinkedHashMap<String, Object> args);

	// invoke async return event

	TransactionEvent invokeFor(InvokeProposal proposal, String func);

	TransactionEvent invokeFor(InvokeProposal proposal, String func, Object... args);

	TransactionEvent invokeFor(InvokeProposal proposal, String func, LinkedHashMap<String, Object> args);

	
	// query
	String query(QueryProposal proposal, String func);

	String query(QueryProposal proposal, String func, Object... args);

	String query(QueryProposal proposal, String func, LinkedHashMap<String, Object> args);

	
	// query async return event

	ResultSet queryFor(QueryProposal proposal, String func);

	ResultSet queryFor(QueryProposal proposal, String func, Object... args);

	ResultSet queryFor(QueryProposal proposal, String func, LinkedHashMap<String, Object> args);


	Class<T> getEntityClass();
	
	Criteria getCriteria();
}
