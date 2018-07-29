package io.github.hooj0.springdata.fabric.chaincode.annotations.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作合约的通道数据信息
 * @author hoojo
 * @createDate 2018年7月16日 下午5:17:16
 * @file Channel.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.PACKAGE , ElementType.METHOD })
@Inherited
@Documented
public @interface Channel {

	/** 智能合约运行 通道名称  */
	String name();
	
	/** 智能合约所在 认证组织 */
	String org();
}
