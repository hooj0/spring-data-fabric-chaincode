package io.github.hooj0.springdata.fabric.chaincode.core.mapping;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.collect.Maps;

import io.github.hooj0.springdata.fabric.chaincode.ChaincodeMappingException;
import io.github.hooj0.springdata.fabric.chaincode.annotations.Entity;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单持久化实体信息实现
 * @author hoojo
 * @createDate 2018年7月17日 上午11:30:18
 * @file SimpleChaincodePersistentEntity.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.mapping
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@Slf4j
public class SimpleChaincodePersistentEntity<T> extends BasicPersistentEntity<T, ChaincodePersistentProperty> implements ChaincodePersistentEntity<T>, ApplicationContextAware {

	private final StandardEvaluationContext context;
	private final SpelExpressionParser parser;
	
	private Map<String, byte[]> transientData = Maps.newHashMap();
	
	public SimpleChaincodePersistentEntity(TypeInformation<T> information) {
		super(information);
		
		this.context = new StandardEvaluationContext();
		this.parser = new SpelExpressionParser();
		
		// 从注解中获取数据，对entity 属性进行填充数据
		Class<T> clazz = information.getType();
		if (clazz.isAnnotationPresent(Entity.class)) {
			Entity entity = clazz.getAnnotation(Entity.class);
			log.debug("@Entity: {}", entity);
		} else {
			throw new ChaincodeMappingException("No added @Entity annotation!");
		}
	}
	
	@Override
	public void addPersistentProperty(ChaincodePersistentProperty property) {
		super.addPersistentProperty(property);
		
		if (property.isTransientProperty()) {
			transientData.put(property.getTransientKey(), null);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context.addPropertyAccessor(new BeanFactoryAccessor());
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		context.setRootObject(applicationContext);
	}

	/**
	 * 解析表达式信息
	 * @author hoojo
	 * @createDate 2018年7月17日 上午11:34:23
	 * @param expressionStrings
	 * @return
	 */
	public String parseExpression(String expressionStrings) {
		// 从表达式中获取设置的值
		Expression expression = parser.parseExpression(expressionStrings, ParserContext.TEMPLATE_EXPRESSION);
		return expression.getValue(context, String.class);
	}

	@Override
	public Map<String, byte[]> transientData() {
		return transientData;
	}
}
