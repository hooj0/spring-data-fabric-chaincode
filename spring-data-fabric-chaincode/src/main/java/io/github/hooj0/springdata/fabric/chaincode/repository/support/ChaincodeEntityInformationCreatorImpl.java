package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Chaincode;
import io.github.hooj0.springdata.fabric.chaincode.annotations.repository.Channel;
import io.github.hooj0.springdata.fabric.chaincode.core.convert.MappingChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.repository.information.RepositoryAannotaitonInformation;

/**
 * 实现 ChaincodeEntityInformationCreator 接口，完成对象实体元数据填充
 * @author hoojo
 * @createDate 2018年7月17日 下午6:55:56
 * @file ChaincodeEntityInformationCreatorImpl.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeEntityInformationCreatorImpl implements ChaincodeEntityInformationCreator {

	private final MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
	
	private RepositoryAannotaitonInformation repositoryAannotaitonInformation;
	
	public ChaincodeEntityInformationCreatorImpl(Class<?> repositoryInterface, MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext) {
		Assert.notNull(mappingContext, "MappingContext must not be null!");
		Assert.notNull(repositoryInterface, "repositoryInterface must not be null!");

		this.mappingContext = mappingContext;
		
		this.repositoryAannotaitonInformation = bindRepositoryAannotaitonInformation(repositoryInterface);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T, ID> ChaincodeEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		ChaincodePersistentEntity<T> persistentEntity = (ChaincodePersistentEntity<T>) mappingContext.getRequiredPersistentEntity(domainClass);

		Assert.notNull(persistentEntity, String.format("Unable to obtain mapping metadata for %s!", domainClass));
		Assert.notNull(persistentEntity.getIdProperty(), String.format("No id property found for %s!", domainClass));

		MappingChaincodeEntityInformation<T, ID> entityInformation = new MappingChaincodeEntityInformation<>(persistentEntity, new MappingChaincodeConverter(mappingContext));
		entityInformation.setRepositoryAannotaitonInformation(getRepositoryAannotaitonInformation());
		
		return entityInformation;
	}
	
	private RepositoryAannotaitonInformation bindRepositoryAannotaitonInformation(Class<?> repositoryInterface) {
		RepositoryAannotaitonInformation information = new RepositoryAannotaitonInformation();
		
		Channel channel = AnnotatedElementUtils.findMergedAnnotation(repositoryInterface, Channel.class);
		if (channel != null) {
			information.setChannelName(channel.name());
		}

		Chaincode chaincode = AnnotationUtils.findAnnotation(repositoryInterface, Chaincode.class);
		if (chaincode != null) {
			
			information.setChannelName(StringUtils.defaultIfBlank(chaincode.channel(), information.getChannelName()));
			information.setChaincodeName(chaincode.name());
			information.setChaincodePath(chaincode.path());
			information.setChaincodeType(chaincode.type());
			information.setChaincodeVersion(chaincode.version());
		}
		
		System.err.println(information);
		return information;
	}

	@Override
	public RepositoryAannotaitonInformation getRepositoryAannotaitonInformation() {
		return repositoryAannotaitonInformation;
	}
}
