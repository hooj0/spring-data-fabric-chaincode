package io.github.hooj0.springdata.fabric.chaincode.core.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;

import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.lang.Nullable;

/**
 * 持久化实体对象属性映射与转换
 * @author hoojo
 * @createDate 2018年7月17日 上午11:05:31
 * @file ChaincodePersistentProperty.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.mapping
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodePersistentProperty extends PersistentProperty<ChaincodePersistentProperty>, ApplicationContextAware {

	String getFieldName();
	
	boolean isTransientProperty();
	
	String getTransientKey();
	
	/**
	 * 通过从属性类型派生的{@code annotationType}查找{@link AnnotatedType}。 
	 * 内省属性字段/访问器可以查找带注释的类型。 
	 * 类型参数中的类型注释会对集合/类似地图类型进行内省。
	 *
	 * @param annotationType must not be {@literal null}.
	 * @return the annotated type or {@literal null}.
	 * @since 2.0
	 */
	@Nullable
	AnnotatedType findAnnotatedType(Class<? extends Annotation> annotationType);
	
	public enum PropertyToFieldNameConverter implements Converter<ChaincodePersistentProperty, String> {
		INSTANCE;

		public String convert(ChaincodePersistentProperty source) {
			return source.getFieldName();
		}
	}
}
