package io.github.hooj0.springdata.fabric.chaincode.core.mapping;

import java.util.Collections;
import java.util.Set;

import org.springframework.data.mapping.model.SimpleTypeHolder;

import com.google.common.collect.Sets;

/**
 * 容纳一组类型的简单容器被认为是简单类型。
 */
public class ChaincodeSimpleTypeHolder extends SimpleTypeHolder {

	private static final Set<Class<?>> SOLR_SIMPLE_TYPES;

	private ChaincodeSimpleTypeHolder() {
		// hide utility class constructor
	}

	static {
		Set<Class<?>> simpleTypes = Sets.newHashSet();
		simpleTypes.add(String.class);

		SOLR_SIMPLE_TYPES = Collections.unmodifiableSet(simpleTypes);
	}
	
	public static final SimpleTypeHolder HOLDER = new SimpleTypeHolder(SOLR_SIMPLE_TYPES, true);
}
