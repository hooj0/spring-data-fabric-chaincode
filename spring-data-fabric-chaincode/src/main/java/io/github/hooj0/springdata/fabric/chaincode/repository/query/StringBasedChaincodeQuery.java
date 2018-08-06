package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import com.google.common.base.Optional;

import io.github.hooj0.fabric.sdk.commons.config.FabricConfiguration;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.Options;
import io.github.hooj0.fabric.sdk.commons.core.execution.option.TransactionsOptions;
import io.github.hooj0.springdata.fabric.chaincode.ChaincodeUnsupportedOperationException;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Instantiate;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Proposal;
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
	private final FabricConfiguration config;
	private StringBasedQueryBinder binder;
	private String query;
	
	public StringBasedChaincodeQuery(ChaincodeQueryMethod method, ChaincodeOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		this(StringUtils.join(method.getRequiredAnnotatedQuery(), QUERY_ARGS_SEPARATOR), method, operations, expressionParser, evaluationContextProvider);
	}

	public StringBasedChaincodeQuery(String namedQuery, ChaincodeQueryMethod queryMethod, ChaincodeOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		super(queryMethod, operations);
		
		this.config = operations.getConfig(method.getCriteria());
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
		ResultProcessor processor = method.getResultProcessor().withDynamicProjection(accessor);
		
		Object[] params = createQuery(accessor, parameterValues);
		params = Optional.fromNullable(params).or(parameterValues);
		
		log.info("query string params: {}", new Object[] { params });
		
		ChaincodeExecutor executor = new ChaincodeExecutor(params, processor.getReturnedType());
		
		if (method.hasInstallAnnotated()) {
			return executor.executeInstall();
		} else if (method.hasInstantiateAnnotated()) {
			return executor.executeInstaantiate();
		} else if (method.hasInvokeAnnotated()) {
			return executor.executeInvoke();
		} else if (method.hasQueryAnnotated()) {
			return executor.executeQuery();
		} else if (method.hasUpgradeAnnotated()) {
			return executor.executeUpgrade();
		} 
		
		throw new ChaincodeUnsupportedOperationException("Unknow Support has not @Annotation implemented yet.");
	}
	
	private class ChaincodeExecutor {
		private Object[] parameterValues;
		private ReturnedType returnedType;
		
		public ChaincodeExecutor(Object[] parameterValues, ReturnedType returnedType) {
			this.parameterValues = parameterValues;
			this.returnedType = returnedType;
		}
		
		protected Object executeInstall() {
			Install install = method.getInstallAnnotated();
			InstallCriteria criteria = new InstallCriteria(method.getCriteria());
			
			String chaincodeLocation = null;
			if (install != null) {
				criteria.setChaincodeUpgradeVersion(install.version());
				chaincodeLocation = install.chaincodeLocation();
			} 
			
			Proposal proposal = method.getProposalAnnotated();
			this.afterCriteriaSet(criteria, proposal);

			chaincodeLocation = StringUtils.defaultIfBlank(chaincodeLocation, config.getChaincodeRootPath());
			File chaincodeFile = new File(chaincodeLocation);
			if (!chaincodeFile.exists()) {
				chaincodeFile = Paths.get(config.getCommonRootPath(), chaincodeLocation).toFile();
				log.warn("chaincode source code directory '{}' does not exist, Try to bring the default prefix path: {}", chaincodeLocation, chaincodeFile);
			} 
			
			return installOperation(criteria, parameterValues, returnedType, chaincodeFile);
		} 
		
		private Object executeInstaantiate() {
			Instantiate instantiate = method.getInstantiateAnnotated();
			InstantiateCriteria criteria = new InstantiateCriteria(method.getCriteria());

			String endorsementPolicyFile = null;
			if (instantiate != null) {
				endorsementPolicyFile = instantiate.endorsementPolicyFile();
			}
			criteria.setEndorsementPolicyFile(getPolicyFile(endorsementPolicyFile));
			
			Proposal proposal = method.getProposalAnnotated();
			this.afterCriteriaSet(criteria, proposal);

			Transaction transaction = method.getTransactionAnnotated();
			this.afterTransactionSet(criteria, transaction);
			
			return instantiateOperation(criteria, parameterValues, returnedType, proposal.func());
		} 
		
		private Object executeUpgrade() {
			Upgrade upgrade = method.getUpgradeAnnotated();
			UpgradeCriteria criteria = new UpgradeCriteria(method.getCriteria());
			
			String endorsementPolicyFile = null;
			if (upgrade != null) {
				endorsementPolicyFile = upgrade.endorsementPolicyFile();
			}
			
			endorsementPolicyFile = StringUtils.defaultIfBlank(endorsementPolicyFile, config.getEndorsementPolicyFilePath());
			criteria.setEndorsementPolicyFile(getPolicyFile(endorsementPolicyFile));
			
			Proposal proposal = method.getProposalAnnotated();
			this.afterCriteriaSet(criteria, proposal);

			Transaction transaction = method.getTransactionAnnotated();
			this.afterTransactionSet(criteria, transaction);
			
			return upgradeOperation(criteria, parameterValues, returnedType, proposal.func());
		} 
		
		private Object executeInvoke() {
			InvokeCriteria criteria = new InvokeCriteria(method.getCriteria());
			
			Proposal proposal = method.getProposalAnnotated();
			this.afterCriteriaSet(criteria, proposal);

			Transaction transaction = method.getTransactionAnnotated();
			this.afterTransactionSet(criteria, transaction);
			
			return invokeOperation(criteria, parameterValues, returnedType, proposal.func());
		}
		
		private Object executeQuery() {
			QueryCriteria criteria = new QueryCriteria(method.getCriteria());
			
			Proposal proposal = method.getProposalAnnotated();
			this.afterCriteriaSet(criteria, proposal);

			return queryOperation(criteria, parameterValues, returnedType, proposal.func());
		}
		
		private File getPolicyFile(String endorsementPolicyFile) {
			endorsementPolicyFile = StringUtils.defaultIfBlank(endorsementPolicyFile, config.getEndorsementPolicyFilePath());
			File policyFile = new File(endorsementPolicyFile);
			if (!policyFile.exists()) {
				policyFile = Paths.get(config.getCommonRootPath(), endorsementPolicyFile).toFile();
				log.warn("endorsement policy directory '{}' does not exist, Try to bring the default prefix path: {}", endorsementPolicyFile, policyFile);
			} 
			
			return policyFile;
		}
		
		private void afterTransactionSet(TransactionsOptions options, Transaction transaction) {
			if (transaction != null) {
				options.setTransactionsUser(getUser(transaction.user()));
				options.setTransactionWaitTime(transaction.waitTime());
			}
		}
		
		private void afterCriteriaSet(Options options, Proposal proposal) {
			if (proposal != null) {
				options.setClientUserContext(getUser(proposal.clientUser()));
				options.setProposalWaitTime(proposal.waitTime());
				options.setRequestUser(getUser(proposal.requestUser()));
				options.setSpecificPeers(proposal.specificPeers());
			}
		}
	}
}
