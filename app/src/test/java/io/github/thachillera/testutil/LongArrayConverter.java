package io.github.thachillera.testutil;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class LongArrayConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        if (source instanceof String && long[].class.isAssignableFrom(targetType)) {
            String[] split = ((String) source).split("\\s*,\\s*");
            long[] result = new long[split.length];

            for (int i = 0; i < split.length; i++) {
                result[i] = Long.parseLong(split[i]);
            }

            return result;
        } else {
            throw new IllegalArgumentException("Conversion from " + source.getClass() + " to "
                    + targetType + " not supported.");
        }
    }
}
