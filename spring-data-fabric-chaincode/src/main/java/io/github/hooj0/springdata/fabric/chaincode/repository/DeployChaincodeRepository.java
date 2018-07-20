package io.github.hooj0.springdata.fabric.chaincode.repository;

import java.io.File;
import java.io.InputStream;

import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
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

	public void install(File chaincodeFile);
	
	public void install(InputStream chaincodeFile);
	
	public void instantiate(String policyFilePath);
	
	public void instantiate(File policyFile);
	
	public void instantiate(ChaincodeEndorsementPolicy endorsementPolicy);
	
	public void upgrade();
	
}
