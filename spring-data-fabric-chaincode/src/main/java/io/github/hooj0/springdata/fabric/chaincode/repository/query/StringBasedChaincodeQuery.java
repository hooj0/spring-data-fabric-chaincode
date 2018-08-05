package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.google.common.base.Optional;

import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Instantiate;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Proposal;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Transaction;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Upgrade;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstallCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstantiateCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InvokeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.QueryCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.UpgradeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.parser.ExpressionEvaluatingParameterBinder;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.parser.SimpleStatement;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.parser.StringBasedQueryBinder;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.parser.StringBasedQueryParser;
import lombok.extern.slf4j.Slf4j;

/**
 * String 类型的 Chaincode Query，多用于注解配置的查询方式
 * @changelog Chaincode Query of type String, mostly used for annotation configuration query
 * @author hoojo
 * @createDate 2018年7月18日 下午3:26:07
 * @file StringBasedChaincodeQuery.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class StringBasedChaincodeQuery extends AbstractChaincodeQuery {

	private static final String QUERY_ARGS_SEPARATOR = "_;_";

	private final StringBasedQueryParser parser;
	private StringBasedQueryBinder binder;
	private String query;
	
	public StringBasedChaincodeQuery(ChaincodeQueryMethod method, ChaincodeOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		this(StringUtils.join(method.getRequiredAnnotatedQuery(), QUERY_ARGS_SEPARATOR), method, operations, expressionParser, evaluationContextProvider);
	}

	public StringBasedChaincodeQuery(String namedQuery, ChaincodeQueryMethod queryMethod, ChaincodeOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		super(queryMethod, operations);
		this.query = namedQuery;
		
		this.parser = new StringBasedQueryParser(conversionService);
		if (StringUtils.isNotBlank(query)) {
			this.binder = new StringBasedQueryBinder(query, new ExpressionEvaluatingParameterBinder(expressionParser, evaluationContextProvider));
		} 
	}

	@Override
	protected Object[] createQuery(ParametersParameterAccessor parameterAccessor, Object[] parameterValues) {

		if (hasSerializeParameter()) {
			parameterValues = serializeParameter(parameterValues);
		} 
		
		if (StringUtils.isNotBlank(query)) {
			log.debug("args string: {}", query);

			SimpleStatement statement = binder.bindQuery(parameterAccessor, method, parameterValues);
			log.debug("binder query statement: {}, args: {}", statement.getBindableStatement(), statement.getArray());
			
			String result = parser.replacePlaceholders(statement.getBindableStatement(), statement.getArray());
			log.debug("parser args: {}", new Object[] { result });
			
			return StringUtils.split(result, QUERY_ARGS_SEPARATOR);
		}
		
		return parameterValues;
	}
	
	@Override
	public Object execute(Object[] parameterValues) {
		
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(method.getParameters(), parameterValues);
		
		Object[] params = createQuery(accessor, parameterValues);
		log.info("query string params: {}", new Object[] { params });

		params = Optional.fromNullable(params).or(parameterValues);
		
		ResultProcessor processor = method.getResultProcessor().withDynamicProjection(accessor);
		
		if (method.hasInstallAnnotated()) {
			
			return doInstall(params, processor.getReturnedType());
		} else if (method.hasInstantiateAnnotated()) {
			
			return doInstaantiate(params, processor.getReturnedType());
		} else if (method.hasUpgradeAnnotated()) {
			
			return doUpgrade(params, processor.getReturnedType());
		} else if (method.hasInvokeAnnotated()) {
			
			return doInvoke(params, processor.getReturnedType());
		} else if (method.hasQueryAnnotated()) {
			
			return doQuery(params, processor.getReturnedType());
		}
		
		return null;
	}
	
	private Object doInstall(Object[] parameterValues, ReturnedType returnedType) {
		Install install = method.getInstallAnnotated();
		
		InstallCriteria criteria = new InstallCriteria(method.getCriteria());
		criteria.setChaincodeUpgradeVersion(install.version());
		
		Proposal proposal = method.getProposalAnnotated();
		if (proposal != null) {
			criteria.setClientUserContext(getUser(StringUtils.defaultIfBlank(install.clientUser(), proposal.clientUser())));
			criteria.setProposalWaitTime(proposal.waitTime());
			criteria.setRequestUser(getUser(proposal.requestUser()));
			criteria.setSpecificPeers(proposal.specificPeers());
		}

		return installOperation(criteria, parameterValues, returnedType, install.chaincodeLocation());
	} 
	
	private Object doInstaantiate(Object[] parameterValues, ReturnedType returnedType) {
		Instantiate instantiate = method.getInstantiateAnnotated();
		
		InstantiateCriteria criteria = new InstantiateCriteria(method.getCriteria());

		criteria.setEndorsementPolicyFile(new File(instantiate.endorsementPolicyFile()));
		
		Proposal proposal = method.getProposalAnnotated();
		if (proposal != null) {
			criteria.setClientUserContext(getUser(StringUtils.defaultIfBlank(instantiate.clientUser(), proposal.clientUser())));
			criteria.setProposalWaitTime(proposal.waitTime());
			criteria.setRequestUser(getUser(proposal.requestUser()));
			criteria.setSpecificPeers(proposal.specificPeers());
		}

		Transaction transaction = method.getTransactionAnnotated();
		if (transaction != null) {
			criteria.setTransactionsUser(getUser(transaction.user()));
			criteria.setTransactionWaitTime(transaction.waitTime());
		}
		
		return instantiateOperation(criteria, parameterValues, returnedType, instantiate.func());
	} 
	
	private Object doUpgrade(Object[] parameterValues, ReturnedType returnedType) {
		Upgrade upgrade = method.getUpgradeAnnotated();
		
		UpgradeCriteria criteria = new UpgradeCriteria(method.getCriteria());
		
		criteria.setEndorsementPolicyFile(new File(upgrade.endorsementPolicyFile()));
		
		Proposal proposal = method.getProposalAnnotated();
		if (proposal != null) {
			criteria.setClientUserContext(getUser(StringUtils.defaultIfBlank(upgrade.clientUser(), proposal.clientUser())));
			criteria.setProposalWaitTime(proposal.waitTime());
			criteria.setRequestUser(getUser(proposal.requestUser()));
			criteria.setSpecificPeers(proposal.specificPeers());
		}

		Transaction transaction = method.getTransactionAnnotated();
		if (transaction != null) {
			criteria.setTransactionsUser(getUser(transaction.user()));
			criteria.setTransactionWaitTime(transaction.waitTime());
		}
		
		return upgradeOperation(criteria, parameterValues, returnedType, upgrade.func());
	} 
	
	private Object doInvoke(Object[] parameterValues, ReturnedType returnedType) {
		Invoke invoke = method.getInvokeAnnotated();
		
		InvokeCriteria criteria = new InvokeCriteria(method.getCriteria());
		
		Proposal proposal = method.getProposalAnnotated();
		if (proposal != null) {
			criteria.setClientUserContext(getUser(StringUtils.defaultIfBlank(invoke.clientUser(), proposal.clientUser())));
			criteria.setProposalWaitTime(proposal.waitTime());
			criteria.setRequestUser(getUser(proposal.requestUser()));
			criteria.setSpecificPeers(proposal.specificPeers());
		}

		Transaction transaction = method.getTransactionAnnotated();
		if (transaction != null) {
			criteria.setTransactionsUser(getUser(transaction.user()));
			criteria.setTransactionWaitTime(transaction.waitTime());
		}
		
		return invokeOperation(criteria, parameterValues, returnedType, invoke.func());
	}
	
	private Object doQuery(Object[] parameterValues, ReturnedType returnedType) {
		Query query = method.getQueryAnnotated();
		
		QueryCriteria criteria = new QueryCriteria(method.getCriteria());
		
		Proposal proposal = method.getProposalAnnotated();
		if (proposal != null) {
			criteria.setClientUserContext(getUser(StringUtils.defaultIfBlank(query.clientUser(), proposal.clientUser())));
			criteria.setProposalWaitTime(proposal.waitTime());
			criteria.setRequestUser(getUser(proposal.requestUser()));
			criteria.setSpecificPeers(proposal.specificPeers());
		}

		return queryOperation(criteria, parameterValues, returnedType, query.func());
	}
}
