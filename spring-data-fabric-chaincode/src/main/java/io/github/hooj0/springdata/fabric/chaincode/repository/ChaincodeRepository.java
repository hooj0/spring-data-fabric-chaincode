package io.github.hooj0.springdata.fabric.chaincode.repository;

import java.io.File;
import java.io.InputStream;

import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * <b>function:</b> chaincode 智能合约 repo
 * @author hoojo
 * @createDate 2018年7月17日 上午9:34:18
 * @file ChaincodeRepository.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@NoRepositoryBean
public interface ChaincodeRepository<T> extends Repository<T, Object> {

	public void install(File chaincodeFile);
	
	public void install(InputStream chaincodeFile);
	
	public void instantiate(String policyFilePath);
	
	public void instantiate(File policyFile);
	
	public void instantiate(ChaincodeEndorsementPolicy endorsementPolicy);
	
	public void upgrade();
	
	public String invoke(String func, String... args);
	
	public String query(String func, String... args);
}
