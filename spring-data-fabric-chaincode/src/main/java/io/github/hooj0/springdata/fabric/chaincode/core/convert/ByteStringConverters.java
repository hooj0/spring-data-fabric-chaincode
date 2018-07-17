package io.github.hooj0.springdata.fabric.chaincode.core.convert;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import io.github.hooj0.springdata.fabric.chaincode.ChaincodeMappingException;

/**
 * <b>function:</b> byte converter
 * @author hoojo
 * @createDate 2018年7月17日 下午4:06:40
 * @file ByteBufferConverter.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.convert
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public final class ByteStringConverters {

	private ByteStringConverters() {}
	
	public static Collection<Converter<?, ?>> getConvertersToRegister() {

		List<Converter<?, ?>> converters = new ArrayList<>();

		converters.add(ByteToStringConverter.INSTANCE);
		converters.add(StringToByteConverter.INSTANCE);

		return converters;
	}
	
	@WritingConverter
	public enum ByteToStringConverter implements Converter<byte[], String> {
		INSTANCE;

		@Override
		public String convert(byte[] source) {
			if (source == null) {
				return null;
			}

			try {
				return new String(source, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new ChaincodeMappingException("byte to string convert exception", e);
			}
		}
	}
	
	@ReadingConverter
	public enum StringToByteConverter implements Converter<String, byte[]> {
		INSTANCE;

		@Override
		public byte[] convert(String source) {
			if (source == null) {
				return null;
			}

			try {
				return source.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new ChaincodeMappingException("string to byte convert exception", e);
			}
		}
	}
}
