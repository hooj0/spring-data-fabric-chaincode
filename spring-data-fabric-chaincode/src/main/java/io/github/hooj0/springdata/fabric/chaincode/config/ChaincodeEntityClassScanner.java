package io.github.hooj0.springdata.fabric.chaincode.config;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Model;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Table;

import io.github.hooj0.springdata.fabric.chaincode.annotations.Entity;

/**
 * 扫描 Chaincode 实体的包。 实体扫描程序使用基本包名称，
 * 基本包类或两者来扫描类路径上使用{@link #getEntityAnnotations() 实体注释}注释的实体类。
 */
public class ChaincodeEntityClassScanner {

	private Set<String> entityBasePackages = new HashSet<>();
	private Set<Class<?>> entityBasePackageClasses = new HashSet<>();
	private @Nullable ClassLoader beanClassLoader;

	/**
	 * Scan one or more base packages for entity classes. Classes are loaded using the current class loader.
	 *
	 * @param entityBasePackages must not be {@literal null}.
	 * @return {@link Set} containing all discovered entity classes.
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> scan(String... entityBasePackages) throws ClassNotFoundException {
		return new ChaincodeEntityClassScanner(entityBasePackages).scanForEntityClasses();
	}

	/**
	 * Scan one or more base packages for entity classes. Classes are loaded using the current class loader.
	 *
	 * @param entityBasePackageClasses must not be {@literal null}.
	 * @return {@link Set} containing all discovered entity classes.
	 * @throws ClassNotFoundException if a discovered class could not be loaded via.
	 */
	public static Set<Class<?>> scan(Class<?>... entityBasePackageClasses) throws ClassNotFoundException {
		return new ChaincodeEntityClassScanner(entityBasePackageClasses).scanForEntityClasses();
	}

	/**
	 * Scan one or more base packages for entity classes. Classes are loaded using the current class loader.
	 *
	 * @param entityBasePackages must not be {@literal null}.
	 * @return {@link Set} containing all discovered entity classes.
	 * @throws ClassNotFoundException if a discovered class could not be loaded via.
	 */
	public static Set<Class<?>> scan(Collection<String> entityBasePackages) throws ClassNotFoundException {
		return new ChaincodeEntityClassScanner(entityBasePackages).scanForEntityClasses();
	}

	/**
	 * Scan one or more base packages for entity classes. Classes are loaded using the current class loader.
	 *
	 * @param entityBasePackages must not be {@literal null}.
	 * @param entityBasePackageClasses must not be {@literal null}.
	 * @return {@link Set} containing all discovered entity classes.
	 * @throws ClassNotFoundException if a discovered class could not be loaded via.
	 */
	public static Set<Class<?>> scan(Collection<String> entityBasePackages, Collection<Class<?>> entityBasePackageClasses) throws ClassNotFoundException {
		return new ChaincodeEntityClassScanner(entityBasePackages, entityBasePackageClasses).scanForEntityClasses();
	}

	/**
	 * Creates a new {@link ChaincodeEntityClassScanner}.
	 */
	public ChaincodeEntityClassScanner() {}

	/**
	 * Creates a new {@link ChaincodeEntityClassScanner} given {@code entityBasePackageClasses}.
	 *
	 * @param entityBasePackageClasses must not be {@literal null}.
	 */
	public ChaincodeEntityClassScanner(Class<?>... entityBasePackageClasses) {
		setEntityBasePackageClasses(Arrays.asList(entityBasePackageClasses));
	}

	/**
	 * Creates a new {@link ChaincodeEntityClassScanner} given {@code entityBasePackages}.
	 *
	 * @param entityBasePackages must not be {@literal null}.
	 */
	public ChaincodeEntityClassScanner(String... entityBasePackages) {
		this(Arrays.asList(entityBasePackages));
	}

	/**
	 * Creates a new {@link ChaincodeEntityClassScanner} given {@code entityBasePackages}.
	 *
	 * @param entityBasePackages must not be {@literal null}.
	 */
	public ChaincodeEntityClassScanner(Collection<String> entityBasePackages) {
		setEntityBasePackages(entityBasePackages);
	}

	/**
	 * Creates a new {@link ChaincodeEntityClassScanner} given {@code entityBasePackages} and
	 * {@code entityBasePackageClasses}.
	 *
	 * @param entityBasePackages must not be {@literal null}.
	 * @param entityBasePackageClasses must not be {@literal null}.
	 */
	public ChaincodeEntityClassScanner(Collection<String> entityBasePackages, Collection<Class<?>> entityBasePackageClasses) {

		setEntityBasePackages(entityBasePackages);
		setEntityBasePackageClasses(entityBasePackageClasses);
	}

	/**
	 * @return base package names used for the entity scan.
	 */
	public Set<String> getEntityBasePackages() {
		return Collections.unmodifiableSet(entityBasePackages);
	}

	/**
	 * Set the base package names to be used for the entity scan.
	 *
	 * @param entityBasePackages must not be {@literal null}.
	 */
	public void setEntityBasePackages(Collection<String> entityBasePackages) {
		this.entityBasePackages = new HashSet<>(entityBasePackages);
	}

	/**
	 * @return base package classes used for the entity scan.
	 */
	public Set<Class<?>> getEntityBasePackageClasses() {
		return Collections.unmodifiableSet(entityBasePackageClasses);
	}

	/**
	 * Set the base package classes to be used for the entity scan.
	 *
	 * @param entityBasePackageClasses must not be {@literal null}.
	 */
	public void setEntityBasePackageClasses(Collection<Class<?>> entityBasePackageClasses) {
		this.entityBasePackageClasses = new HashSet<>(entityBasePackageClasses);
	}

	/**
	 * Set the bean {@link ClassLoader} to load class candidates discovered by the class path scan.
	 *
	 * @param beanClassLoader must not be {@literal null}.
	 */
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
	}

	/**
	 * Scans the mapping base package for entity classes annotated with {@link Table} or {@link Persistent}.
	 * @see #getEntityBasePackages()
	 * @return {@code Set<Class<?>>} representing the annotated entity classes found.
	 * @throws ClassNotFoundException if a discovered class could not be loaded via.
	 */
	public Set<Class<?>> scanForEntityClasses() throws ClassNotFoundException {

		Set<Class<?>> classes = new HashSet<>();

		for (String basePackage : getEntityBasePackages()) {
			classes.addAll(scanBasePackageForEntities(basePackage));
		}

		for (Class<?> basePackageClass : getEntityBasePackageClasses()) {
			classes.addAll(scanBasePackageForEntities(basePackageClass.getPackage().getName()));
		}

		return classes;
	}

	protected Set<Class<?>> scanBasePackageForEntities(String basePackage) throws ClassNotFoundException {

		HashSet<Class<?>> classes = new HashSet<>();

		if (StringUtils.isEmpty(basePackage)) {
			return classes;
		}

		// classpath 下 扫描
		ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);

		for (Class<? extends Annotation> annotation : getEntityAnnotations()) {
			componentProvider.addIncludeFilter(new AnnotationTypeFilter(annotation));
		}

		for (BeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {

			if (candidate.getBeanClassName() != null) {
				classes.add(ClassUtils.forName(candidate.getBeanClassName(), beanClassLoader));
			}
		}

		return classes;
	}

	/**
	 * @return entity annotations.
	 * @see Model
	 * @see Persistent
	 */
	@SuppressWarnings("unchecked")
	protected Class<? extends Annotation>[] getEntityAnnotations() {
		return new Class[] { Entity.class, Persistent.class };
	}
}
