package io.github.hooj0.springdata.fabric.chaincode.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.github.hooj0.springdata.fabric.chaincode.annotations.Entity;

/**
 * <b>function:</b>
 * @author hoojo
 * @createDate 2018年7月17日 下午5:18:44
 * @file BaseEntity.java
 * @package io.github.hooj0.springdata.fabric.chaincode.entity
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Entity
public class BaseEntity {

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
