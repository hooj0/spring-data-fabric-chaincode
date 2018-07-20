package io.github.hooj0.springdata.fabric.chaincode.repository.support;

import static org.springframework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

import io.github.hooj0.springdata.fabric.chaincode.ChaincodeUnsupportedOperationException;
import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentEntity;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.repository.ChaincodeRepository;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.ChaincodeQueryMethod;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.PartTreeChaincodeQuery;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.StringBasedChaincodeQuery;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过 ChaincodeRepositoryFactory 创建 {@link ChaincodeRepository} 实例
 * @author hoojo
 * @createDate 2018年7月17日 下午6:39:01
 * @file ChaincodeRepositoryFactory.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.support
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class ChaincodeRepositoryFactory extends RepositoryFactorySupport {

	private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
	
	private final ChaincodeEntityInformationCreator entityInformationCreator;
	private final ChaincodeOperations operations;
	
	public ChaincodeRepositoryFactory(Class<?> repositoryInterface, ChaincodeOperations operations) {
		log.debug("Creating chaincode bean factory.");
		
		Assert.notNull(operations, "ChaincodeOperations must not be null!");
		Assert.notNull(repositoryInterface, "repositoryInterface must not be null!");
		
		this.operations = operations;
		this.entityInformationCreator = new ChaincodeEntityInformationCreatorImpl(repositoryInterface, this.operations.getConverter().getMappingContext());
	}
	
	@Override
	public <T, ID> ChaincodeEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		return entityInformationCreator.getEntityInformation(domainClass);
	}

	@Override
	protected Object getTargetRepository(RepositoryInformation metadata) {
		// XXX new SimpleChaincodeRepository<T>();
		return getTargetRepositoryViaReflection(metadata, getEntityInformation(metadata.getDomainType()), operations);
	}

	/**
	 * 针对不同类型的 metadata 可以返回对应的 repo.class
	 * @author hoojo
	 * @createDate 2018年7月4日 下午4:47:32
	 */
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		if (isQueryDslRepository(metadata.getRepositoryInterface())) {
			throw new ChaincodeUnsupportedOperationException("QueryDsl Support has not been implemented yet.");
		}

		log.debug("IdType: {}", metadata.getIdType());
		if (String.class.isAssignableFrom(metadata.getIdType())) {
			return SimpleChaincodeRepository.class;
		} else if (metadata.getIdType() == Object.class) {
			return SimpleChaincodeRepository.class;
		} 
		
		throw new ChaincodeUnsupportedOperationException("Unknow Support has not been implemented yet.");
	}
	
	@Override
    public <T> T getRepository(Class<T> repositoryInterface) {
        log.debug("Creating proxy for {}", repositoryInterface.getSimpleName());

        return super.getRepository(repositoryInterface);
    }
	
	/**
	 * Querydsl 类型的查询 repo
	 * @author hoojo
	 * @createDate 2018年7月4日 下午4:47:10
	 */
	private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
		log.debug("repositoryInterface: {}", repositoryInterface);
		
		return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}
	
	@Override
	protected Optional<QueryLookupStrategy> getQueryLookupStrategy(Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		// 可以根据配置不同的key 适配不同的 EnableChaincodeRepositories.queryLookupStrategy()
		log.debug("key: {}", key);
		
		return Optional.of(new ChaincodeQueryLookupStrategy(operations, evaluationContextProvider, operations.getConverter().getMappingContext()));
	}

	/**
	 * 查询查找策略
	 */
	private class ChaincodeQueryLookupStrategy implements QueryLookupStrategy {

		private final ChaincodeOperations operations;
		private final QueryMethodEvaluationContextProvider evaluationContextProvider;
		private final MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext;
		
		ChaincodeQueryLookupStrategy(ChaincodeOperations operations, QueryMethodEvaluationContextProvider evaluationContextProvider, MappingContext<? extends ChaincodePersistentEntity<?>, ChaincodePersistentProperty> mappingContext) {
			this.operations = operations;
			this.evaluationContextProvider = evaluationContextProvider;
			this.mappingContext = mappingContext;
		}

		@Override
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {

			ChaincodeQueryMethod queryMethod = new ChaincodeQueryMethod(method, metadata, factory, mappingContext);
			String namedQueryName = queryMethod.getNamedQueryName();

			log.debug("queryMethod.getName: {}", queryMethod.getName());
			log.debug("queryMethod: {}", queryMethod);
			log.debug("namedQueryName: {}", namedQueryName);

			System.out.println("namedQueryName: " + namedQueryName);
			System.out.println("RepositoryInterface: " + metadata.getRepositoryInterface().getSimpleName());
			
			if (namedQueries.hasQuery(namedQueryName)) {
				String namedQuery = namedQueries.getQuery(namedQueryName);
				return new StringBasedChaincodeQuery(namedQuery, queryMethod, operations, EXPRESSION_PARSER, evaluationContextProvider);
			} else if (!queryMethod.hasDeployAnnotated()) {
				return new StringBasedChaincodeQuery(queryMethod, operations, EXPRESSION_PARSER, evaluationContextProvider);
			} else {
				return new PartTreeChaincodeQuery(queryMethod, operations);
			}
		}
	}
}
