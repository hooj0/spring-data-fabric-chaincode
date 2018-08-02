package io.github.hooj0.springdata.fabric.chaincode.annotations.repository;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.hooj0.springdata.fabric.chaincode.enums.SerializationProvider;
import io.github.hooj0.springdata.fabric.chaincode.enums.SerializationMode;

/**
 * chaincode repository interface input output serialization
 * @author hoojo
 * @createDate 2018年8月1日 下午3:30:44
 * @file Serialization.java
 * @package io.github.hooj0.springdata.fabric.chaincode.annotations.repository
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Serialization {

	SerializationMode value() default SerializationMode.ALL;
	
	SerializationProvider provider() default SerializationProvider.GSON;
}
