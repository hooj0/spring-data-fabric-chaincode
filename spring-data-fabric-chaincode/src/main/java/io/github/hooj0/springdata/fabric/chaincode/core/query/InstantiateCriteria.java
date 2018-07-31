package io.github.hooj0.springdata.fabric.chaincode.core.query;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.InstantiateOptions;
import lombok.Getter;
import lombok.ToString;

/**
 * chaincode deploy repository instantiate criteria
 * @author hoojo
 * @createDate 2018年7月30日 下午4:11:59
 * @file InstantiateCriteria.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@ToString
@Getter
public final class InstantiateCriteria extends InstantiateOptions {

	private Criteria criteria;

	public InstantiateCriteria(Criteria criteria) {
		super();
		this.criteria = criteria;
		
		this.setChaincodeId(this.criteria.getChaincodeID());
		this.setChaincodeType(this.criteria.getType());
	}
}
