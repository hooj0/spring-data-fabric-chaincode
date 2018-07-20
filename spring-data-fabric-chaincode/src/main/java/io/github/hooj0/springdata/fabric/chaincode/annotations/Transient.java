package io.github.hooj0.springdata.fabric.chaincode.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * <b>function:</b> Transient Data Field Annotation
 * @author hoojo
 * @createDate 2018年7月17日 下午2:06:34
 * @file Transient.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Field(transientField = true)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Documented
public @interface Transient {

	/** Chaincode Transient Map Key 为 Chaincode TransientMap Key取名 */
	@AliasFor(annotation = Field.class, attribute = "transientAlias")
	String alias() default "";
}
