package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;

import io.github.hooj0.springdata.fabric.chaincode.ChaincodeUnsupportedOperationException;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Deploy;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Instantiate;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Proposal;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Serialization;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Transaction;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Upgrade;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.core.query.Criteria;
import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;
import lombok.extern.slf4j.Slf4j;

/**
 * chaincode Repository query method information
 * @author hoojo
 * @createDate 2018年7月18日 上午9:44:31
 * @file ChaincodeQueryMethod.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class ChaincodeQueryMethod extends QueryMethod {

	private final MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
	private @Nullable ChaincodeEntityMetadata<?> metadata;
	private final Method method;
	
	private final Criteria criteria;
	private final ProposalType proposalType;
	private final Proposal proposalAnnotated;
	private final Deploy deployAnnotated;
	
	private Class[] annotationes = { Install.class, Instantiate.class, Upgrade.class, Invoke.class, Query.class, Channel.class, Transaction.class, Serialization.class };
	private ClassToInstanceMap<Annotation> annotationInstatnces = MutableClassToInstanceMap.<Annotation>create();
	
	@SuppressWarnings("unchecked")
	public ChaincodeQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory, MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext, Criteria criteria) {
		super(method, metadata, factory);
		
		this.method = method;
		this.mappingContext = mappingContext;
		this.criteria = criteria;
		this.proposalAnnotated = AnnotatedElementUtils.findMergedAnnotation(method, Proposal.class);

		verify(method, metadata);
		
		this.proposalType = (ProposalType) AnnotationUtils.getValue(proposalAnnotated, "type");
		this.deployAnnotated = AnnotatedElementUtils.findMergedAnnotation(method, Deploy.class);
		for (Class clazz : annotationes) {
			Annotation annotation = AnnotationUtils.findAnnotation(method, clazz);
			
			if (annotation != null) {
				annotationInstatnces.put(clazz, annotation);
			}
		}
		
		log.trace("--------------------------------------------------");
		log.trace("method: " + method.getName());
		log.trace("Proposal: " + proposalAnnotated);
		log.trace("Deploy: " + deployAnnotated);
		log.trace("annotationInstatnces: " + annotationInstatnces);
		log.trace("criteria: " + criteria);
		log.trace("--------------------------------------------------");
	}

	public Proposal getProposalAnnotated() {
		return this.proposalAnnotated;
	}
	
	public boolean hasProposalAnnotated() {
		return this.proposalAnnotated != null;
	}
	
	public String getAnnotatedProposal() {
		return (String) AnnotationUtils.getValue(proposalAnnotated, "value");
	}
	
	public ProposalType getAnnotatedProposalType() {
		return this.proposalType;
	}
	
	public Invoke getInvokeAnnotated() {
		return this.getAnnotation(Invoke.class);
	}
	
	public boolean hasInvokeAnnotated() {
		return proposalType == ProposalType.INVOKE || getInvokeAnnotated() != null;
	}
	
	public Query getQueryAnnotated() {
		return this.getAnnotation(Query.class);
	}
	
	public boolean hasQueryAnnotated() {
		return proposalType == ProposalType.QUERY || getQueryAnnotated() != null;
	}
	
	public Deploy getDeployAnnotated() {
		return this.deployAnnotated;
	}
	
	public boolean hasDeployAnnotated() {
		
		return getDeployAnnotated() != null;
	}
	
	public Install getInstallAnnotated() {
		return this.getAnnotation(Install.class);
	}
	
	public boolean hasInstallAnnotated() {
		return proposalType == ProposalType.INSTALL || getInstallAnnotated() != null;
	}
	
	public Instantiate getInstantiateAnnotated() {
		return this.getAnnotation(Instantiate.class);
	}
	
	public boolean hasInstantiateAnnotated() {
		return proposalType == ProposalType.INSTANTIATE || getInstantiateAnnotated() != null;
	}
	
	public Upgrade getUpgradeAnnotated() {
		return this.getAnnotation(Upgrade.class);
	}
	
	public boolean hasUpgradeAnnotated() {
		return proposalType == ProposalType.UPGRADE || getUpgradeAnnotated() != null;
	}
	
	public Channel getChannelAnnotated() {
		return this.getAnnotation(Channel.class);
	}
	
	public boolean hasChannelAnnotated() {
		return getChannelAnnotated() != null;
	}
	
	public Transaction getTransactionAnnotated() {
		return this.getAnnotation(Transaction.class);
	}
	
	public boolean hasTransactionAnnotated() {
		return getTransactionAnnotated() != null;
	}
	
	public Serialization getSerializationAnnotated() {
		return this.getAnnotation(Serialization.class);
	}
	
	public boolean hasSerializationAnnotated() {
		return getSerializationAnnotated() != null;
	}

	public TypeInformation<?> getReturnType() {
		return ClassTypeInformation.fromReturnTypeOf(this.method);
	}
	
	public Class<?> getResultType() {
		TypeInformation<?> actualType = getReturnType().getActualType();

		return actualType.getType();
	}
	
	@SuppressWarnings({ "unchecked" })
	private <T> T getAnnotation(Class<T> annotationClass) {
		
		if (annotationInstatnces.containsKey(annotationClass)) {
			return (T) annotationInstatnces.get(annotationClass);
		}
		
		return null;
	}
	
	public Criteria getCriteria() {
		return this.criteria;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ChaincodeEntityMetadata<?> getEntityInformation() {

		if (this.metadata == null) {
			Class<?> returnedObjectType = getReturnedObjectType();
			Class<?> domainClass = getDomainClass();
			
			if (ClassUtils.isPrimitiveOrWrapper(returnedObjectType)) {
				this.metadata = new SimpleChaincodeEntityMetadata<>((Class<Object>) domainClass, this.mappingContext.getRequiredPersistentEntity(domainClass)); 
			} else {
				ChaincodePersistentEntity<?> returnedEntity = this.mappingContext.getPersistentEntity(returnedObjectType);
				ChaincodePersistentEntity<?> managedEntity = this.mappingContext.getRequiredPersistentEntity(domainClass);

				returnedEntity = returnedEntity == null || returnedEntity.getType().isInterface() ? managedEntity : returnedEntity;
				
				this.metadata = new SimpleChaincodeEntityMetadata<>(returnedEntity.getType(), managedEntity); 
			}
		}
		
		return this.metadata;
	}

	public String[] getRequiredAnnotatedQuery() {
		return Optional.of(this.proposalAnnotated).map(Proposal::args).orElseThrow(() -> new IllegalStateException("Chaincode Repository method " + this + " has no annotated Proposal"));
	}
	
	public void verify(Method method, RepositoryMetadata metadata) {
		if (isPageQuery()) {
			throw new ChaincodeUnsupportedOperationException("Page queries are not supported. Use a Slice query.");
		}
		if (!this.hasProposalAnnotated()) {
			throw new ChaincodeUnsupportedOperationException("Repository " + method.getName() + " are not supported.");
		}
	}
}
