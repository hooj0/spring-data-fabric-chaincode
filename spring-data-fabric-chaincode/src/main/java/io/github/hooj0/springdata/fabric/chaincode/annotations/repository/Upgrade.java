package io.github.hooj0.springdata.fabric.chaincode.annotations.repository;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.QueryAnnotation;

import io.github.hooj0.springdata.fabric.chaincode.enums.DeployMode;
import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;

/**
 * <b>function:</b> 升级 智能合约 Chaincode
 * @author hoojo
 * @createDate 2018年7月16日 下午5:17:39
 * @file Upgrade.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Deploy(mode = DeployMode.UPGRADE)
@Proposal(type = ProposalType.UPGRADE)
@QueryAnnotation
public @interface Upgrade {

	@AliasFor(annotation = Proposal.class)
	String value() default "";
	
	/** 部署升级的版本号 */
	String version() default "";
}
