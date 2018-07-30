package io.github.hooj0.springdata.fabric.chaincode.core.query;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.InvokeOptions;
import lombok.Getter;
import lombok.ToString;

/**
 * chaincode repository invoke transaction criteria
 * @author hoojo
 * @createDate 2018年7月30日 下午3:10:34
 * @file InvokeCriteria.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@ToString
@Getter
public final class InvokeCriteria extends InvokeOptions {

	private Criteria criteria;
	
	public InvokeCriteria(Criteria criteria) {
		super();
		this.criteria = criteria;
		this.setChaincodeId(this.criteria.getChaincodeID());
	}
	
	public InvokeCriteria setCriteria(Criteria criteria) {
		this.criteria = criteria;
		this.setChaincodeId(this.criteria.getChaincodeID());
		return this;
	}
}
