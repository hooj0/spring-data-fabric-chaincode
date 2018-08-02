package io.github.hooj0.springdata.fabric.chaincode.repository.query.parser;

import lombok.ToString;

/**
 * Chaincode String Query 返回结果封装
 * @changelog Chaincode Query of type String result wrapper 
 * @author hoojo
 * @createDate 2018年7月14日 下午6:36:30
 * @file SimpleStatement.java
 * @package io.github.hooj0.springdata.template.repository.query.complex
 * @project spring-data-template
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
@ToString
public class SimpleStatement {
	
	private String bindableStatement;
	private Object[] array;

	public SimpleStatement(String bindableStatement) {
		this.bindableStatement = bindableStatement;
	}

	public SimpleStatement(String bindableStatement, Object[] array) {
		this.bindableStatement = bindableStatement;
		this.array = array;
	}

	public String getBindableStatement() {
		return bindableStatement;
	}

	public Object[] getArray() {
		return array;
	}
}
