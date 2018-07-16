package io.github.hooj0.springdata.fabric.chaincode.annotations;

import io.github.hooj0.springdata.fabric.chaincode.enums.Mode;

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
@Deploy(mode = Mode.INSTANTIATE)
public @interface Instantiate {

}
