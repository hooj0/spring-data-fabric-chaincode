package io.github.hooj0.springdata.fabric.chaincode.enums;

/**
 * chaincode repository interface input output Serialized interface mode
 * 
 * @author hoojo
 * @createDate 2018年8月1日 下午3:33:02
 * @file SerializationMode.java
 * @package io.github.hooj0.springdata.fabric.chaincode.enums
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum SerializationMode {

	ALL("全部，参数序列化和返回值反序列化"), 
	SERIALIZE("序列化，将参数对象序列化成字符串"), 
	DESERIALIZE("反序列化，将返回值字符串序列化成对象");
	
	private String desc;
	SerializationMode(String desc) {
		this.desc = desc;
	}
	public String getDesc() {
		return desc;
	}
}
