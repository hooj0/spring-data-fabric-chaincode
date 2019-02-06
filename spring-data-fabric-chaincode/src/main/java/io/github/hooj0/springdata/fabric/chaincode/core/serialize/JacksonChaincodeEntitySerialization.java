package io.github.hooj0.springdata.fabric.chaincode.core.serialize;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.hooj0.springdata.fabric.chaincode.ChaincodeSerializationException;
import io.github.hooj0.springdata.fabric.chaincode.repository.query.ChaincodeQueryMethod;

/**
 * jackson chaincode entity serialization support
 * @author hoojo
 * @createDate 2018年8月13日 下午4:12:57
 * @file JacksonChaincodeEntitySerialization.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.serialize
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public enum JacksonChaincodeEntitySerialization implements ChaincodeEntitySerialization {

	INSTANCE;
	
	private final static ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
	}
	
	@Override
	public <T> String serialize(T entity) {
		
		StringWriter writer = new StringWriter();
		try {
			mapper.writeValue(writer, entity);
		} catch (IOException e) {
			throw new ChaincodeSerializationException(e, "jackson chaincode entity serialize exception: %s", e.getMessage());
		}
		return writer.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(String json, ChaincodeQueryMethod method) {
		
		/*
		System.err.println("1->" + method.getReturnType().getActualType());
		System.err.println("4->" + method.getResultType());
		System.err.println("5->" + method.getReturnedObjectType());
		System.err.println("b2->" + method.getReturnType().getActualType().getType());
		System.err.println("e->" + method.getResultProcessor().getReturnedType().getReturnedType());
		System.out.println("type ->" + method.getReturnType().getRawTypeInformation().getType());
		
		1->com.hoojo.transaction.chaincode.article.entity.ArticleEntity
		4->class com.hoojo.transaction.chaincode.article.entity.ArticleEntity
		5->class com.hoojo.transaction.chaincode.article.entity.ArticleEntity
		b2->class com.hoojo.transaction.chaincode.article.entity.ArticleEntity
		e->class com.hoojo.transaction.chaincode.article.entity.ArticleEntity
		type ->interface java.util.List
		 */
		
		try {
			if (method.isCollectionQuery()) {
				return mapper.readValue(json, getCollectionType(method.getReturnType().getRawTypeInformation().getType(), method.getResultType()));
			} 
			
			return (T) mapper.readValue(json, method.getResultType());
		} catch (IOException e) {
			throw new ChaincodeSerializationException(e, "jackson chaincode entity deserialize exception: %s", e.getMessage());
		}
	}
	
	private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
}
