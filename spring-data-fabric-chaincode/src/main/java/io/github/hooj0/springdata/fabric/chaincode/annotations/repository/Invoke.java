package io.github.hooj0.springdata.fabric.chaincode.annotations.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.QueryAnnotation;

import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;

/**
 * 交易类型智能合约请求
 * @author hoojo
 * @createDate 2018年7月16日 下午5:15:09
 * @file Invoke.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Proposal(type = ProposalType.INVOKE)
@QueryAnnotation
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Invoke {
	
	@AliasFor(annotation = Proposal.class)
	String value() default "";
	
	/** 执行Chaincode智能合约的方法名称 */
	String func() default "";
	
	/** 执行Chaincode智能合约的参数名称，支持表达式 */
	String[] args() default "";
}
