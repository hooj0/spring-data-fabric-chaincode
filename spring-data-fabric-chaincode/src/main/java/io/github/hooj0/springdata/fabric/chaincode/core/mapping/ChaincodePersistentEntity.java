package io.github.hooj0.springdata.fabric.chaincode.core.mapping;

import java.util.Map;

import org.springframework.data.mapping.PersistentEntity;

/**
 * 持久化对象实体信息接口
 * @author hoojo
 * @createDate 2018年7月17日 上午11:08:55
 * @file ChaincodePersistentEntity.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.mapping
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public interface ChaincodePersistentEntity<T> extends PersistentEntity<T, ChaincodePersistentProperty> {

	Map<String, ChaincodePersistentProperty> getTransientProperties();
	
	Map<String, String> getTransientMappings();
	
	Map<String, String> getFieldMappings();
}
