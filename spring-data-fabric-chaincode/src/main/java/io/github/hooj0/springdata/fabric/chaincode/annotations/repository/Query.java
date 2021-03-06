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
 * chaincode query proposal annotation
 * @author hoojo
 * @createDate 2018年7月16日 下午5:15:21
 * @file Query.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Proposal(type = ProposalType.QUERY)
@QueryAnnotation
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Query {

	@AliasFor(annotation = Proposal.class)
	String value() default "";
	
	/** 执行Chaincode智能合约的方法名称，默认为当前注解方法的名称 */
	@AliasFor(annotation = Proposal.class, attribute = "func")
	String func() default "";
	
	/** 执行Chaincode智能合约的参数，支持占位符或spel表达式，默认取参数列表  */
	@AliasFor(annotation = Proposal.class, attribute = "args")
	String[] args() default {};
	
	/** HFClient 客户端上下文用户  */
	@AliasFor(annotation = Proposal.class, attribute = "clientUser")
	String clientUser() default "";
}
