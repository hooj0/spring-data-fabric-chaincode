package io.github.hooj0.springdata.fabric.chaincode.repository.query.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import io.github.hooj0.springdata.fabric.chaincode.repository.query.ChaincodeQueryMethod;
import lombok.ToString;

/**
 * 查询语句 参数表达式绑定器
 * @changelog query statement parameter expression binder
 * @author hoojo
 * @createDate 2018年7月14日 下午9:14:55
 * @file ExpressionEvaluatingParameterBinder.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ExpressionEvaluatingParameterBinder {

	private final SpelExpressionParser expressionParser;
	private final QueryMethodEvaluationContextProvider evaluationContextProvider;

	/**
	 * Creates new {@link ExpressionEvaluatingParameterBinder}
	 * @param expressionParser
	 *            must not be {@literal null}.
	 * @param evaluationContextProvider
	 *            must not be {@literal null}.
	 */
	public ExpressionEvaluatingParameterBinder(SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {

		Assert.notNull(expressionParser, "ExpressionParser must not be null");
		Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null");

		this.expressionParser = expressionParser;
		this.evaluationContextProvider = evaluationContextProvider;
	}

	/**
	 * Bind values provided by {@link ParametersParameterAccessor} to placeholders in {@link BindingContext} while
	 * considering potential conversions and parameter types.
	 * @param parameterAccessor
	 *            must not be {@literal null}.
	 * @param bindingContext
	 *            must not be {@literal null}.
	 * @return {@literal null} if given {@code raw} value is empty.
	 */
	public List<Object> bind(ParametersParameterAccessor parameterAccessor, BindingContext bindingContext, Object[] values) {

		if (!bindingContext.hasBindings()) {
			return Collections.emptyList();
		}

		List<Object> parameters = new ArrayList<>(bindingContext.getBindings().size());

		bindingContext.getBindings() //
				.stream() //
				.map(binding -> getParameterValueForBinding(parameterAccessor, bindingContext.getParameters(), binding, values)) //
				.forEach(parameters::add);

		return parameters;
	}

	/**
	 * 将参数值绑定到对应参数索引占位符的索引上
	 * @param parameterAccessor
	 *            must not be {@literal null}.
	 * @param parameters
	 *            must not be {@literal null}.
	 * @param binding
	 *            must not be {@literal null}.
	 * @return the value used for the given {@link ParameterBinding}.
	 */
	@Nullable
	private Object getParameterValueForBinding(ParametersParameterAccessor parameterAccessor, Parameters<?, ?> parameters, ParameterBinding binding, Object[] values) {
		if (binding.isExpression()) {
			return evaluateExpression(binding.getExpression(), parameters, values);
		}

		return binding.isNamed()
				? parameterAccessor.getBindableValue(getParameterIndex(parameters, binding.getParameterName()))
				: parameterAccessor.getBindableValue(binding.getParameterIndex());
	}

	// 获取参数的索引位置
	private int getParameterIndex(Parameters<?, ?> parameters, String parameterName) {

		return parameters.stream() //
				.filter(parameter -> parameter //
						.getName().filter(s -> s.equals(parameterName)) //
						.isPresent()) //
				.mapToInt(Parameter::getIndex) //
				.findFirst() //
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Invalid parameter name; Cannot resolve parameter [%s]", parameterName)));
	}

	/**
	 * 转换参数表达式
	 * @param expressionString
	 *            must not be {@literal null} or empty.
	 * @param parameters
	 *            must not be {@literal null}.
	 * @param parameterValues
	 *            must not be {@literal null}.
	 * @return the value of the {@code expressionString} evaluation.
	 */
	@Nullable
	private Object evaluateExpression(String expressionString, Parameters<?, ?> parameters, Object[] parameterValues) {

		EvaluationContext evaluationContext = evaluationContextProvider.getEvaluationContext(parameters, parameterValues);
		Expression expression = expressionParser.parseExpression(expressionString);

		return expression.getValue(evaluationContext, Object.class);
	}

	/**
	 * 参数绑定上下文
	 */
	static class BindingContext {

		final ChaincodeQueryMethod queryMethod;
		final List<ParameterBinding> bindings;

		/**
		 * Creates new {@link BindingContext}.
		 * @param queryMethod
		 *            {@link ChaincodeQueryMethod} on which the parameters are evaluated.
		 * @param bindings
		 *            {@link List} of {@link ParameterBinding} containing name or position (index) information
		 *            pertaining to the parameter in the referenced {@code queryMethod}.
		 */
		public BindingContext(ChaincodeQueryMethod queryMethod, List<ParameterBinding> bindings) {
			this.queryMethod = queryMethod;
			this.bindings = bindings;
		}

		/**
		 * @return {@literal true} when list of bindings is not empty.
		 */
		boolean hasBindings() {
			return !CollectionUtils.isEmpty(bindings);
		}

		/**
		 * Get unmodifiable list of {@link ParameterBinding}s.
		 * @return never {@literal null}.
		 */
		List<ParameterBinding> getBindings() {
			return Collections.unmodifiableList(bindings);
		}

		/**
		 * Get the associated {@link TemplateParameters}.
		 * @return the {@link TemplateParameters} associated with the {@link ChaincodeQueryMethod}.
		 */
		Parameters<?, ?> getParameters() {
			return queryMethod.getParameters();
		}

		/**
		 * Get the {@link ChaincodeQueryMethod}.
		 * @return the {@link ChaincodeQueryMethod} used in the expression evaluation context.
		 */
		ChaincodeQueryMethod getQueryMethod() {
			return queryMethod;
		}
	}

	/**
	 * 与名称或索引数据绑定的通用参数。
	 */
	@ToString
	public static class ParameterBinding {

		private final boolean quoted;
		private final int parameterIndex;
		private final @Nullable String expression;
		private final @Nullable String parameterName;

		private ParameterBinding(int parameterIndex, boolean quoted, @Nullable String expression, @Nullable String parameterName) {

			this.parameterIndex = parameterIndex;
			this.quoted = quoted;
			this.expression = expression;
			this.parameterName = parameterName;
		}

		static ParameterBinding expression(String expression, boolean quoted) {
			return new ParameterBinding(-1, quoted, expression, null);
		}

		static ParameterBinding indexed(int parameterIndex) {
			return new ParameterBinding(parameterIndex, false, null, null);
		}

		static ParameterBinding named(String name) {
			return new ParameterBinding(-1, false, null, name);
		}

		boolean isNamed() {
			return (parameterName != null);
		}

		int getParameterIndex() {
			return parameterIndex;
		}

		String getParameter() {
			return ("?" + (isExpression() ? "expr" : "") + parameterIndex);
		}

		@Nullable
		String getExpression() {
			return expression;
		}

		boolean isExpression() {
			return (this.expression != null);
		}

		@Nullable
		String getParameterName() {
			return parameterName;
		}
	}
}
