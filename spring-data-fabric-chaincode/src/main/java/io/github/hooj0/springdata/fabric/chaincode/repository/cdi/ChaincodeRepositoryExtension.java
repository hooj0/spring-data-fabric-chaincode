package io.github.hooj0.springdata.fabric.chaincode.repository.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessBean;

import org.springframework.data.repository.cdi.CdiRepositoryBean;
import org.springframework.data.repository.cdi.CdiRepositoryExtensionSupport;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;

/**
 * 便携式CDI扩展，它为Spring Data存储库注册bean，
 * 让repo在不使用spring的ioc/di注入也能使用Injection进行注入
 * @author hoojo
 * @createDate 2018年7月18日 下午5:35:04
 * @file ChaincodeRepositoryExtension.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.cdi
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeRepositoryExtension extends CdiRepositoryExtensionSupport {

	private final Map<Set<Annotation>, Bean<ChaincodeOperations>> operationsMap = new HashMap<>();

	/**
	 * 实现一个观察器，它检查 ChaincodeOperations bean并将它们存储在{@link #operationsMap}中，
	 * 以便以后与相应的存储库bean相关联。
	 * @param <T> The type.
	 * @param processBean The annotated type as defined by CDI.
	 */
	@SuppressWarnings("unchecked")
	<T> void processBean(@Observes ProcessBean<T> processBean) {
		Bean<T> bean = processBean.getBean();
		for (Type type : bean.getTypes()) {
			if (type instanceof Class<?> && ChaincodeOperations.class.isAssignableFrom((Class<?>) type)) {
				operationsMap.put(bean.getQualifiers(), ((Bean<ChaincodeOperations>) bean));
			}
		}
	}
	
	/**
	 * 实现一个观察者，它将bean注册到CDI容器中，用于检测到的Spring Data存储库。
	 * 存储库bean使用其限定符与EntityManagers相关联。 
	 * @author hoojo
	 * @createDate 2018年7月9日 上午10:22:35
	 * @param afterBeanDiscovery
	 * @param beanManager
	 */
	void afterBeanDiscovery(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) {
		for (Entry<Class<?>, Set<Annotation>> entry : getRepositoryTypes()) {

			Class<?> repositoryType = entry.getKey();
			Set<Annotation> qualifiers = entry.getValue();

			CdiRepositoryBean<?> repositoryBean = createRepositoryBean(repositoryType, qualifiers, beanManager);
			afterBeanDiscovery.addBean(repositoryBean);
			registerBean(repositoryBean);
		}
	}
	
	/**
	 * 创建 ChaincodeRepositoryBean
	 * @author hoojo
	 * @createDate 2018年7月9日 上午10:24:10
	 */
	private <T> CdiRepositoryBean<T> createRepositoryBean(Class<T> repositoryType, Set<Annotation> qualifiers, BeanManager beanManager) {
		if (!this.operationsMap.containsKey(qualifiers)) {
			throw new UnsatisfiedResolutionException(String.format("Unable to resolve a bean for '%s' with qualifiers %s.", ChaincodeOperations.class.getName(), qualifiers));
		}

		Bean<ChaincodeOperations> operationsBean = this.operationsMap.get(qualifiers);

		return new ChaincodeRepositoryBean<>(operationsBean, qualifiers, repositoryType, beanManager, getCustomImplementationDetector());
	}
}
