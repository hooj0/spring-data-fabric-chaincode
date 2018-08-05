package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.User;
import org.springframework.util.Assert;

import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.TransactionsOptions;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.DeployChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder.InstantiateProposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder.Proposal;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.creator.ProposalBuilder.TransactionProposal;
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
public abstract class AbstractChaincodeRepository<T> implements ChaincodeRepository<T>, DeployChaincodeRepository<T> {

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
	
	protected void afterCriteriaSet(Proposal proposal, Options options) {

		afterOptionSet(proposal, options);
		
		if (proposal instanceof TransactionProposal && options instanceof TransactionsOptions) {
			TransactionProposal transactionProposal = (TransactionProposal) proposal;
			TransactionsOptions transactionsOptions = (TransactionsOptions) options;

			afterTransactionSet(transactionProposal, transactionsOptions);
		}
		
		if (proposal instanceof InstantiateProposal && options instanceof InstantiateOptions) {
			InstantiateProposal instantiateProposal = (InstantiateProposal) proposal;
			InstantiateOptions instantiateOptions = (InstantiateOptions) options;

			afterInstantiateSet(instantiateProposal, instantiateOptions);
		}
	}
	
	protected void afterOptionSet(Proposal proposal, Options options) {
		options.setSpecificPeers(proposal.isSpecificPeers());
		options.setClientUserContext(getUser(proposal.getClientUser()));
		options.setProposalWaitTime(proposal.getProposalWaitTime());
		options.setTransientData(proposal.getTransientData());
		options.setRequestUser(getUser(proposal.getRequestUser()));
	}
	
	protected void afterTransactionSet(TransactionProposal proposal, TransactionsOptions options) {
		options.setOptions(proposal.getOptions());
		options.setOrderers(proposal.getOrderers());
		options.setTransactionsUser(getUser(proposal.getTransactionsUser()));
		options.setSend2Peers(proposal.getSend2Peers());
	}
	
	protected void afterInstantiateSet(InstantiateProposal proposal, InstantiateOptions options) {
		options.setEndorsementPolicy(proposal.getEndorsementPolicy());
		options.setEndorsementPolicyFile(proposal.getEndorsementPolicyFile());
		options.setEndorsementPolicyInputStream(proposal.getEndorsementPolicyInputStream());
	}
	
	protected User getUser(String user) {
		if (!StringUtils.isBlank(user)) {
			Organization org = operations.getOrganization(criteria);
			Assert.notNull(org, "Organization not found!");
			
			return org.getUser(user);
		}
		
		return null;
	}
	
	@Override
	public Criteria getCriteria() {
		return this.criteria;
	}
	
	@Override
	public Organization getOrganization() {
		return operations.getOrganization(criteria);
	}
	
	@Override
	public FabricConfiguration getConfig() {
		return operations.getConfig(criteria);
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
