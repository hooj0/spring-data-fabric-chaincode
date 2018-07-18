package io.github.hooj0.springdata.fabric.chaincode.repository.cdi;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.springframework.data.repository.cdi.CdiRepositoryBean;
import org.springframework.data.repository.config.CustomRepositoryImplementationDetector;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.support.ChaincodeRepositoryFactory;

/**
 * CDI 依赖注入 repo bean <br/>
 * 使用 CdiRepositoryBean 创建 {@link ChaincodeRepository } 实例
 * @author hoojo
 * @createDate 2018年7月17日 上午10:25:55
 * @file ChaincodeRepositoryBean.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.cdi
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeRepositoryBean<T> extends CdiRepositoryBean<T> {

	private final Bean<ChaincodeOperations> operationsBean;
	
	public ChaincodeRepositoryBean(Bean<ChaincodeOperations> operations, Set<Annotation> qualifiers, Class<T> repositoryType, BeanManager beanManager, @Nullable CustomRepositoryImplementationDetector detector) {
		super(qualifiers, repositoryType, beanManager, Optional.of(detector));

		Assert.notNull(operations, "Cannot create repository with 'null' for ChaincodeOperations.");
		this.operationsBean = operations;
	}

	@Override
	protected T create(CreationalContext<T> creationalContext, Class<T> repositoryType) {
		ChaincodeOperations chaincodeOperations = getDependencyInstance(operationsBean, ChaincodeOperations.class);

		return create(() -> new ChaincodeRepositoryFactory(repositoryType, chaincodeOperations), repositoryType);
	}
	
	@Override
	public Class<? extends Annotation> getScope() {
		return operationsBean.getScope();
	}
}
