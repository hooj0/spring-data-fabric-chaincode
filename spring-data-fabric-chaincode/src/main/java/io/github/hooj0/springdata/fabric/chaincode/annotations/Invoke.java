package io.github.hooj0.springdata.fabric.chaincode.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月16日 下午5:15:09
 * @file Invoke.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface Invoke {
	
	/** 执行Chaincode智能合约的方法名称 */
	String func() default "";
	
	/** 执行Chaincode智能合约的参数名称，支持表达式 */
	String[] args() default "";
}
