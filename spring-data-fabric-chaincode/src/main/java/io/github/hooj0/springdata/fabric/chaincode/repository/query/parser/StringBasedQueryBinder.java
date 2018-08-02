package io.github.hooj0.springdata.fabric.chaincode.repository.query.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import io.github.hooj0.springdata.fabric.chaincode.repository.query.ChaincodeQueryMethod;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.parser.ExpressionEvaluatingParameterBinder.BindingContext;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.parser.ExpressionEvaluatingParameterBinder.ParameterBinding;

/**
 * 将字符串中的参数表达式绑定为表达式参数值后的字符串对象
 * @changelog bind a parameter expression in a string to a string object after the expression parameter value
 * @createDate 2018年7月14日 下午9:11:05
 * @file StringBasedQueryBinder.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query.parser
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class StringBasedQueryBinder {

	private final String query;
	private final ExpressionEvaluatingParameterBinder parameterBinder;
	private final List<ParameterBinding> queryParameterBindings = new ArrayList<>();

	/**
	 * Create a new {@link StringBasedQueryBinder} given {@code query}, {@link ExpressionEvaluatingParameterBinder} and
	 * {@link CodecRegistry}.
	 * @param query must not be empty.
	 * @param parameterBinder must not be {@literal null}.
	 */
	public StringBasedQueryBinder(String query, ExpressionEvaluatingParameterBinder parameterBinder) {
		Assert.hasText(query, "Query must not be empty");
		Assert.notNull(parameterBinder, "ExpressionEvaluatingParameterBinder must not be null");

		this.parameterBinder = parameterBinder;
		this.query = ParameterBindingParser.INSTANCE.parseAndCollectParameterBindingsFromQueryIntoBindings(query, this.queryParameterBindings);
	}

	public ExpressionEvaluatingParameterBinder getParameterBinder() {
		return this.parameterBinder;
	}

	protected String getQuery() {
		return this.query;
	}

	/**
	 * 将方法和参数、及其参数值，绑定一个Statement对象
	 * @param parameterAccessor must not be {@literal null}.
	 * @param queryMethod must not be {@literal null}.
	 * @return the bound String query containing formatted parameters.
	 */
	public SimpleStatement bindQuery(ParametersParameterAccessor parameterAccessor, ChaincodeQueryMethod queryMethod, Object[] values) {
		Assert.notNull(parameterAccessor, "ParametersParameterAccessor must not be null");
		Assert.notNull(queryMethod, "TemplateQueryMethod must not be null");

		List<Object> arguments = getParameterBinder().bind(parameterAccessor, new BindingContext(queryMethod, this.queryParameterBindings), values);

		return ParameterBinder.INSTANCE.bind(getQuery(), arguments);
	}
	
	/**
	 * 从给定查询字符串中提取参数绑定的解析器。
	 * 将 ?_param_? 替换为 ?
	 */
	public static enum ParameterBinder {

		INSTANCE;

		private static final String ARGUMENT_PLACEHOLDER = "?_param_?";
		private static final Pattern ARGUMENT_PLACEHOLDER_PATTERN = Pattern.compile(Pattern.quote(ARGUMENT_PLACEHOLDER));

		public SimpleStatement bind(String input, List<Object> parameters) {

			if (parameters.isEmpty()) {
				return new SimpleStatement(input);
			}

			StringBuilder result = new StringBuilder();

			int startIndex = 0;
			int currentPosition = 0;
			int parameterIndex = 0;

			Matcher matcher = ARGUMENT_PLACEHOLDER_PATTERN.matcher(input);
			while (currentPosition < input.length()) {
				if (!matcher.find()) {
					break;
				}

				int exprStart = matcher.start();
				result.append(input.subSequence(startIndex, exprStart)).append("?");

				parameterIndex++;
				currentPosition = matcher.end();
				startIndex = currentPosition;
			}

			String bindableStatement = result.append(input.subSequence(currentPosition, input.length())).toString();
			return new SimpleStatement(bindableStatement, parameters.subList(0, parameterIndex).toArray());
		}
	}

	/**
	 * 从给定查询字符串中提取参数绑定的解析器。
	 * 将参数或表达式替换成 ?_param_?
	 */
	public static enum ParameterBindingParser {

		INSTANCE;

		private static final char CURRLY_BRACE_OPEN = '{';
		private static final char CURRLY_BRACE_CLOSE = '}';

		private static final Pattern INDEX_PARAMETER_BINDING_PATTERN = Pattern.compile("\\?(\\d+)");
		private static final Pattern NAMED_PARAMETER_BINDING_PATTERN = Pattern.compile("\\:(\\w+)");
		private static final Pattern INDEX_BASED_EXPRESSION_PATTERN = Pattern.compile("\\?\\#\\{");
		private static final Pattern NAME_BASED_EXPRESSION_PATTERN = Pattern.compile("\\:\\#\\{");

		private static final String ARGUMENT_PLACEHOLDER = "?_param_?";

		/**
		 * Returns a list of {@link ParameterBinding}s found in the given {@code input}.
		 * @param input can be {@literal null} or empty.
		 * @param bindings must not be {@literal null}.
		 * @return a list of {@link ParameterBinding}s found in the given {@code input}.
		 */
		public String parseAndCollectParameterBindingsFromQueryIntoBindings(String input, List<ParameterBinding> bindings) {
			if (!StringUtils.hasText(input)) {
				return input;
			}
			
			Assert.notNull(bindings, "Parameter bindings must not be null");
			return transformQueryAndCollectExpressionParametersIntoBindings(input, bindings);
		}

		private static String transformQueryAndCollectExpressionParametersIntoBindings(String input, List<ParameterBinding> bindings) {
			StringBuilder result = new StringBuilder();

			int startIndex = 0;
			int currentPosition = 0;
			while (currentPosition < input.length()) {

				Matcher matcher = findNextBindingOrExpression(input, currentPosition);
				// no expression parameter found
				if (matcher == null) {
					break;
				}

				int exprStart = matcher.start();
				currentPosition = exprStart;
				if (matcher.pattern() == NAME_BASED_EXPRESSION_PATTERN || matcher.pattern() == INDEX_BASED_EXPRESSION_PATTERN) {
					// eat parameter expression
					int curlyBraceOpenCount = 1;
					currentPosition += 3;

					while (curlyBraceOpenCount > 0 && currentPosition < input.length()) {
						switch (input.charAt(currentPosition++)) {
							case CURRLY_BRACE_OPEN:
								curlyBraceOpenCount++;
								break;
							case CURRLY_BRACE_CLOSE:
								curlyBraceOpenCount--;
								break;
							default:
						}
					}

					result.append(input.subSequence(startIndex, exprStart));
				} else {
					result.append(input.subSequence(startIndex, exprStart));
				}
				result.append(ARGUMENT_PLACEHOLDER);

				if (matcher.pattern() == NAME_BASED_EXPRESSION_PATTERN || matcher.pattern() == INDEX_BASED_EXPRESSION_PATTERN) {
					bindings.add(ExpressionEvaluatingParameterBinder.ParameterBinding.expression(input.substring(exprStart + 3, currentPosition - 1), true));
				} else {
					if (matcher.pattern() == INDEX_PARAMETER_BINDING_PATTERN) {
						bindings.add(ExpressionEvaluatingParameterBinder.ParameterBinding.indexed(Integer.parseInt(matcher.group(1))));
					} else {
						bindings.add(ExpressionEvaluatingParameterBinder.ParameterBinding.named(matcher.group(1)));
					}
					currentPosition = matcher.end();
				}

				startIndex = currentPosition;
			}

			return result.append(input.subSequence(currentPosition, input.length())).toString();
		}

		@Nullable
		private static Matcher findNextBindingOrExpression(String input, int position) {
			List<Matcher> matchers = new ArrayList<>();

			matchers.add(INDEX_PARAMETER_BINDING_PATTERN.matcher(input));
			matchers.add(NAMED_PARAMETER_BINDING_PATTERN.matcher(input));
			matchers.add(INDEX_BASED_EXPRESSION_PATTERN.matcher(input));
			matchers.add(NAME_BASED_EXPRESSION_PATTERN.matcher(input));

			TreeMap<Integer, Matcher> matcherMap = new TreeMap<>();
			for (Matcher matcher : matchers) {
				if (matcher.find(position)) {
					matcherMap.put(matcher.start(), matcher);
				}
			}

			return (matcherMap.isEmpty() ? null : matcherMap.values().iterator().next());
		}
	}
}
