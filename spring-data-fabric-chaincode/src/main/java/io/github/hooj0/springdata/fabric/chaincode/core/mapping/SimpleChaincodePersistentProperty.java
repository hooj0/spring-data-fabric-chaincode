package io.github.hooj0.springdata.fabric.chaincode.core.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.Property;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.annotations.Field;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Transient;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单 Property 信息实现 
 * @author hoojo
 * @createDate 2018年7月17日 上午11:13:20
 * @file SimpleChaincodePersistentProperty.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.mapping
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class SimpleChaincodePersistentProperty extends AnnotationBasedPersistentProperty<ChaincodePersistentProperty> implements ChaincodePersistentProperty {

	private static final Set<Class<?>> SUPPORTED_ID_TYPES = new HashSet<>(3);
	private static final Set<String> SUPPORTED_ID_PROPERTY_NAMES = new HashSet<>(1);
	
	private ApplicationContext context;

	private boolean isFieldProperty;
	private boolean isTransientProperty;
	private String transientKey;
	
	static {
		SUPPORTED_ID_TYPES.add(String.class);
		SUPPORTED_ID_TYPES.add(Long.class);
		SUPPORTED_ID_TYPES.add(Integer.class);

		SUPPORTED_ID_PROPERTY_NAMES.add("id");
	}
	
	public SimpleChaincodePersistentProperty(Property property, PersistentEntity<?, ChaincodePersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(property, owner, simpleTypeHolder);
		log.debug("Added property: {}, filed: {}", property.getName(), property.getField());
		
		if (isAnnotationPresent(Field.class)) {
			Field field = findAnnotation(Field.class);
			
			if (field != null) {
				isFieldProperty = true;
			}
			
			if (isFieldProperty && field.transientField()) {
				isTransientProperty = true;
				transientKey = StringUtils.defaultString(field.transientAlias(), getFieldName());
			} else {
				isTransientProperty = false;
			}
		} else if (isAnnotationPresent(Transient.class)) {
			
			Transient transientField = findAnnotation(Transient.class);
			if (transientField != null) {
				isTransientProperty = true;
				transientKey = StringUtils.defaultString(transientField.alias(), getFieldName());
			} else {
				isTransientProperty = false;
			}
		}
		
		/*
		Field field = AnnotatedElementUtils.findMergedAnnotation(property.getType(), Field.class);
		if (field != null) {
			isFieldProperty = true;
		}
		
		if (isFieldProperty && field.transientField()) {
			isTransientProperty = true;
			transientKey = field.transientAlias();
		} else {
			isTransientProperty = false;
		}
		*/
		
		log.debug("isTransientProperty: {}, transientKey: {}", isTransientProperty, transientKey);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	@Override
	public String getFieldName() {
		return getProperty().getName();
	}
	
	@Override
	public boolean isIdProperty() {
		return super.isIdProperty() || SUPPORTED_ID_PROPERTY_NAMES.contains(getFieldName());
	}
	
	@Override
	protected Association<ChaincodePersistentProperty> createAssociation() {
		Assert.isTrue(context != null, "contxt not null");
		
		return new Association<ChaincodePersistentProperty>(this, null);
	}

	@Override
	public AnnotatedType findAnnotatedType(Class<? extends Annotation> annotationType) {
		return (AnnotatedType) super.findAnnotation(annotationType);
	}

	@Override
	public boolean isTransientProperty() {
		return isTransientProperty;
	}

	@Override
	public String getTransientKey() {
		return transientKey;
	}

	@Override
	public boolean isFieldProperty() {
		return isFieldProperty;
	}
}
