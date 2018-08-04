package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.User;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ReturnedType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.fabric.sdk.commons.domain.Organization;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.DateTimeConverters;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstallCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InstantiateCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.InvokeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.QueryCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.query.UpgradeCriteria;
import io.github.hooj0.springdata.fabric.chaincode.core.serialize.ChaincodeEntitySerialization;
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
	protected final ChaincodeEntitySerialization serialization;
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
		this.serialization = operations.getConverter().getChaincodeEntitySerialization();
	}

	@Override
	public ChaincodeQueryMethod getQueryMethod() {
		return method;
	}
	
	protected abstract String[] createQuery(ParametersParameterAccessor parameterAccessor, Object[] parameterValues);
	
	protected Object installOperation(InstallCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String chaincodeFile) {
		criteria.setTransientData(transformTransientData(parameterValues));

		@SuppressWarnings("serial")
		TypeToken<Collection<ProposalResponse>> typeToken = new TypeToken<Collection<ProposalResponse>>() {};
		if (ClassUtils.isAssignable(typeToken.getType().getClass(), returnedType.getReturnedType())) {
			return operations.install(criteria, chaincodeFile);
		} 
		
		return operations.install(criteria, chaincodeFile);
	} 
	
	@SuppressWarnings("serial")
	protected Object instaantiateOperation(InstantiateCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String func) {
		criteria.setTransientData(transformTransientData(parameterValues));
		
		func = StringUtils.defaultIfBlank(func, method.getName());
		
		/*if (method.hasSerializationAnnotated()) {
			ResultSet result = operations.instantiate(criteria, func, parameterValues);
			return method.getSerializationAnnotated().provider().getSerialization().deserialize(returnedType.getReturnedType(), result.getResult());
		}*/
		
		TypeToken<CompletableFuture<TransactionEvent>> typeToken = new TypeToken<CompletableFuture<TransactionEvent>>() {};
		
		if (ClassUtils.isAssignable(typeToken.getType().getClass(), returnedType.getReturnedType())) {
			return operations.instantiateAsync(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(TransactionEvent.class, returnedType.getReturnedType())) {
			return operations.instantiateFor(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(ResultSet.class, returnedType.getReturnedType())) {
			return operations.instantiate(criteria, func, parameterValues);
		}
		
		ResultSet result = operations.instantiate(criteria, func, parameterValues);
		if (!ClassUtils.isPrimitiveOrWrapper(returnedType.getReturnedType())) {
			return serialization.deserialize(returnedType.getReturnedType(), result.getResult());
		}
		
		return result.getResult();
	} 
	
	@SuppressWarnings("serial")
	protected Object upgradeOperation(UpgradeCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String func) {
		criteria.setTransientData(transformTransientData(parameterValues));

		func = StringUtils.defaultIfBlank(func, method.getName());
		
		/*if (method.hasSerializationAnnotated()) {
			ResultSet result = operations.upgrade(criteria, func, parameterValues);
			return method.getSerializationAnnotated().provider().getSerialization().deserialize(returnedType.getReturnedType(), result.getResult());
		}*/
		
		TypeToken<CompletableFuture<TransactionEvent>> typeToken = new TypeToken<CompletableFuture<TransactionEvent>>() {};
		
		if (ClassUtils.isAssignable(typeToken.getType().getClass(), returnedType.getReturnedType())) {
			return operations.upgradeAsync(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(TransactionEvent.class, returnedType.getReturnedType())) {
			return operations.upgradeFor(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(ResultSet.class, returnedType.getReturnedType())) {
			return operations.upgrade(criteria, func, parameterValues);
		}
		
		ResultSet result = operations.upgrade(criteria, func, parameterValues);
		if (!ClassUtils.isPrimitiveOrWrapper(returnedType.getReturnedType())) {
			return serialization.deserialize(returnedType.getReturnedType(), result.getResult());
		}
		
		return result.getResult();
	} 
	
	@SuppressWarnings("serial")
	protected Object invokeOperation(InvokeCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String func) {
		criteria.setTransientData(transformTransientData(parameterValues));

		func = StringUtils.defaultIfBlank(func, method.getName());
		
		TypeToken<CompletableFuture<TransactionEvent>> typeToken = new TypeToken<CompletableFuture<TransactionEvent>>() {};
		
		if (ClassUtils.isAssignable(typeToken.getType().getClass(), returnedType.getReturnedType())) {
			return operations.invokeAsync(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(TransactionEvent.class, returnedType.getReturnedType())) {
			return operations.invokeFor(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(ResultSet.class, returnedType.getReturnedType())) {
			return operations.invoke(criteria, func, parameterValues);
		}
		
		ResultSet result = operations.invoke(criteria, func, parameterValues);
		if (!ClassUtils.isPrimitiveOrWrapper(returnedType.getReturnedType())) {
			return serialization.deserialize(returnedType.getReturnedType(), result.getResult());
		}
		
		return result.getResult();
	} 
	
	protected Object queryOperation(QueryCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String func) {
		criteria.setTransientData(transformTransientData(parameterValues));

		func = StringUtils.defaultIfBlank(func, method.getName());
		
		if (ClassUtils.isAssignable(TransactionEvent.class, returnedType.getReturnedType())) {
			return operations.queryFor(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(String.class, returnedType.getReturnedType())) {
			return operations.query(criteria, func, parameterValues);
		}
		
		String result = operations.query(criteria, func, parameterValues);
		if (!ClassUtils.isPrimitiveOrWrapper(returnedType.getReturnedType())) {
			return serialization.deserialize(returnedType.getReturnedType(), result);
		}
		
		return result;
	} 
	
	protected Map<String, byte[]> transformTransientData(Object[] parameterValues) {
		Map<String, byte[]> transientData = Maps.newHashMap();
		
		for (Object param : parameterValues) {
			if (param == null) {
				continue;
			}
			
			if (!ClassUtils.isPrimitiveOrWrapper(param.getClass())) {
				ChaincodePersistentEntity<?> entity = mappingContext.getPersistentEntity(param.getClass());
				if (entity != null) {
					transientData.putAll(combinationTransientData(entity, param));
				}
			}
		}
		
		return transientData;
	}
	
	private Map<String, byte[]> combinationTransientData(ChaincodePersistentEntity<?> entity, Object param) {
		Map<String, byte[]> transientData = Maps.newHashMap();

		Map<String, String> mappings = entity.getTransientMappings();
		if (mappings.isEmpty()) {
			return transientData;
		}
		
		Set<String> keys = mappings.keySet();
		for (String key : keys) {
			try {
				/*
				Method getter = ReflectionUtils.findMethod(param.getClass(), "get" + StringUtils.capitalize(key));
				Object value = ReflectionUtils.invokeMethod(getter, param);

				MethodUtils.invokeMethod(param, "get" + StringUtils.capitalize(key));
				*/
				
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
			Organization org = operations.getOrganization(method.getCriteria());
			Assert.notNull(org, "Organization not found!");
			
			return org.getUser(user);
		}
		
		return null;
	}
}
