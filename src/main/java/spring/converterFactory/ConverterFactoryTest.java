package spring.converterFactory;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class ConverterFactoryTest implements ConverterFactory<String, Enum> {
    @Override public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) { return new StringToEnumConverter(targetType); }
    private final class StringToEnumConverter<T extends Enum<T>> implements Converter<String, T> {
        private final Class<T> enumType;
        public StringToEnumConverter(Class<T> enumType) { this.enumType = enumType; }
        @Override public T convert(String source) { return Enum.valueOf(this.enumType, source.toUpperCase().trim()); }
    }
}
