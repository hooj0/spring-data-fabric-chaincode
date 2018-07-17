package io.github.hooj0.springdata.fabric.chaincode.config;

import java.util.Set;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;

import com.google.common.collect.Table;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeTemplate;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.SimpleChaincodeMappingContext;

/**
 * <b>function:</b> 智能合约 抽象基础配置
 * @author hoojo
 * @createDate 2018年7月16日 下午4:55:51
 * @file AbstractChaincodeConfiguration.java
 * @package io.github.hooj0.springdata.fabric.chaincode.config
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class AbstractChaincodeConfiguration implements BeanClassLoaderAware {

	private @Nullable ClassLoader beanClassLoader;
	
	@Bean
	public SimpleChaincodeMappingContext mappingContext() throws ClassNotFoundException {
		SimpleChaincodeMappingContext mappingContext = new SimpleChaincodeMappingContext();
		mappingContext.setInitialEntitySet(getInitialEntitySet());

		return mappingContext;
	}
	
	@Bean
	public MappingChaincodeConverter mappingConverter() throws ClassNotFoundException {
		
		MappingChaincodeConverter converter = new MappingChaincodeConverter(mappingContext());
		return converter;
	}
	
	@Bean
	public ChaincodeTemplate chaincodeTemplate() {
		return new ChaincodeTemplate();
	}
	
	@Bean
	public ChaincodeOperations chaincodeOperations() throws ClassNotFoundException {
		
		return null;
		//return this.chaincodeTemplate();
	}
	
	/**
	 *  基本包以扫描使用{@link Table}注释注释的实体。
	 *  默认情况下，返回包名称{@literal this} {@ code this.getClass().getPackage().getName()}。
	 *  此方法必须永远不会返回{@literal null}。
	 */
	public String[] getEntityBasePackages() {
		return new String[] { getClass().getPackage().getName() };
	}
	
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	/**
	 * 返回初始实体类的{@link Set}。 使用{@link #getEntityBasePackages() }默认扫描类路径。 
	 * 可以由子类覆盖以跳过类路径扫描并返回一组固定的实体类。
	 */
	protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
		return ChaincodeEntityClassScanner.scan(getEntityBasePackages());
	}
}
