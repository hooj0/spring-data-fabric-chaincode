package io.github.hooj0.springdata.fabric.chaincode.repository;

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

	public String invoke(String func, String... args);
	
	public String query(String func, String... args);
	
	public Class<T> getEntityClass();
}
