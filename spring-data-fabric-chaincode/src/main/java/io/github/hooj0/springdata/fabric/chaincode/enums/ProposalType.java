package io.github.hooj0.springdata.fabric.chaincode.enums;

/**
 * 提议请求类型
 * @author hoojo
 * @createDate 2018年7月18日 上午10:08:53
 * @file ProposalType.java
 * @package io.github.hooj0.springdata.fabric.chaincode.enums
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum ProposalType {

	/** 安装Chaincode提议 */
	INSTALL("安装Chaincode提议"),
	/** 实例化Chaincode提议 */
	INSTANTIATE("实例化Chaincode提议"),
	/** 交易Chaincode提议 */
	INVOKE("交易Chaincode提议"),
	/** 查询Chaincode提议 */
	QUERY("查询Chaincode提议"),
	/** 升级Chaincode提议 */
	UPGRADE("升级Chaincode提议");
	
	private String desc;
	ProposalType(String desc) {
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
}
