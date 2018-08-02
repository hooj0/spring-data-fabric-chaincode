package io.github.hooj0.springdata.fabric.chaincode.core.serialize;

import com.google.gson.Gson;

/**
 * gson chaincode entity serialization support
 * @author hoojo
 * @createDate 2018年8月1日 下午2:35:16
 * @file GsonChaincodeEntitySerialization.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.serialize
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum GsonChaincodeEntitySerialization implements ChaincodeEntitySerialization {

	INSTANCE;
	
	private static final Gson gson = new Gson();
	
	@Override
	public <T> String serialize(T entity) {
		return gson.toJson(entity);
	}

	@Override
	public <T> T deserialize(Class<T> clazz, String json) {
		return gson.fromJson(json, clazz);
	}
}
