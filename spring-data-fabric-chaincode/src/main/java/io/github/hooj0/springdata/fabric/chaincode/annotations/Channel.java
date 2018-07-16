package io.github.hooj0.springdata.fabric.chaincode.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>function:</b> 操作合约的通道数据信息
 * @author hoojo
 * @createDate 2018年7月16日 下午5:17:16
 * @file Channel.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.PACKAGE, ElementType.METHOD })
public @interface Channel {

	/** 通道名称  */
	String name() default "";
}
