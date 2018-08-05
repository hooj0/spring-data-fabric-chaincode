package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
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
import io.github.hooj0.springdata.fabric.chaincode.enums.SerializationMode;
import lombok.extern.slf4j.Slf4j;

/**
 * Chaincode `invoke & query & instantiate & install & upgrade` abstract support
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
	
	protected abstract Object[] createQuery(ParametersParameterAccessor parameterAccessor, Object[] parameterValues);
	
	protected Object installOperation(InstallCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String chaincodeFile) {
		criteria.setTransientData(transformTransientData(parameterValues));

		return operations.install(criteria, chaincodeFile);
	} 
	
	protected Object instantiateOperation(InstantiateCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String func) {
		criteria.setTransientData(transformTransientData(parameterValues));
		
		func = StringUtils.defaultIfBlank(func, method.getName());
		
		Class<?> resultClass = returnedType.getReturnedType();
		
		if (ClassUtils.isAssignable(CompletableFuture.class, method.getResultType()) && ClassUtils.isAssignable(TransactionEvent.class, resultClass)) {
			return operations.instantiateAsync(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(TransactionEvent.class, resultClass)) {
			return operations.instantiateFor(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(ResultSet.class, resultClass)) {
			return operations.instantiate(criteria, func, parameterValues);
		}
		
		ResultSet result = operations.instantiate(criteria, func, parameterValues);
		
		if (hasDeserializeResult()) {
			return deserializeResult(resultClass, result.getResult());
		} else if (ClassUtils.isAssignable(String.class, resultClass)) {
			return result.getResult();
		} else if (!ClassUtils.isPrimitiveOrWrapper(resultClass)) {
			return serialization.deserialize(resultClass, result.getResult());
		}
		
		return result.getResult();
	} 
	
	protected Object upgradeOperation(UpgradeCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String func) {
		criteria.setTransientData(transformTransientData(parameterValues));

		func = StringUtils.defaultIfBlank(func, method.getName());
		
		Class<?> resultClass = returnedType.getReturnedType();
		
		if (ClassUtils.isAssignable(CompletableFuture.class, method.getResultType()) && ClassUtils.isAssignable(TransactionEvent.class, resultClass)) {
			return operations.upgradeAsync(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(TransactionEvent.class, resultClass)) {
			return operations.upgradeFor(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(ResultSet.class, resultClass)) {
			return operations.upgrade(criteria, func, parameterValues);
		}
		
		ResultSet result = operations.upgrade(criteria, func, parameterValues);
		
		if (hasDeserializeResult()) {
			return deserializeResult(resultClass, result.getResult());
		} else if (ClassUtils.isAssignable(String.class, resultClass)) {
			return result.getResult();
		} else if (!ClassUtils.isPrimitiveOrWrapper(resultClass)) {
			return serialization.deserialize(resultClass, result.getResult());
		}
		
		return result.getResult();
	} 
	
	@SuppressWarnings("serial")
	protected Object invokeOperation(InvokeCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String func) {
		criteria.setTransientData(transformTransientData(parameterValues));

		func = StringUtils.defaultIfBlank(func, method.getName());
		
		Class<?> resultClass = returnedType.getReturnedType();
		TypeToken<CompletableFuture<TransactionEvent>> typeToken = new TypeToken<CompletableFuture<TransactionEvent>>() {};
		/*
		System.err.println("1->" + method.getReturnType().getActualType());
		System.err.println("3->" + typeToken.getType());
		System.err.println("4->" + method.getResultType());
		System.err.println("5->" + method.getReturnedObjectType());
		System.err.println("b2->" + method.getReturnType().getActualType().getType());
		System.err.println("e->" + typeToken.getType().getTypeName());
		
		
		1->java.util.concurrent.CompletableFuture<org.hyperledger.fabric.sdk.BlockEvent$TransactionEvent>
		3->java.util.concurrent.CompletableFuture<org.hyperledger.fabric.sdk.BlockEvent$TransactionEvent>
		4->class java.util.concurrent.CompletableFuture
		5->class org.hyperledger.fabric.sdk.BlockEvent$TransactionEvent
		b2->class java.util.concurrent.CompletableFuture
		e->java.util.concurrent.CompletableFuture<org.hyperledger.fabric.sdk.BlockEvent$TransactionEvent>
		 */
		
		boolean isFutrued = StringUtils.equals(typeToken.getType().getTypeName(), method.getReturnType().getActualType().toString());
		boolean isFutruedEvent = (ClassUtils.isAssignable(CompletableFuture.class, method.getResultType()) && ClassUtils.isAssignable(TransactionEvent.class, resultClass));
		if (isFutruedEvent || isFutrued) {
			return operations.invokeAsync(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(TransactionEvent.class, resultClass)) {
			return operations.invokeFor(criteria, func, parameterValues);
		} else if (ClassUtils.isAssignable(ResultSet.class, resultClass)) {
			return operations.invoke(criteria, func, parameterValues);
		}
		
		ResultSet result = operations.invoke(criteria, func, parameterValues);
		
		if (hasDeserializeResult()) {
			return deserializeResult(resultClass, result.getResult());
		} else if (ClassUtils.isAssignable(String.class, resultClass)) {
			return result.getResult();
		} else if (!ClassUtils.isPrimitiveOrWrapper(resultClass)) {
			return serialization.deserialize(resultClass, result.getResult());
		}
		
		return result.getResult();
	} 
	
	protected Object queryOperation(QueryCriteria criteria, Object[] parameterValues, ReturnedType returnedType, String func) {
		criteria.setTransientData(transformTransientData(parameterValues));

		func = StringUtils.defaultIfBlank(func, method.getName());
		
		Class<?> resultClass = returnedType.getReturnedType();
		
		if (ClassUtils.isAssignable(ResultSet.class, resultClass)) {
			return operations.queryFor(criteria, func, parameterValues);
		} 
		
		String result = operations.query(criteria, func, parameterValues);
		
		if (hasDeserializeResult()) {
			return deserializeResult(resultClass, result);
		} else if (ClassUtils.isAssignable(String.class, resultClass)) {
			return result;
		} else if (!ClassUtils.isPrimitiveOrWrapper(resultClass)) {
			return serialization.deserialize(resultClass, result);
		} 
		
		return result;
	} 
	
	protected boolean hasSerializeParameter() {
		
		if (method.hasSerializationAnnotated()) {
			SerializationMode mode = method.getSerializationAnnotated().value();
			if (mode == SerializationMode.ALL || mode == SerializationMode.SERIALIZE) {
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean hasDeserializeResult() {
		
		if (method.hasSerializationAnnotated()) {
			SerializationMode mode = method.getSerializationAnnotated().value();
			if (mode == SerializationMode.ALL || mode == SerializationMode.DESERIALIZE) {
				return true;
			}
		}
		
		return false;
	}
	
	protected String[] serializeParameter(Object[] parameterValues) {

		String[] params = new String[parameterValues.length];
		for (int i = 0; i < parameterValues.length; i++) {
			params[i] = method.getSerializationAnnotated().provider().getSerialization().serialize(parameterValues[i]);
		}
		
		return params;
	}
	
	protected Object deserializeResult(Class<?> clazz, String result) {
		
		return method.getSerializationAnnotated().provider().getSerialization().deserialize(clazz, result);
	}
	
	protected Map<String, byte[]> transformTransientData(Object[] parameterValues) {
		Map<String, byte[]> transientData = Maps.newHashMap();
		
		for (Object param : parameterValues) {
			if (param == null) {
				continue;
			}
			
			if (!ClassUtils.isPrimitiveOrWrapper(param.getClass()) && !ClassUtils.isAssignable(param.getClass(), String.class)) {
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
				
				Object parameter = MethodUtils.invokeMethod(param, "get" + StringUtils.capitalize(key));
				if (parameter == null) {
					continue;
				}
				
				String value = null;
				if (conversionService.canConvert(parameter.getClass(), String.class)) {
					value = conversionService.convert(parameter, String.class);
				} else {
					value = parameter.toString();
				}
				
				transientData.put(mappings.get(key), value.getBytes());
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
