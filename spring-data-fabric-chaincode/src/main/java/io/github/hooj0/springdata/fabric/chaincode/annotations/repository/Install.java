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
 * Chaincode install proposal annotation
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

	@AliasFor(annotation = Proposal.class)
	String value() default "";
	
	/** HFClient 客户端上下文用户  */
	@AliasFor(annotation = Proposal.class, attribute = "clientUser")
	String clientUser() default "";
	
	/** Chaincode 源码目录位置 */
	String chaincodeLocation();
	
	/** 安装升级的版本，在升级版本时使用 */
	String version() default "";
	
	/** Chaincode META-INF 目录的配置 */
	String metaINF() default "";
}
