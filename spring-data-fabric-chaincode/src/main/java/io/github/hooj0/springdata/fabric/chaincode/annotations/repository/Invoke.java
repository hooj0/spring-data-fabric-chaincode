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
	
	/** 执行Chaincode智能合约的方法名称，默认为当前注解方法的名称 */
	String func() default "";
	
	/** 执行Chaincode智能合约的参数，支持占位符或spel表达式，默认取参数列表  */
	@AliasFor(annotation = Proposal.class, attribute = "args")
	String[] args() default {};
	
	/** HFClient 客户端上下文用户  */
	@AliasFor(annotation = Proposal.class, attribute = "clientUser")
	String clientUser() default "";
	
	/** 当前请求用户  */
	@AliasFor(annotation = Proposal.class, attribute = "requestUser")
	String requestUser() default "";
	
	/** 发送给特定的 peer节点 */
	@AliasFor(annotation = Proposal.class, attribute = "specificPeers")
	boolean specificPeers() default true;
	
	/** 请求提议等待响应事件 */
	@AliasFor(annotation = Proposal.class, attribute = "waitTime")
	long proposalWaitTime() default 0;
	
	/** 交易等待时间 */
	@AliasFor(annotation = Transaction.class, attribute = "waitTime")
	long transactionWaitTime() default 0;
	/** 发起交易的用户 */
	@AliasFor(annotation = Transaction.class, attribute = "user")
	String transactionsUser() default "";
}
