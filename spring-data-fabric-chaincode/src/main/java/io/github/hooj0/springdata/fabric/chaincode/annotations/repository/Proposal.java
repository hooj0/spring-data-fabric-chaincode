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
	
	ProposalType type() default ProposalType.INVOKE;
}
