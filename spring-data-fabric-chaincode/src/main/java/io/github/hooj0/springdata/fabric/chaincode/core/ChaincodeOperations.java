package io.github.hooj0.springdata.fabric.chaincode.core;

import java.util.LinkedHashMap;

import io.github.hooj0.fabric.sdk.commons.core.ChaincodeDeployOperations;
import io.github.hooj0.fabric.sdk.commons.core.ChaincodeTransactionOperations;
import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;

/**
 * chaincode operations `install & invoke & instantiate & query & upgrade` interface
 * 
 * @author hoojo
 * @createDate 2018年7月17日 上午10:28:28
 * @file ChaincodeOperations.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeOperations/* extends ChaincodeDeployOperations, ChaincodeTransactionOperations */ {

	ChaincodeConverter getConverter();
	
	ChaincodeDeployOperations getChaincodeDeployOperations(Criteria criteria);

	ChaincodeTransactionOperations getChaincodeTransactionOperations(Criteria criteria);

	// invoke

	ResultSet invoke(Criteria criteria, String func);

	ResultSet invoke(Criteria criteria, String func, Object... args);

	ResultSet invoke(Criteria criteria, String func, LinkedHashMap<String, Object> args);
}
