package io.github.hooj0.springdata.fabric.chaincode.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.hooj0.springdata.fabric.chaincode.enums.Mode;

/**
 * <b>function:</b> 部署合约chaincode，包括安装、实例化、升级
 * @author hoojo
 * @createDate 2018年7月16日 下午5:15:48
 * @file Deploy.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.PACKAGE, ElementType.METHOD })
@Documented
public @interface Deploy {

	/** 部署模式 */
	Mode mode() default Mode.INSTALL;
}
