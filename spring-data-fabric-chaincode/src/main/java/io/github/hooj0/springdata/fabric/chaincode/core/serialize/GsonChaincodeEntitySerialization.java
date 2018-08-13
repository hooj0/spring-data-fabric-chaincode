package io.github.hooj0.springdata.fabric.chaincode.core.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.hooj0.springdata.fabric.chaincode.ChaincodeUnsupportedOperationException;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.ChaincodeQueryMethod;

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
	
	private static final Gson gson;
	
	static {
		GsonBuilder builder = new GsonBuilder();
		// 2018-08-13T16:09:54.1769762+08:00
		builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").serializeNulls().disableHtmlEscaping();
		
		gson = builder.create();
	}
	
	@Override
	public <T> String serialize(T entity) {
		return gson.toJson(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(String json, ChaincodeQueryMethod method) {

		if (method.isCollectionQuery()) {
	        throw new ChaincodeUnsupportedOperationException("Gson Provider not support collection '%s' deserialize.", method.getReturnType().getRawTypeInformation().getType());
		} 
		
		return (T) gson.fromJson(json, method.getResultType());
	}
}
