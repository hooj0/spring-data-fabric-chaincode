package io.github.hooj0.springdata.fabric.chaincode.enums;

import io.github.hooj0.springdata.fabric.chaincode.core.serialize.ChaincodeEntitySerialization;
import io.github.hooj0.springdata.fabric.chaincode.core.serialize.GsonChaincodeEntitySerialization;

/**
 * chaincode repository interface input output serialization provider
 * @author hoojo
 * @createDate 2018年8月1日 下午3:38:18
 * @file SerializationProvider.java
 * @package io.github.hooj0.springdata.fabric.chaincode.enums
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum SerializationProvider {

	GSON("Gson 序列化实现"), 
	JACKSON("JACKSON 序列化实现"), 
	PROTOBUF("PROTOBUF 序列化实现"), 
	XML("XML 序列化实现");
	
	private String desc;
	SerializationProvider(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public ChaincodeEntitySerialization getSerialization() {
		if (this == GSON) {
			return GsonChaincodeEntitySerialization.INSTANCE;
		}

		return GsonChaincodeEntitySerialization.INSTANCE;
	}
}
