package io.github.hooj0.springdata.fabric.chaincode.core.convert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.convert.converter.Converter;

/**
 * 时间格式化转换器，在 ConversionService 中可以设置使用
 * @changelog add java date converter string fromat date
 * @author hoojo
 * @createDate 2018年7月11日 上午9:55:03
 * @file DateTimeConverters.java
 * @blog http://hoojo.cnblogs.com
 * @email hoojo_@126.com
 * @version 1.0
 */
public final class DateTimeConverters {

	private static final DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	private DateTimeConverters() {}
	
	public static Collection<Converter<?, ?>> getConvertersToRegister() {

		List<Converter<?, ?>> converters = new ArrayList<>();

		converters.add(JodaDateTimeConverter.INSTANCE);
		converters.add(JodaLocalDateTimeConverter.INSTANCE);
		converters.add(JavaDateConverter.INSTANCE);

		return converters;
	}
	
	public enum JodaDateTimeConverter implements Converter<ReadableInstant, String> {
		INSTANCE;

		@Override
		public String convert(ReadableInstant source) {
			if (source == null) {
				return null;
			}
			return formatter.print(source);
		}
	}

	public enum JodaLocalDateTimeConverter implements Converter<LocalDateTime, String> {
		INSTANCE;

		@Override
		public String convert(LocalDateTime source) {
			if (source == null) {
				return null;
			}
			return formatter.print(source.toDateTime(DateTimeZone.UTC));
		}
	}

	public enum JavaDateConverter implements Converter<Date, String> {
		INSTANCE;

		@Override
		public String convert(Date source) {
			if (source == null) {
				return null;
			}

			return DATE_FORMATTER.format(source.getTime());
		}
	}
}
