package io.github.hooj0.springdata.fabric.chaincode.annotations;

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
public @interface Transient {

	/** Chaincode Transient Map Key 为 Chaincode TransientMap Key取名 */
	@AliasFor(annotation = Field.class, attribute = "transientAlias")
	String alias() default "";
}
