package io.github.hooj0.springdata.fabric.chaincode.core.serialize;

import io.github.hooj0.springdata.fabric.chaincode.repository.query.ChaincodeQueryMethod;

/**
 * Chaincode 操作对象 字符串与实体对象之间的编组和解组/序列和反序列化
 * @changelog chaincode operation object grouping and unmarshalling/sequence and deserialization between strings and entity objects
 * @author hoojo
 * @createDate 2018年7月22日 下午1:49:19
 * @file ChaincodeEntitySerialization.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.serialize
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodeEntitySerialization {

	/**
	 * 将对象序列成字符串 
	 * @author hoojo
	 * @createDate 2018年7月22日 下午1:51:14
	 * @param entity T
	 * @return serialize string
	 */
	public <T> String serialize(T entity);
	
	/**
	 * 将字符串反序列成对象
	 * @author hoojo
	 * @createDate 2018年7月22日 下午1:51:47
	 * @param value json string
	 * @param value deserialize target ChaincodeQueryMethod
	 * @return deserialize value
	 */
	public <T> T deserialize(String json, ChaincodeQueryMethod method);
}
