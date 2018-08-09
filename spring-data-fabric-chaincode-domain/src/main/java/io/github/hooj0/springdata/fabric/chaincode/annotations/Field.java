package io.github.hooj0.springdata.fabric.chaincode.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>function:</b> Chaincode TransientMap Data Annotation
 * @author hoojo
 * @createDate 2018年7月17日 下午2:01:17
 * @file Field.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Documented
@Inherited
public @interface Field {

	/** 映射到 Chaincode的字段名 */
	String mapping() default "";
	
	/** 是否为 TransientMap 属性 */
	boolean transientField() default true;
	
	/**  TransientMap 属性 别名，不取别名就用实体对象属性名称 */
	String transientAlias() default "";
}
