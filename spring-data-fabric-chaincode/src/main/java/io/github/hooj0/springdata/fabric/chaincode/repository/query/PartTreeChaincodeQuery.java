package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.util.List;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.parser.PartTree;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.parser.ChaincodeQueryCreator;

/**
 * PartTree 自动派生查询操作
 * @changelog automatic derived `PartTree` query operation
 * @author hoojo
 * @createDate 2018年7月18日 下午4:24:06
 * @file PartTreeChaincodeQuery.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class PartTreeChaincodeQuery extends AbstractChaincodeQuery {

	private final MappingContext<?, ChaincodePersistentProperty> mappingContext;
	private final PartTree tree;
	
	public PartTreeChaincodeQuery(ChaincodeQueryMethod queryMethod, ChaincodeOperations operations) {
		super(queryMethod, operations);
		
		this.tree = new PartTree(method.getName(), method.getEntityInformation().getJavaType());
		this.mappingContext = operations.getConverter().getMappingContext();
	}

	@Override
	protected String[] createQuery(ParametersParameterAccessor accessor, Object[] parameterValues) {
		
		List<String> queries = new ChaincodeQueryCreator(tree, accessor, mappingContext, conversionService).createQuery();
		return (String[]) queries.toArray();
	}

	@Override
	public Object execute(Object[] values) {
		return null;
	}
}
