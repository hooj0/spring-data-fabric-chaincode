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
 * Deploy contract chaincode, including installation, instantiation, and upgrade
 * @author hoojo
 * @createDate 2018年7月16日 下午5:15:48
 * @file Deploy.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Proposal(type = ProposalType.INSTALL)
@QueryAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.PACKAGE, ElementType.METHOD })
@Documented
public @interface Deploy {

	/** 部署模式 */
	@AliasFor(annotation = Proposal.class, attribute = "type")
	ProposalType mode() default ProposalType.INSTALL;
}
