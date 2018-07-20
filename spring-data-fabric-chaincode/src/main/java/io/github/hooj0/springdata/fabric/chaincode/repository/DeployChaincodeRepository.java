package io.github.hooj0.springdata.fabric.chaincode.repository;

import java.io.File;
import java.io.InputStream;

import org.springframework.data.repository.NoRepositoryBean;

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

	/**
	 * 安装智能合约 Chaincode 
	 * @author hoojo
	 * @createDate 2018年7月20日 下午3:58:26
	 * @param chaincodeSourceFile 智能合约源码文件
	 */
	public void install(File chaincodeSourceFile);
	
	/**
	 * 安装智能合约 Chaincode 
	 * @author hoojo
	 * @createDate 2018年7月20日 下午3:59:08
	 * @param chaincodeInputStream 智能合约源码文件Stream
	 */
	public void install(InputStream chaincodeInputStream);

	/**
	 * 实例化智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午3:57:47
	 * @param policyAsBytes 背书策略配置文件 bytes
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	public String instantiate(byte[] policyAsBytes, String func, String... args);
	
	/**
	 * 实例化智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param policyFile 背书策略配置文件
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	public String instantiate(File policyFile, String func, String... args);
	
	/**
	 * 实例化智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:05:22
	 * @param policyInputStream 背书策略配置文件输入流
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	public String instantiate(InputStream policyInputStream, String func, String... args);
	
	/**
	 * 升级智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午3:57:47
	 * @param policyAsBytes 背书策略配置文件 bytes
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	public String upgrade(byte[] policyAsBytes, String version, String func, String... args);
	
	/**
	 * 升级智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:03:40
	 * @param policyFile 背书策略配置文件
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	public String upgrade(File policyFile, String version, String func, String... args);
	
	/**
	 * 升级智能合约 Chaincode
	 * @author hoojo
	 * @createDate 2018年7月20日 下午4:05:22
	 * @param policyInputStream 背书策略配置文件输入流
	 * @param func 智能合约Chaincode初始化方法
	 * @param args 智能合约Chaincode初始化方法参数
	 */
	public String upgrade(InputStream policyInputStream, String version, String func, String... args);
	
}
