package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstallCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstantiateCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InvokeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.QueryCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.UpgradeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.ProposalBuilder.InstallProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.ProposalBuilder.InstantiateProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.ProposalBuilder.InvokeProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.ProposalBuilder.QueryProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.ProposalBuilder.UpgradeProposal;

/**
 * Chaincode Repository Simple Implements
 * @author hoojo
 * @createDate 2018年7月18日 上午9:31:41
 * @file SimpleChaincodeRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class SimpleChaincodeRepository<T> extends AbstractChaincodeRepository<T> {

	public SimpleChaincodeRepository(Criteria globalCriteria, ChaincodeEntityInformation<T, ?> metadata, ChaincodeOperations operations) {
		super(globalCriteria, metadata, operations);
	}
	
	public SimpleChaincodeRepository(ChaincodeOperations operations) {
		super(operations);
	}
	
	@Override
	public ResultSet invoke(InvokeProposal proposal, String func) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invoke(invokeCriteria, func);
	}

	@Override
	public ResultSet invoke(InvokeProposal proposal, String func, Object... args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invoke(invokeCriteria, func, args);
	}

	@Override
	public ResultSet invoke(InvokeProposal proposal, String func, LinkedHashMap<String, Object> args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invoke(invokeCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeProposal proposal, String func) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invokeAsync(invokeCriteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeProposal proposal, String func, Object... args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invokeAsync(invokeCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(InvokeProposal proposal, String func, LinkedHashMap<String, Object> args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invokeAsync(invokeCriteria, func, args);
	}

	@Override
	public TransactionEvent invokeFor(InvokeProposal proposal, String func) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invokeFor(invokeCriteria, func);
	}

	@Override
	public TransactionEvent invokeFor(InvokeProposal proposal, String func, Object... args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invokeFor(invokeCriteria, func, args);
	}

	@Override
	public TransactionEvent invokeFor(InvokeProposal proposal, String func, LinkedHashMap<String, Object> args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		
		afterCriteriaSet(proposal, invokeCriteria);
		
		return this.operations.invokeFor(invokeCriteria, func, args);
	}

	@Override
	public String query(QueryProposal proposal, String func) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		afterCriteriaSet(proposal, queryCriteria);
		
		return this.operations.query(queryCriteria, func);
	}

	@Override
	public String query(QueryProposal proposal, String func, Object... args) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		afterCriteriaSet(proposal, queryCriteria);
		
		return this.operations.query(queryCriteria, func, args);
	}

	@Override
	public String query(QueryProposal proposal, String func, LinkedHashMap<String, Object> args) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		afterCriteriaSet(proposal, queryCriteria);
		
		return this.operations.query(queryCriteria, func, args);
	}

	@Override
	public ResultSet queryFor(QueryProposal proposal, String func) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		afterCriteriaSet(proposal, queryCriteria);
		
		return this.operations.queryFor(queryCriteria, func);
	}

	@Override
	public ResultSet queryFor(QueryProposal proposal, String func, Object... args) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		afterCriteriaSet(proposal, queryCriteria);
		
		return this.operations.queryFor(queryCriteria, func, args);
	}

	@Override
	public ResultSet queryFor(QueryProposal proposal, String func, LinkedHashMap<String, Object> args) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		afterCriteriaSet(proposal, queryCriteria);
		
		return this.operations.queryFor(queryCriteria, func, args);
	}
	
	@Override
	public Collection<ProposalResponse> install(InstallProposal proposal, String chaincodeSourceLocation) {
		InstallCriteria installCriteria = new InstallCriteria(criteria);
		installCriteria.setChaincodeUpgradeVersion(proposal.getUpgradeVersion());
		
		afterCriteriaSet(proposal, installCriteria);
		
		return this.operations.install(installCriteria, chaincodeSourceLocation);
	}

	@Override
	public Collection<ProposalResponse> install(InstallProposal proposal, File chaincodeSourceFile) {
		InstallCriteria installCriteria = new InstallCriteria(criteria);
		installCriteria.setChaincodeUpgradeVersion(proposal.getUpgradeVersion());
		
		afterCriteriaSet(proposal, installCriteria);
		
		return this.operations.install(installCriteria, chaincodeSourceFile);
	}

	@Override
	public Collection<ProposalResponse> install(InstallProposal proposal, InputStream chaincodeInputStream) {
		InstallCriteria installCriteria = new InstallCriteria(criteria);
		installCriteria.setChaincodeUpgradeVersion(proposal.getUpgradeVersion());
		
		afterCriteriaSet(proposal, installCriteria);
		
		return this.operations.install(installCriteria, chaincodeInputStream);
	}

	@Override
	public ResultSet instantiate(InstantiateProposal proposal, String func) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiate(instantiateCriteria, func);
	}

	@Override
	public ResultSet instantiate(InstantiateProposal proposal, String func, Object... args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiate(instantiateCriteria, func, args);
	}

	@Override
	public ResultSet instantiate(InstantiateProposal proposal, String func, LinkedHashMap<String, Object> args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiate(instantiateCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateProposal proposal, String func) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiateAsync(instantiateCriteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateProposal proposal, String func, Object... args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiateAsync(instantiateCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(InstantiateProposal proposal, String func, LinkedHashMap<String, Object> args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiateAsync(instantiateCriteria, func, args);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateProposal proposal, String func) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiateFor(instantiateCriteria, func);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateProposal proposal, String func, Object... args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiateFor(instantiateCriteria, func, args);
	}

	@Override
	public TransactionEvent instantiateFor(InstantiateProposal proposal, String func, LinkedHashMap<String, Object> args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		afterCriteriaSet(proposal, instantiateCriteria);
		
		return this.operations.instantiateFor(instantiateCriteria, func, args);
	}

	@Override
	public ResultSet upgrade(UpgradeProposal proposal, String func) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgrade(upgradeCriteria, func);
	}

	@Override
	public ResultSet upgrade(UpgradeProposal proposal, String func, Object... args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgrade(upgradeCriteria, func, args);
	}

	@Override
	public ResultSet upgrade(UpgradeProposal proposal, String func, LinkedHashMap<String, Object> args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgrade(upgradeCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeProposal proposal, String func) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgradeAsync(upgradeCriteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeProposal proposal, String func, Object... args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgradeAsync(upgradeCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(UpgradeProposal proposal, String func, LinkedHashMap<String, Object> args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgradeAsync(upgradeCriteria, func, args);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeProposal proposal, String func) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgradeFor(upgradeCriteria, func);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeProposal proposal, String func, Object... args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgradeFor(upgradeCriteria, func, args);
	}

	@Override
	public TransactionEvent upgradeFor(UpgradeProposal proposal, String func, LinkedHashMap<String, Object> args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		afterCriteriaSet(proposal, upgradeCriteria);
		
		return this.operations.upgradeFor(upgradeCriteria, func, args);
	}
}
