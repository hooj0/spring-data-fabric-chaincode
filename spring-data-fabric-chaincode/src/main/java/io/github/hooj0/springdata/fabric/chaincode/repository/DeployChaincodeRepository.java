package io.github.hooj0.springdata.fabric.chaincode.repository;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.springframework.data.repository.NoRepositoryBean;

import io.github.hooj0.fabric.sdk.commons.core.execution.result.ResultSet;

/**
 * 部署智能合约 Chaincode Repository
 * @author hoojo
 * @createDate 2018年7月19日 上午8:56:34
 * @file DeployChaincodeRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@NoRepositoryBean
public interface DeployChaincodeRepository<T> extends ChaincodeRepository<T> {

	// install
	/**
	 * 安装智能合约 Chaincode 
	 * @author hoojo
	 * @createDate 2018年7月20日 下午3:58:26
	 * @param chaincodeSourceLocation 智能合约源码文件位置
	 */
	Collection<ProposalResponse> install(String chaincodeSourceLocation);

	/**
	 * 安装智能合约 Chaincode 
	 * @author hoojo
	 * @createDate 2018年7月20日 下午3:58:26
	 * @param chaincodeSourceFile 智能合约源码文件
	 */
	Collection<ProposalResponse> install(File chaincodeSourceFile);
	
	/**
	 * 安装智能合约 Chaincode 
	 * @author hoojo
	 * @createDate 2018年7月20日 下午3:59:08
	 * @param chaincodeInputStream 智能合约源码文件Stream
	 */
	Collection<ProposalResponse> install(InputStream chaincodeInputStream);
	
	// instantiate
	
	/**
	 * 实例化智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 */
	ResultSet instantiate(String func);
	
	/**
	 * 实例化智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	ResultSet instantiate(String func, Object... args);

	/**
	 * 实例化智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	ResultSet instantiate(String func, LinkedHashMap<String, Object> args);
	
	// instantiate async
	
	/**
	 * 实例化智能合约 Chaincode 返回异步线程模型
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 */
	CompletableFuture<TransactionEvent> instantiateAsync(String func);
	
	/**
	 * 实例化智能合约 Chaincode 返回异步线程模型
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	CompletableFuture<TransactionEvent> instantiateAsync(String func, Object... args);

	/**
	 * 实例化智能合约 Chaincode 返回异步线程模型
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	CompletableFuture<TransactionEvent> instantiateAsync(String func, LinkedHashMap<String, Object> args);
	
	// instantiate async return event
	
	/**
	 * 实例化智能合约 Chaincode 返回交易事件结果
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 */
	TransactionEvent instantiateFor(String func);
	
	/**
	 * 实例化智能合约 Chaincode 返回交易事件结果
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	TransactionEvent instantiateFor(String func, Object... args);

	/**
	 * 实例化智能合约 Chaincode 返回交易事件结果
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	TransactionEvent instantiateFor(String func, LinkedHashMap<String, Object> args);
	
	
	
	// upgrade
	/**
	 * 升级智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 */
	ResultSet upgrade(String func);
	
	/**
	 * 升级智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	ResultSet upgrade(String func, Object... args);

	/**
	 * 升级智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	ResultSet upgrade(String func, LinkedHashMap<String, Object> args);
	
	// upgrade async
	
	/**
	 * 升级智能合约 Chaincode，返回异步线程模型
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 */
	CompletableFuture<TransactionEvent> upgradeAsync(String func);
	
	/**
	 * 升级智能合约 Chaincode，返回异步线程模型
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	CompletableFuture<TransactionEvent> upgradeAsync(String func, Object... args);

	/**
	 * 升级智能合约 Chaincode，返回异步线程模型
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	CompletableFuture<TransactionEvent> upgradeAsync(String func, LinkedHashMap<String, Object> args);
	
	// upgrade async return event
	/**
	 * 升级智能合约 Chaincode，返回事件结果
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	TransactionEvent upgradeFor(String func);
	
	/**
	 * 升级智能合约 Chaincode，返回事件结果
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	TransactionEvent upgradeFor(String func, Object... args);

	/**
	 * 升级智能合约 Chaincode，返回事件结果
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	TransactionEvent upgradeFor(String func, LinkedHashMap<String, Object> args);
	
}
