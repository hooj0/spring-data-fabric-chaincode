package io.github.hooj0.springdata.fabric.chaincode.core.query;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.QueryOptions;
import lombok.Getter;
import lombok.ToString;

/**
 * chaincode repository query transaction criteria
 * @author hoojo
 * @createDate 2018年7月30日 下午2:24:30
 * @file QueryCriteria.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@ToString
@Getter
public final class QueryCriteria extends QueryOptions {
	
	private Criteria criteria;
	
	public QueryCriteria(Criteria criteria) {
		this.criteria = criteria;
		this.setChaincodeId(this.criteria.getChaincodeID());
	}
	
	public QueryCriteria setCriteria(Criteria criteria) {
		this.criteria = criteria;
		this.setChaincodeId(this.criteria.getChaincodeID());
		return this;
	}
}
