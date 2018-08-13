package io.github.hooj0.springdata.fabric.chaincode.core.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
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
 * chaincode simple persistent entity property mapping support
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
	private String mappingName;
	
	static {
		SUPPORTED_ID_TYPES.add(String.class);
		SUPPORTED_ID_TYPES.add(byte[].class);

		SUPPORTED_ID_PROPERTY_NAMES.add("hash");
		SUPPORTED_ID_PROPERTY_NAMES.add("hashId");
		SUPPORTED_ID_PROPERTY_NAMES.add("txId");
		SUPPORTED_ID_PROPERTY_NAMES.add("transactionId");
	}
	
	public SimpleChaincodePersistentProperty(Property property, PersistentEntity<?, ChaincodePersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(property, owner, simpleTypeHolder);
		log.debug("Added property: {}, filed: {}", property.getName(), property.getField().get());
		
		// isAnnotatedTransient(property);
		isAnnotationTransient(property);
		
		if (isTransientProperty) {
			log.debug("isTransientProperty: {}, transientKey: {}", isTransientProperty, transientKey);
		}
		if (isFieldProperty) {
			log.debug("isFieldProperty: {}, mappingName: {}", isTransientProperty, mappingName);
		}
	}
	
	@SuppressWarnings("unused")
	private void isAnnotatedTransient(Property property) {
		if (isAnnotationPresent(Field.class)) {
			Field field = findAnnotation(Field.class);
			
			isFieldProperty = field != null;
			
			if (isFieldProperty && field.transientField()) {
				isTransientProperty = true;
				transientKey = StringUtils.defaultIfBlank(field.transientAlias(), getFieldName());
			} else {
				isTransientProperty = false;
			}
		} else if (isAnnotationPresent(Transient.class)) {
			
			Transient transientField = findAnnotation(Transient.class);
			if (transientField != null) {
				isTransientProperty = true;
				transientKey = StringUtils.defaultIfBlank(transientField.alias(), getFieldName());
			} else {
				isTransientProperty = false;
			}
		}
	}
	
	private void isAnnotationTransient(Property property) {
		if (isAnnotationPresent(Field.class) || isAnnotationPresent(Transient.class)) {
			
			Field field = AnnotatedElementUtils.findMergedAnnotation(property.getField().get(), Field.class);
			
			isFieldProperty = field != null;
			
			if (isFieldProperty) {
				mappingName = StringUtils.defaultIfBlank(field.mapping(), getFieldName());
			}
			
			if (isFieldProperty && field.transientField()) {
				isTransientProperty = true;
				transientKey = StringUtils.defaultIfBlank(field.transientAlias(), getFieldName());
			} else {
				isTransientProperty = false;
			}
		}
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

	@Override
	public String getMappingName() {
		return mappingName;
	}
}
