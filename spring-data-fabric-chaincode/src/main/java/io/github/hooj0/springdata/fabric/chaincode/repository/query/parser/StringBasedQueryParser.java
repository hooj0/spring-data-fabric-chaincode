package io.github.hooj0.springdata.fabric.chaincode.repository.query.parser;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.query.ParametersParameterAccessor;

/**
 * 将 含有占位符的 字符串转换成对应参数值的字符串
 * @changelog convert a string containing a placeholder to a string of corresponding parameter values
 * @author hoojo
 * @createDate 2018年8月1日 上午9:57:48
 * @file StringBasedQueryBinder.java
 * @package io.github.hooj0.springdata.fabric.chaincode.repository.query.parser
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class StringBasedQueryParser {

	private static final Pattern INDEX_PARAMETER_PLACEHOLDER = Pattern.compile("\\?(\\d+)");
	private static final Pattern SIMPLE_PARAMETER_PLACEHOLDER = Pattern.compile("\\?");
	private ConversionService conversionService;
	
	public StringBasedQueryParser(ConversionService conversionService) {
		this.conversionService = conversionService;
	}
	
	public String replacePlaceholders(String input, Object[] values) {
		Matcher matcher = SIMPLE_PARAMETER_PLACEHOLDER.matcher(input);

		String result = input;
		int index = 0;
		while (matcher.find()) {
			String group = matcher.group();
			result = result.replace(group, getParameterWithIndex(values, index));
			index ++;
		}

		return result;
	}

	// 通过参数占位符获取参数名称
	@SuppressWarnings("rawtypes")
	protected String getParameterWithIndex(Object[] values, int index) {
		Object parameter = values[index];
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
	
	// 替换占位符，将其替换为正确的参数
	public String replacePlaceholders(String input, ParametersParameterAccessor accessor) {
		Matcher matcher = INDEX_PARAMETER_PLACEHOLDER.matcher(input);

		String result = input;
		while (matcher.find()) {
			String group = matcher.group();
			int index = Integer.parseInt(matcher.group(1));
			result = result.replace(group, getParameterWithIndex(accessor, index));
		}

		return result;
	}
	
	@SuppressWarnings("rawtypes")
	protected String getParameterWithIndex(ParametersParameterAccessor accessor, int index) {
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
