package io.github.hooj0.springdata.fabric.chaincode.enums;

/**
 * <b>function:</b> 部署合约 Chaincode的方式
 * @author hoojo
 * @createDate 2018年7月16日 下午5:44:03
 * @file DeployMode.java
 * @package io.github.hooj0.springdata.fabric.chaincode.enums
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum DeployMode {

	/** 安装合约模式 */
	INSTALL,
	/** 实例化合约模式 */
	INSTANTIATE,
	/** 升级合约模式 */
	UPGRADE;
}
