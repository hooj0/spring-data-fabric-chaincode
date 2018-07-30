package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.springframework.util.Assert;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstallCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstantiateCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InvokeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.QueryCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.UpgradeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.DeployChaincodeRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * ChaincodeRepository Base abstract repository
 * @author hoojo
 * @createDate 2018年7月18日 上午9:18:34
 * @file AbstractChaincodeRepositoryQuery.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@NoArgsConstructor
@Slf4j
public class AbstractChaincodeRepository<T> implements ChaincodeRepository<T>, DeployChaincodeRepository<T> {

	protected ChaincodeEntityInformation<T, ?> entityInformation;
	protected ChaincodeOperations operations;
	protected Class<T> entityClass;
	protected Criteria criteria;

	public AbstractChaincodeRepository(ChaincodeOperations operations) {
		this.operations = operations;
		
		Assert.notNull(operations, "ChaincodeOperations must not be null!");
	}
	
	public AbstractChaincodeRepository(Criteria criteria, ChaincodeEntityInformation<T, ?> entityInformation, ChaincodeOperations operations) {
		this(operations);
		
		Assert.notNull(criteria, "criteria must not be null!");

		this.entityInformation = entityInformation;
		this.criteria = criteria;

		log.debug("criteria: {}", criteria);
	}
	
	@Override
	public ResultSet invoke(String func) {
		
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		
		return this.operations.invoke(invokeCriteria, func);
	}

	@Override
	public ResultSet invoke(String func, Object... args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		
		return this.operations.invoke(invokeCriteria, func, args);
	}

	@Override
	public ResultSet invoke(String func, LinkedHashMap<String, Object> args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		
		return this.operations.invoke(invokeCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(String func) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		// invokeCriteria.setOptions(options);
		// invokeCriteria.setOrderers(orderers);
		// invokeCriteria.setTransactionsUser(transactionsUser);
		// invokeCriteria.setTransactionWaitTime(transactionWaitTime);
		
		return this.operations.invokeAsync(invokeCriteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(String func, Object... args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		// invokeCriteria.setOptions(options);
		// invokeCriteria.setOrderers(orderers);
		// invokeCriteria.setTransactionsUser(transactionsUser);
		// invokeCriteria.setTransactionWaitTime(transactionWaitTime);
		
		return this.operations.invokeAsync(invokeCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> invokeAsync(String func, LinkedHashMap<String, Object> args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		// invokeCriteria.setOptions(options);
		// invokeCriteria.setOrderers(orderers);
		// invokeCriteria.setTransactionsUser(transactionsUser);
		// invokeCriteria.setTransactionWaitTime(transactionWaitTime);
		
		return this.operations.invokeAsync(invokeCriteria, func, args);
	}

	@Override
	public TransactionEvent invokeFor(String func) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		// invokeCriteria.setOptions(options);
		// invokeCriteria.setOrderers(orderers);
		// invokeCriteria.setTransactionsUser(transactionsUser);
		// invokeCriteria.setTransactionWaitTime(transactionWaitTime);
		
		return this.operations.invokeFor(invokeCriteria, func);
	}

	@Override
	public TransactionEvent invokeFor(String func, Object... args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		// invokeCriteria.setOptions(options);
		// invokeCriteria.setOrderers(orderers);
		// invokeCriteria.setTransactionsUser(transactionsUser);
		// invokeCriteria.setTransactionWaitTime(transactionWaitTime);
		
		return this.operations.invokeFor(invokeCriteria, func, args);
	}

	@Override
	public TransactionEvent invokeFor(String func, LinkedHashMap<String, Object> args) {
		InvokeCriteria invokeCriteria = new InvokeCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		// invokeCriteria.setOptions(options);
		// invokeCriteria.setOrderers(orderers);
		// invokeCriteria.setTransactionsUser(transactionsUser);
		// invokeCriteria.setTransactionWaitTime(transactionWaitTime);
		
		return this.operations.invokeFor(invokeCriteria, func, args);
	}

	@Override
	public String query(String func) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		// invokeCriteria.setClientUserContext(clientUserContext);
		// invokeCriteria.setOptions(options);
		// invokeCriteria.setOrderers(orderers);
		// invokeCriteria.setTransactionsUser(transactionsUser);
		// invokeCriteria.setTransactionWaitTime(transactionWaitTime);
		
		return this.operations.query(queryCriteria, func);
	}

	@Override
	public String query(String func, Object... args) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		return this.operations.query(queryCriteria, func, args);
	}

	@Override
	public String query(String func, LinkedHashMap<String, Object> args) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		return this.operations.query(queryCriteria, func, args);
	}

	@Override
	public ResultSet queryFor(String func) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		return this.operations.queryFor(queryCriteria, func);
	}

	@Override
	public ResultSet queryFor(String func, Object... args) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		return this.operations.queryFor(queryCriteria, func, args);
	}

	@Override
	public ResultSet queryFor(String func, LinkedHashMap<String, Object> args) {
		QueryCriteria queryCriteria = new QueryCriteria(criteria);
		
		return this.operations.queryFor(queryCriteria, func, args);
	}
	
	@Override
	public Collection<ProposalResponse> install(String chaincodeSourceLocation) {
		InstallCriteria installCriteria = new InstallCriteria(criteria);
		
		return this.operations.install(installCriteria, chaincodeSourceLocation);
	}

	@Override
	public Collection<ProposalResponse> install(File chaincodeSourceFile) {
		InstallCriteria installCriteria = new InstallCriteria(criteria);
		
		return this.operations.install(installCriteria, chaincodeSourceFile);
	}

	@Override
	public Collection<ProposalResponse> install(InputStream chaincodeInputStream) {
		InstallCriteria installCriteria = new InstallCriteria(criteria);
		
		return this.operations.install(installCriteria, chaincodeInputStream);
	}

	@Override
	public ResultSet instantiate(String func) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiate(instantiateCriteria, func);
	}

	@Override
	public ResultSet instantiate(String func, Object... args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiate(instantiateCriteria, func, args);
	}

	@Override
	public ResultSet instantiate(String func, LinkedHashMap<String, Object> args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiate(instantiateCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(String func) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiateAsync(instantiateCriteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(String func, Object... args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiateAsync(instantiateCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> instantiateAsync(String func, LinkedHashMap<String, Object> args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiateAsync(instantiateCriteria, func, args);
	}

	@Override
	public TransactionEvent instantiateFor(String func) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiateFor(instantiateCriteria, func);
	}

	@Override
	public TransactionEvent instantiateFor(String func, Object... args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiateFor(instantiateCriteria, func, args);
	}

	@Override
	public TransactionEvent instantiateFor(String func, LinkedHashMap<String, Object> args) {
		InstantiateCriteria instantiateCriteria = new InstantiateCriteria(criteria);
		
		return this.operations.instantiateFor(instantiateCriteria, func, args);
	}

	@Override
	public ResultSet upgrade(String func) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgrade(upgradeCriteria, func);
	}

	@Override
	public ResultSet upgrade(String func, Object... args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgrade(upgradeCriteria, func, args);
	}

	@Override
	public ResultSet upgrade(String func, LinkedHashMap<String, Object> args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgrade(upgradeCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(String func) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgradeAsync(upgradeCriteria, func);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(String func, Object... args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgradeAsync(upgradeCriteria, func, args);
	}

	@Override
	public CompletableFuture<TransactionEvent> upgradeAsync(String func, LinkedHashMap<String, Object> args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgradeAsync(upgradeCriteria, func, args);
	}

	@Override
	public TransactionEvent upgradeFor(String func) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgradeFor(upgradeCriteria, func);
	}

	@Override
	public TransactionEvent upgradeFor(String func, Object... args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgradeFor(upgradeCriteria, func, args);
	}

	@Override
	public TransactionEvent upgradeFor(String func, LinkedHashMap<String, Object> args) {
		UpgradeCriteria upgradeCriteria = new UpgradeCriteria(criteria);
		
		return this.operations.upgradeFor(upgradeCriteria, func, args);
	}
	
	
	@Override
	public Class<T> getEntityClass() {
		if (!isEntityClassSet()) {
			try {
				this.entityClass = resolveReturnedClassFromGenericType();
			} catch (Exception e) {
				throw new RuntimeException("Unable to resolve EntityClass. Please use according setter!", e);
			}
		}
		
		return entityClass;
	}
	
	private boolean isEntityClassSet() {
		return entityClass != null;
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> resolveReturnedClassFromGenericType() {
		ParameterizedType parameterizedType = resolveReturnedClassFromGenericType(getClass());
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	
	private ParameterizedType resolveReturnedClassFromGenericType(Class<?> clazz) {
		Object genericSuperclass = clazz.getGenericSuperclass();
		
		if (genericSuperclass instanceof ParameterizedType) {
			
			ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
			Type rawtype = parameterizedType.getRawType();
			if (SimpleChaincodeRepository.class.equals(rawtype)) {
				return parameterizedType;
			}
		}
		return resolveReturnedClassFromGenericType(clazz.getSuperclass());
	}
}
