package io.github.hooj0.springdata.fabric.chaincode.core.query;

import io.github.hooj0.fabric.sdk.commons.core.execution.option.UpgradeOptions;
import lombok.Getter;
import lombok.ToString;

/**
 * chaincode deploy repository upgrade criteria
 * @author hoojo
 * @createDate 2018年7月30日 下午4:10:27
 * @file UpgradeCriteria.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@ToString
@Getter
public final class UpgradeCriteria extends UpgradeOptions {

	private Criteria criteria;

	public UpgradeCriteria(Criteria criteria) {
		super();
		this.criteria = criteria;
		
		this.setChaincodeId(this.criteria.getChaincodeID());
		this.setChaincodeType(this.criteria.getType());
	}
}
