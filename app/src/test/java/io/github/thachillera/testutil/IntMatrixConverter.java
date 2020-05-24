package io.github.thachillera.testutil;

import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

/**
 * Junit 5 CSV Source converter
 *
 * Expected format example: [1,2,3],[4,5,6],[7,8,9]
 */
public class IntMatrixConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        if (source instanceof String && int[][].class.isAssignableFrom(targetType)) {
            String[] arraySplit = ((String) source).substring(1, ((String) source).length() - 1).split("\\s*]\\s*,\\s*\\[\\s*");

            int intSplitLength = arraySplit[0].split("\\s*,\\s*").length;

            int[][] result = new int[arraySplit.length][intSplitLength];

            for (int i = 0; i < arraySplit.length; i++) {
                String[] intSplit = arraySplit[i].split("\\s*,\\s*");
                for (int j = 0; j < intSplit.length; j++) {
                    result[i][j] = Integer.parseInt(intSplit[j]);
                }
            }

            return result;
        } else {
            throw new IllegalArgumentException("Conversion from " + source.getClass() + " to "
                    + targetType + " not supported.");
        }
    }
}
