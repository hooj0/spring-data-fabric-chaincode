package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.EntityMetadata;

import io.github.hooj0.springdata.fabric.chaincode.repository.information.RepositoryAannotaitonInformation;

/**
 * 扩展EntityMetadata以添加查询实体实例信息的功能
 * 
 * @author hoojo
 * @createDate 2018年7月17日 下午6:55:01
 * @file ChaincodeEntityInformation.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeEntityInformation<T, ID> extends EntityInformation<T, ID>, EntityMetadata<T> {

	public RepositoryAannotaitonInformation getRepositoryAannotaitonInformation();
}
