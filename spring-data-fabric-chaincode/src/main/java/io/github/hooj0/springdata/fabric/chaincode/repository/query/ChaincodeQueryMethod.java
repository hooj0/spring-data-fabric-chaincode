package io.github.hooj0.springdata.fabric.chaincode.repository.query;

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

import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Deploy;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Install;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Invoke;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Proposal;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Query;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Upgrade;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.enums.ProposalType;

/**
 * Repository 查询方法的具体信息
 * @author hoojo
 * @createDate 2018年7月18日 上午9:44:31
 * @file ChaincodeQueryMethod.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeQueryMethod extends QueryMethod {

	private MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
	private @Nullable ChaincodeEntityMetadata<?> metadata;
	private final Method method;
	private Proposal proposal;
	
	public ChaincodeQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory, MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext) {
		super(method, metadata, factory);
		
		verify(method, metadata);
		
		this.method = method;
		this.mappingContext = mappingContext;
		
		this.proposal = AnnotatedElementUtils.findMergedAnnotation(method, Proposal.class);
		
		System.out.println("--------------------------------------------------");
		System.out.println("method: " + method.getName());
		System.out.println("query: " + proposal);
		System.out.println("--------------------------------------------------");
	}

	public Proposal getProposalAnnotated() {
		return this.proposal;
	}
	
	public boolean hasProposalAnnotated() {
		return this.proposal != null;
	}
	
	public String getAnnotatedProposal() {
		return (String) AnnotationUtils.getValue(proposal, "value");
	}
	
	public ProposalType getAnnotatedProposalType() {
		return (ProposalType) AnnotationUtils.getValue(proposal, "type");
	}
	
	public Invoke getInvokeAnnotated() {
		return null;
	}
	
	public boolean hasInvokeAnnotated() {
		return getInvokeAnnotated() != null;
	}
	
	public Query getQueryAnnotated() {
		return null;
	}
	
	public boolean hasQueryAnnotated() {
		return getQueryAnnotated() != null;
	}
	
	public Deploy getDeployAnnotated() {
		return null;
	}
	
	public boolean hasDeployAnnotated() {
		return getDeployAnnotated() != null;
	}
	
	public Install getInstallAnnotated() {
		return null;
	}
	
	public boolean hasInstallAnnotated() {
		return getInstallAnnotated() != null;
	}
	
	public Install getInstantiateAnnotated() {
		return null;
	}
	
	public boolean hasInstantiateAnnotated() {
		return getInstantiateAnnotated() != null;
	}
	
	public Upgrade getUpgradeAnnotated() {
		return null;
	}
	
	public boolean hasUpgradeAnnotated() {
		return getUpgradeAnnotated() != null;
	}

	public TypeInformation<?> getReturnType() {
		return ClassTypeInformation.fromReturnTypeOf(this.method);
	}
	
	public Class<?> getResultType() {
		TypeInformation<?> actualType = getReturnType().getActualType();

		return actualType.getType();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ChaincodeEntityMetadata<?> getEntityInformation() {

		if (this.metadata == null) {
			Class<?> returnedObjectType = getReturnedObjectType();
			Class<?> domainClass = getDomainClass();
			
			System.out.println("***************************************************");
			System.out.println("returnedObjectType: " + returnedObjectType);
			System.out.println("domainClass: " + domainClass);
			System.out.println("DeclaringClass: " + method.getDeclaringClass());
			System.out.println("***************************************************");
			
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

	public String getRequiredAnnotatedQuery() {
		return Optional.of(this.proposal).map(Proposal::value).orElseThrow(() -> new IllegalStateException("Chaincode Repository method " + this + " has no annotated Proposal"));
	}
	
	public void verify(Method method, RepositoryMetadata metadata) {
		if (isPageQuery()) {
			throw new RuntimeException("Page queries are not supported. Use a Slice query.");
		}
	}
}
