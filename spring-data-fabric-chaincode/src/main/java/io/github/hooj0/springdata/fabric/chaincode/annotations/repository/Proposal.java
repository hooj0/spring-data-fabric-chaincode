package io.github.hooj0.springdata.fabric.chaincode.annotations.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.QueryAnnotation;

import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;

/**
 * 智能合约 请求提议，在所有交易动作之前的发出的请求
 * @changelog Smart contract request proposal, issued request before all trading actions
 * @author hoojo
 * @createDate 2018年7月18日 上午9:54:19
 * @file Proposal.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Documented
@QueryAnnotation
public @interface Proposal {

	String value() default "";
	
	/** 执行Chaincode智能合约的参数，支持占位符或spel表达式，默认取参数列表  */
	String[] args() default {};
	
	ProposalType type() default ProposalType.INVOKE;
	
	/** HFClient 客户端上下文用户  */
	String clientUser() default "";
	
	/** 当前请求用户  */
	String requestUser() default "";
	
	/** 发送给特定的 peer节点 */
	boolean specificPeers() default false;
	
	/** 请求提议等待响应事件 */
	long waitTime() default 0;
}
