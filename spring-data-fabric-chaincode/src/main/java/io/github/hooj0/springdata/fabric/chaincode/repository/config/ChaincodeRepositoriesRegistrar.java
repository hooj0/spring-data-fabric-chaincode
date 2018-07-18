package io.github.hooj0.springdata.fabric.chaincode.repository.config;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * {@link RepositoryBeanDefinitionRegistrarSupport}通过{@link EnableChaincodeRepositories}设置Template存储库。
 * @author hoojo
 * @createDate 2018年7月18日 下午6:30:07
 * @file ChaincodeRepositoriesRegistrar.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.config
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableChaincodeRepositories.class;
	}

	@Override
	protected RepositoryConfigurationExtension getExtension() {
		return new ChaincodeRepositoryConfigExtension();
	}

}
