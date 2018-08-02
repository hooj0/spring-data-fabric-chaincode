package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.parser.AbstractQueryCreator;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;

import com.google.common.collect.Lists;

import io.github.hooj0.springdata.fabric.chaincode.ChaincodeUnsupportedOperationException;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * PartTree Chaincode Query Creator
 * @author hoojo
 * @createDate 2018年7月18日 下午4:41:24
 * @file ChaincodeQueryCreator.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class ChaincodeQueryCreator extends AbstractQueryCreator<List<String>, List<String>> {

	private final GenericConversionService conversionService;
	private final MappingContext<?, ChaincodePersistentProperty> context;

	public ChaincodeQueryCreator(PartTree tree, ParameterAccessor parameters, MappingContext<?, ChaincodePersistentProperty> context, GenericConversionService conversionService) {
		super(tree, parameters);
		
		this.conversionService = conversionService;
		this.context = context;
	}

	@Override
	protected List<String> create(Part part, Iterator<Object> iterator) {
		PersistentPropertyPath<ChaincodePersistentProperty> path = context.getPersistentPropertyPath(part.getProperty());
		
		return Lists.newArrayList(from(part, new String(path.toDotPath(ChaincodePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator));
	}

	@Override
	protected List<String> and(Part part, List<String> base, Iterator<Object> iterator) {
		if (base == null) {
			return create(part, iterator);
		}
		
		PersistentPropertyPath<ChaincodePersistentProperty> path = context.getPersistentPropertyPath(part.getProperty());
		String[] queries = from(part, new String(path.toDotPath(ChaincodePersistentProperty.PropertyToFieldNameConverter.INSTANCE)), iterator);
		
		base.addAll(Arrays.asList(queries));
		return base;
	}

	@Override
	protected List<String> or(List<String> base, List<String> query) {
		
		throw new ChaincodeUnsupportedOperationException("Chaincode does not support an OR operator");
	}

	@Override
	protected List<String> complete(List<String> queries, Sort sort) {
		if (queries == null) {
			return null;
		}
		
		return queries;
	}

	private String[] from(Part part, String instance, Iterator<?> parameters) {
		Part.Type type = part.getType();

		String criteria = instance;
		if (criteria == null) {
			criteria = new String();
		}
		String property = part.getProperty().getSegment();
		
		switch (type) {
			case TRUE:
			case FALSE:
			case NEGATING_SIMPLE_PROPERTY:
			case REGEX:
			case LIKE:
			case STARTING_WITH:
			case ENDING_WITH:
			case CONTAINING:
				return new String[] { property, join(parameters) };
			case GREATER_THAN:
			case AFTER:
			case GREATER_THAN_EQUAL:
			case LESS_THAN:
			case BEFORE:
			case LESS_THAN_EQUAL:
			case BETWEEN:
			case IN:
				return new String[] { property, join(parameters) };
			case NOT_IN:
			case SIMPLE_PROPERTY:
				return new String[] { property, join(parameters) };
			case WITHIN: 
				return new String[] { property, join(parameters) };
			case NEAR: {
				return new String[] { property, join(parameters) };
			}
			default:
				log.debug("criteria: ", criteria.toString());
				throw new InvalidDataAccessApiUsageException("Illegal criteria found '" + type + "'.");
		}
	}
	
	protected String join(Iterator<?> parameters) {
		List<String> list = Lists.newArrayList();
		while (parameters.hasNext()) {
			Object parameter = parameters.next();
			if (conversionService.canConvert(parameter.getClass(), String.class)) {
				return conversionService.convert(parameter, String.class);
			}
			
			list.add(parameter.toString());
		}
		
		return StringUtils.join(list, ", ");
	}
	
	protected Object[] asArray(Object o) {
		if (o instanceof Collection) {
			return ((Collection<?>) o).toArray();
		} else if (o.getClass().isArray()) {
			return (Object[]) o;
		}
		return new Object[]{o};
	}
}
