package io.github.hooj0.springdata.fabric.chaincode.annotations.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.QueryAnnotation;

import io.github.hooj0.springdata.fabric.chaincode.enums.DeployMode;
import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;

/**
 * <b>function:</b> 实例化智能合约 Chaincode
 * @author hoojo
 * @createDate 2018年7月16日 下午5:16:59
 * @file Instantiate.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Deploy(mode = DeployMode.INSTANTIATE)
@Proposal(type = ProposalType.INSTANTIATE)
@QueryAnnotation
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Instantiate {

	@AliasFor(annotation = Proposal.class)
	String value() default "";
}
