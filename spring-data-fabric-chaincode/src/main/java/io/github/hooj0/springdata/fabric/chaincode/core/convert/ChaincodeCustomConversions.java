package io.github.hooj0.springdata.fabric.chaincode.core.convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;

import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodePersistentProperty.PropertyToFieldNameConverter;
import io.github.hooj0.springdata.fabric.chaincode.core.mapping.ChaincodeSimpleTypeHolder;

/**
 * 自定义类型转换服务
 * @author hoojo
 * @createDate 2018年7月17日 下午5:27:16
 * @file ChaincodeCustomConversions.java
 * @package io.github.hooj0.springdata.fabric.chaincode.core.convert
 * @project spring-data-fabric-chaincode
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public class ChaincodeCustomConversions extends CustomConversions {

	private static final StoreConversions STORE_CONVERSIONS;
	private static final List<Object> STORE_CONVERTERS;

	static {
		List<Object> converters = new ArrayList<>();

		converters.add(CustomToStringConverter.INSTANCE);
		converters.add(PropertyToFieldNameConverter.INSTANCE);
		converters.addAll(ByteStringConverters.getConvertersToRegister());
		converters.addAll(DateTimeConverters.getConvertersToRegister());

		STORE_CONVERTERS = Collections.unmodifiableList(converters);
		// STORE_CONVERSIONS = StoreConversions.of(ChaincodeSimpleTypeHolder.HOLDER, STORE_CONVERTERS);
		STORE_CONVERSIONS = StoreConversions.of(ChaincodeSimpleTypeHolder.DEFAULT, STORE_CONVERTERS);
	}

	public ChaincodeCustomConversions() {
		this(Collections.emptyList());
	}
	
	public ChaincodeCustomConversions(Collection<?> converters) {
		super(STORE_CONVERSIONS, converters);
	}
	
	@WritingConverter
	private enum CustomToStringConverter implements GenericConverter {
		INSTANCE;

		public Set<ConvertiblePair> getConvertibleTypes() {
			ConvertiblePair localeToString = new ConvertiblePair(Locale.class, String.class);
			ConvertiblePair booleanToString = new ConvertiblePair(Character.class, String.class);

			return new HashSet<>(Arrays.asList(localeToString, booleanToString));
		}

		public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			return source != null ? source.toString() : null;
		}
	}
}
