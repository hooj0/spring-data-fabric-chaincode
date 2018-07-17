package io.github.hooj0.springdata.fabric.chaincode.core;

import org.hyperledger.fabric.sdk.HFClient;

import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;

/**
 * <b>function:</b> Chaincode Operations
 * @author hoojo
 * @createDate 2018年7月17日 上午10:28:28
 * @file ChaincodeOperations.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeOperations {

	public HFClient getClient();
	
	public ChaincodeConverter getConverter();
	
	public void install();
}
