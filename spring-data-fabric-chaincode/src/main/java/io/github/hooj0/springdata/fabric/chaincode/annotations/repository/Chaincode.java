package io.github.hooj0.springdata.fabric.chaincode.annotations.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.springframework.core.annotation.AliasFor;

/**
 * chaincode repository annotation
 * @author hoojo
 * @createDate 2018年7月16日 下午5:01:01
 * @file Chaincode.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.PACKAGE, ElementType.METHOD })
@Documented
@Channel(name = "", org = "")
public @interface Chaincode {

	/** chaincode 通道名称 */
	@AliasFor(annotation = Channel.class, attribute = "name")
	String channel() default "";
	
	/** 智能合约所在 认证组织 */
	@AliasFor(annotation = Channel.class, attribute = "org")
	String org() default "";
	
	/** chaincode 合约名称 */
	String name();
	
	/** chaincode 合约版本 */
	String version() default "1.0";
	
	/** chaincode 合约类型 */
	Type type() default Type.GO_LANG;
	
	/** chaincode 合约所在路径 */
	String path() default "";
}
