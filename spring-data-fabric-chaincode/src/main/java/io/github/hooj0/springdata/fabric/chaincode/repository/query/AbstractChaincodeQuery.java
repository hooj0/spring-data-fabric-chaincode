package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.DateTimeConverters;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria.CriteriaBuilder;
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
		
		if (method.hasInstallAnnotated()) {
			System.err.println("install: " + method.getChannelAnnotated());
			
			CriteriaBuilder.newBuilder().channel(method.getChannelAnnotated().name()).build();
			
		} else if (method.hasInstantiateAnnotated()) {
			System.err.println("Instantiate: " + method.getChannelAnnotated());
		} else if (method.hasInstantiateAnnotated()) {
			System.err.println("Instantiate: " + method.getChannelAnnotated());
		} else if (method.hasInvokeAnnotated()) {
			System.err.println("Invoke: " + method.getChannelAnnotated());
		} else if (method.hasQueryAnnotated()) {
			System.err.println("Query: " + method.getChannelAnnotated());
		}
		
		ParametersParameterAccessor accessor = new ParametersParameterAccessor(method.getParameters(), parameterValues);
		log.info("execute accessor: {}", accessor);
		
		String[] stringQueries = createQuery(accessor);
		log.info("query stringQueries: {}", new Object[] { stringQueries });
		
		ResultProcessor processor = method.getResultProcessor().withDynamicProjection(accessor);
		Class<?> typeToRead = processor.getReturnedType().getTypeToRead();
		log.info("query result: {}", typeToRead);
		
		// return operations.queryForObject(stringQuery, queryMethod.getEntityInformation().getJavaType());
		return null;
	}
}
