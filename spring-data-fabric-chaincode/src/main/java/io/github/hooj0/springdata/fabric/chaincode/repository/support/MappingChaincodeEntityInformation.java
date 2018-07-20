package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import org.springframework.data.repository.core.support.PersistentEntityInformation;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.core.convert.ChaincodeConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.repository.information.RepositoryAannotaitonInformation;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现 EntityInformation 填充 Entity 相关信息。
 * 并且可以充分利用 Converter/PersistentEntity 进行数据转换与填充。
 * @author hoojo
 * @createDate 2018年7月17日 下午7:00:51
 * @file MappingChaincodeEntityInformation.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class MappingChaincodeEntityInformation<T, ID> extends PersistentEntityInformation<T, ID> implements ChaincodeEntityInformation<T, ID> {

	private final ChaincodePersistentEntity<T> entityMetadata;
	private final ChaincodeConverter converter;
	
	private RepositoryAannotaitonInformation repositoryAannotaitonInformation;
	
	public MappingChaincodeEntityInformation(ChaincodePersistentEntity<T> entity, ChaincodeConverter converter) {
		super(entity);
		
		Assert.notNull(converter, "ChaincodeConverter must not be null!");
		Assert.notNull(entity, "ChaincodePersistentEntity must not be null!");

		this.entityMetadata = entity;
		this.converter = converter;
	}
	
	public void out() {
		log.debug("entityMetadata name: {}", entityMetadata.getName());
		log.debug("repositoryAannotaitonInformation: {}", repositoryAannotaitonInformation);
		log.debug("entityMetadata: {}", entityMetadata);
		log.debug("converter: {}", converter);
	}

	@Override
	public RepositoryAannotaitonInformation getRepositoryAannotaitonInformation() {
		return repositoryAannotaitonInformation;
	}

	public void setRepositoryAannotaitonInformation(RepositoryAannotaitonInformation repositoryAannotaitonInformation) {
		Assert.notNull(repositoryAannotaitonInformation, "RepositoryAannotaitonInformation must not be null!");

		this.repositoryAannotaitonInformation = repositoryAannotaitonInformation;
	}
}
