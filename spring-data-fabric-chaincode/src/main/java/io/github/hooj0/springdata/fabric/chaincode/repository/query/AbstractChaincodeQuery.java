package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.User;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Maps;

import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Instantiate;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Upgrade;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.DateTimeConverters;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstallCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstantiateCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InvokeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.QueryCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.UpgradeCriteria;
import lombok.extern.slf4j.Slf4j;

/**
 * Chaincode 智能合约 查询基础扩展类
 * @author hoojo
 * @createDate 2018年7月18日 下午3:12:44
 * @file AbstractChaincodeQuery.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public abstract class AbstractChaincodeQuery implements RepositoryQuery {

	protected final GenericConversionService conversionService = new GenericConversionService();
	protected final MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
	protected final ChaincodeOperations operations;
	protected final ChaincodeQueryMethod method;

	{
		if (!conversionService.canConvert(java.util.Date.class, String.class)) {
			conversionService.addConverter(DateTimeConverters.JavaDateConverter.INSTANCE);
		}
		if (!conversionService.canConvert(org.joda.time.ReadableInstant.class, String.class)) {
			conversionService.addConverter(DateTimeConverters.JodaDateTimeConverter.INSTANCE);
		}
		if (!conversionService.canConvert(org.joda.time.LocalDateTime.class, String.class)) {
			conversionService.addConverter(DateTimeConverters.JodaLocalDateTimeConverter.INSTANCE);
		}
	}

	public AbstractChaincodeQuery(ChaincodeQueryMethod queryMethod, ChaincodeOperations operations) {
		this.method = queryMethod;
		this.operations = operations;
		
		this.mappingContext = operations.getConverter().getMappingContext();
	}

	@Override
	public ChaincodeQueryMethod getQueryMethod() {
		return method;
	}
	
	protected abstract String[] createQuery(ParametersParameterAccessor parameterAccessor);
	
	@Override
	public Object execute(Object[] parameterValues) {
		
		if (method.hasProposalAnnotated()) {
			log.debug("chaincode proposal request: {}, args: {}", method.getName(), parameterValues);
		}
		if (method.hasDeployAnnotated()) {
			log.debug("chaincode deploy request: {}, args: {}", method.getName(), parameterValues);
		} 
		System.err.println(method.getChannelAnnotated());
		
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(method.getParameters(), parameterValues);
		String[] stringQueries = createQuery(accessor);
		log.info("query stringQueries: {}", new Object[] { stringQueries });
		
		ResultProcessor processor = method.getResultProcessor().withDynamicProjection(accessor);
		Class<?> typeToRead = processor.getReturnedType().getTypeToRead();
		
		log.info("query result: {}", typeToRead);
		
		if (method.hasInstallAnnotated()) {
			
			return doInstall(parameterValues);
		} else if (method.hasInstantiateAnnotated()) {
			
			return doInstaantiate(parameterValues);
		} else if (method.hasUpgradeAnnotated()) {
			
			return doUpgrade(parameterValues);
		} else if (method.hasInvokeAnnotated()) {
			
			return doInvoke(parameterValues);
		} else if (method.hasQueryAnnotated()) {
			
			return doQuery(parameterValues);
		}
		
		return null;
	}
	
	protected Map<String, byte[]> transformTransientEntity(Object[] parameterValues) {
		Map<String, byte[]> transientData = Maps.newHashMap();
		
		for (Object param : parameterValues) {
			if (param == null) {
				continue;
			}
			
			if (!ClassUtils.isPrimitiveOrWrapper(param.getClass())) {
				ChaincodePersistentEntity<?> entity = mappingContext.getPersistentEntity(param.getClass());
				if (entity != null) {
					transientData.putAll(combinationTransientEntity(entity, param));
				}
			}
		}
		
		return transientData;
	}
	
	private Map<String, byte[]> combinationTransientEntity(ChaincodePersistentEntity<?> entity, Object param) {
		Map<String, byte[]> transientData = Maps.newHashMap();

		Map<String, String> mappings = entity.getTransientMappings();
		if (mappings.isEmpty()) {
			return transientData;
		}
		
		Set<String> keys = mappings.keySet();
		for (String key : keys) {
			try {
				String value = BeanUtils.getProperty(param, key);
				
				byte[] data = value.getBytes();
				transientData.put(mappings.get(key), data);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				log.error("获取 {} 属性 {} 值异常", param.getClass().getName(), key, e);
			}
		}
		
		return transientData;
	}
	
	protected User getUser(String user) {
		if (!StringUtils.isBlank(user)) {
			Organization org = operations.getOrganization(method.getCriteria().getOrg());
			Assert.notNull(org, "Organization not found!");
			
			return org.getUser(user);
		}
		
		return null;
	}
	
	private Object doInstall(Object[] parameterValues) {
		Install install = method.getInstallAnnotated();
		
		InstallCriteria criteria = new InstallCriteria(method.getCriteria());
		criteria.setChaincodeUpgradeVersion(install.version());
		criteria.setClientUserContext(getUser(install.clientUser()));
		criteria.setProposalWaitTime(install.waitTime());
		criteria.setRequestUser(getUser(install.requestUser()));
		criteria.setSpecificPeers(install.specificPeers());
		
		criteria.setTransientData(transformTransientEntity(parameterValues));

		return operations.install(criteria, install.chaincodeLocation());
	} 
	
	private Object doInstaantiate(Object[] parameterValues) {
		Instantiate instantiate = method.getInstantiateAnnotated();
		
		InstantiateCriteria criteria = new InstantiateCriteria(method.getCriteria());
		
		criteria.setEndorsementPolicyFile(new File(instantiate.endorsementPolicyFile()));
		
		criteria.setClientUserContext(getUser(instantiate.clientUser()));
		criteria.setProposalWaitTime(instantiate.proposalWaitTime());
		criteria.setRequestUser(getUser(instantiate.requestUser()));
		criteria.setSpecificPeers(instantiate.specificPeers());

		criteria.setTransactionsUser(getUser(instantiate.transactionsUser()));
		criteria.setTransactionWaitTime(instantiate.transactionWaitTime());
		
		criteria.setTransientData(transformTransientEntity(parameterValues));
		
		String func = StringUtils.defaultIfBlank(instantiate.func(), method.getName());
		
		Object[] args = parameterValues;
		if (args != null && args.length != 0) {
			args = instantiate.args();
		}
		
		return operations.instantiate(criteria, func, parameterValues);
	} 
	
	private Object doUpgrade(Object[] parameterValues) {
		Upgrade upgrade = method.getUpgradeAnnotated();
		
		UpgradeCriteria criteria = new UpgradeCriteria(method.getCriteria());
		
		criteria.setEndorsementPolicyFile(new File(upgrade.endorsementPolicyFile()));
		
		criteria.setClientUserContext(getUser(upgrade.clientUser()));
		criteria.setProposalWaitTime(upgrade.proposalWaitTime());
		criteria.setRequestUser(getUser(upgrade.requestUser()));
		criteria.setSpecificPeers(upgrade.specificPeers());

		criteria.setTransactionsUser(getUser(upgrade.transactionsUser()));
		criteria.setTransactionWaitTime(upgrade.transactionWaitTime());
		
		criteria.setTransientData(transformTransientEntity(parameterValues));

		String func = StringUtils.defaultIfBlank(upgrade.func(), method.getName());
		
		Object[] args = parameterValues;
		if (args != null && args.length != 0) {
			args = upgrade.args();
		}
		
		//return operations.upgradeFor(criteria, func, parameterValues);
		return operations.upgrade(criteria, func, parameterValues);
	} 
	
	private Object doInvoke(Object[] parameterValues) {
		Invoke invoke = method.getInvokeAnnotated();
		
		InvokeCriteria criteria = new InvokeCriteria(method.getCriteria());
		
		criteria.setClientUserContext(getUser(invoke.clientUser()));
		criteria.setProposalWaitTime(invoke.proposalWaitTime());
		criteria.setRequestUser(getUser(invoke.requestUser()));
		criteria.setSpecificPeers(invoke.specificPeers());

		criteria.setTransactionsUser(getUser(invoke.transactionsUser()));
		criteria.setTransactionWaitTime(invoke.transactionWaitTime());
		
		criteria.setTransientData(transformTransientEntity(parameterValues));

		String func = StringUtils.defaultIfBlank(invoke.func(), method.getName());
		
		Object[] args = parameterValues;
		if (args != null && args.length != 0) {
			args = invoke.args();
		}
		
		return operations.invoke(criteria, func, parameterValues);
	}
	
	private Object doQuery(Object[] parameterValues) {
		Query query = method.getQueryAnnotated();
		
		QueryCriteria criteria = new QueryCriteria(method.getCriteria());
		
		criteria.setClientUserContext(getUser(query.clientUser()));
		criteria.setProposalWaitTime(query.waitTime());
		criteria.setRequestUser(getUser(query.requestUser()));
		criteria.setSpecificPeers(query.specificPeers());

		criteria.setTransientData(transformTransientEntity(parameterValues));

		String func = StringUtils.defaultIfBlank(query.func(), method.getName());
		
		Object[] args = parameterValues;
		if (args != null && args.length != 0) {
			args = query.args();
		}
		
		return operations.query(criteria, func, args);
	}
}
