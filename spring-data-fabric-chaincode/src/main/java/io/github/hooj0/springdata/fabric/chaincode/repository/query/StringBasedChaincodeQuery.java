package io.github.hooj0.springdata.fabric.chaincode.repository.query;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import io.github.hooj0.springdata.fabric.chaincode.core.ChaincodeOperations;
import lombok.extern.slf4j.Slf4j;

/**
 * String 类型的 Chaincode Query，多用于注解配置的查询方式
 * @author hoojo
 * @createDate 2018年7月18日 下午3:26:07
 * @file StringBasedChaincodeQuery.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class StringBasedChaincodeQuery extends AbstractChaincodeQuery {

	private static final Pattern PARAMETER_PLACEHOLDER = Pattern.compile("\\?(\\d+)");
	private static final String QUERY_ARGS_SEPARATOR = "|,|";

	private String query;
	
	public StringBasedChaincodeQuery(ChaincodeQueryMethod method, ChaincodeOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		this(StringUtils.join(method.getRequiredAnnotatedQuery(), QUERY_ARGS_SEPARATOR), method, operations, expressionParser, evaluationContextProvider);
	}

	public StringBasedChaincodeQuery(String namedQuery, ChaincodeQueryMethod queryMethod, ChaincodeOperations operations, SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {
		super(queryMethod, operations);
		
		this.query = namedQuery;
		//this.stringBasedQuery = new StringBasedQuery(namedQuery, new ExpressionEvaluatingParameterBinder(expressionParser, evaluationContextProvider));
	}

	@Override
	protected String[] createQuery(ParametersParameterAccessor parameterAccessor) {
		log.info("args string: {}", query);

		String result = replacePlaceholders(query, parameterAccessor);

		log.info("args result: {}", new Object[] { result });
		return StringUtils.split(result, QUERY_ARGS_SEPARATOR);
	}

	// 替换占位符，将其替换为正确的参数
	private String replacePlaceholders(String input, ParametersParameterAccessor accessor) {
		Matcher matcher = PARAMETER_PLACEHOLDER.matcher(input);
		
		String result = input;
		while (matcher.find()) {
			String group = matcher.group();
			int index = Integer.parseInt(matcher.group(1));
			result = result.replace(group, getParameterWithIndex(accessor, index));
		}
		
		return result;
	}

	// 通过参数占位符获取参数名称
	@SuppressWarnings("rawtypes")
	private String getParameterWithIndex(ParametersParameterAccessor accessor, int index) {
		Object parameter = accessor.getBindableValue(index);
		if (parameter == null) {
			return "null";
		}
		
		// 参数转换
		if (conversionService.canConvert(parameter.getClass(), String.class)) {
			return conversionService.convert(parameter, String.class);
		}
		
		if (parameter instanceof Collection) {
			StringBuilder sb = new StringBuilder();
			for (Object o : (Collection) parameter) {
				if (conversionService.canConvert(o.getClass(), String.class)) {
					sb.append(conversionService.convert(o, String.class));
				} else {
					sb.append(o.toString());
				}
				sb.append(" ");
			}
			return sb.toString().trim();
		}
		
		return parameter.toString();
	}
}
