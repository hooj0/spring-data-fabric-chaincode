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
 * 安装智能合约 Chaincode
 * @author hoojo
 * @createDate 2018年7月16日 下午5:16:01
 * @file Install.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Deploy(mode = ProposalType.INSTALL)
@QueryAnnotation
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Install {

	/** Chaincode 源码目录位置 */
	String chaincodeLocation();

	@AliasFor(annotation = Proposal.class)
	String value() default "";
	
	/** HFClient 客户端上下文用户  */
	@AliasFor(annotation = Proposal.class, attribute = "clientUser")
	String clientUser() default "";
	
	/** 当前请求用户  */
	@AliasFor(annotation = Proposal.class, attribute = "requestUser")
	String requestUser() default "";
	
	/** 发送给特定的 peer节点 */
	@AliasFor(annotation = Proposal.class, attribute = "specificPeers")
	boolean specificPeers() default false;
	
	/** 请求提议等待响应事件 */
	@AliasFor(annotation = Proposal.class, attribute = "waitTime")
	long waitTime() default 0;
	
	/** 安装升级的版本，在升级版本时使用 */
	@AliasFor(annotation = Proposal.class, attribute = "version")
	String version() default "";
}
