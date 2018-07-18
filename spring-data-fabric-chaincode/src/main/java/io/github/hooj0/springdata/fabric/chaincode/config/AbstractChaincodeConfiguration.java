package io.github.hooj0.springdata.fabric.chaincode.config;

import java.util.Set;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.lang.Nullable;

import com.google.common.collect.Table;

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
public abstract class AbstractChaincodeConfiguration implements BeanClassLoaderAware {

	private @Nullable ClassLoader beanClassLoader;
	
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
