package io.github.hooj0.springdata.fabric.chaincode.annotations.repository;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.annotation.QueryAnnotation;

import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;

/**
 * 查询 智能合约 请求
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
public @interface Query {

	@AliasFor(annotation = Proposal.class)
	String value() default "";
}
