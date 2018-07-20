package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import io.github.hooj0.springdata.fabric.chaincode.repository.information.RepositoryAannotaitonInformation;

/**
 * ChaincodeEntityInformation 对象创造者接口
 * @author hoojo
 * @createDate 2018年7月17日 下午6:51:28
 * @file ChaincodeEntityInformationCreator.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeEntityInformationCreator {

	<T, ID> ChaincodeEntityInformation<T, ID> getEntityInformation(Class<T> domainClass);
	
	RepositoryAannotaitonInformation getRepositoryAannotaitonInformation();
}
