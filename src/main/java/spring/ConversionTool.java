package spring;

public final class ConversionTool {
    public static String hex2binary(String hex) {
        hex = hex.contains("0x") ? hex.substring(2) : hex;
        StringBuilder sb = new StringBuilder();
        final String BINARY_DIGITS = "01";
        final int LENGTH = hex.length(),
                UPPER_LETTER_CODE_POINT = 65,
                LOWER_LETTER_CODE_POINT = 97,
                LOW_DIGIT_CODE_POINT = 48,
                HIGH_DIGIT_CODE_POINT = 57,
                UPPER_LOWER_LETTER_DELTA_CODE_POINT = 32,
                MASK = 1,
                HIGHEST_ORDER = 3,
                BINARY_TOTAL_LENGTH = 4,
                LETTER_BASE = 10;
        for (int i = 0; i < LENGTH; i++) {
            int decimal = hex.charAt(i);
            int upper;
            int result =
                    decimal > LOW_DIGIT_CODE_POINT && decimal < HIGH_DIGIT_CODE_POINT ?
                            decimal - LOW_DIGIT_CODE_POINT :
                            ((upper = decimal - UPPER_LETTER_CODE_POINT) >= UPPER_LOWER_LETTER_DELTA_CODE_POINT ?
                                    decimal - LOWER_LETTER_CODE_POINT : upper) + LETTER_BASE;
            for (int j = 0, carry = (int) (Math.log(result) / Math.log(2)); j < BINARY_TOTAL_LENGTH; j++) {
                int val = 0;
                if (HIGHEST_ORDER - carry - j <= 0) val = result >> carry-- & MASK;
                sb.append(BINARY_DIGITS.charAt(val));
            }
        }

        int leadingZeroFinallyPosition = 0;
        for (;sb.charAt(leadingZeroFinallyPosition) == '0'; leadingZeroFinallyPosition++);

        return sb.substring(leadingZeroFinallyPosition);
    }

    public static String binary2hex(String binary) {
        StringBuilder sb = new StringBuilder();
        final int LENGTH = binary.length(),
                MULTIPLE = 4,
                LETTER_BASE = 10,
                MAX_WIDTH = 3,
                DIGITAL_THRESHOLD = 9,
                UPPER_LETTER_CODE_POINT = 65;

        int finalLength = LENGTH / MULTIPLE;
        if ((LENGTH % MULTIPLE) != 0) {
            binary = fillZero((LENGTH / MULTIPLE + 1) * MULTIPLE - LENGTH, binary);
            finalLength++;
        }
        for (int i = 0; i < finalLength; i++) {
            int n = convertDecimal(
                    binary.substring(i * MULTIPLE, (i + 1) * MULTIPLE),
                    MULTIPLE,
                    MAX_WIDTH
            );
            if (n > DIGITAL_THRESHOLD) sb.append((char) (UPPER_LETTER_CODE_POINT + (n - LETTER_BASE)));
            else sb.append(n);
        }

        return sb.toString();
    }

    public static int binary2decimal(String binary) {
        final int LENGTH = binary.length();
        return convertDecimal(binary, LENGTH, LENGTH - 1);
    }

    private static int convertDecimal(String data, int cycles, int len) {
        int result = 0;
        final char VALID_DIGIT = '1';
        for (int i = 0; i < cycles; i++) {
            char c = data.charAt(len - i);
            if (c == VALID_DIGIT)
                result += (1 << i) * Character.getNumericValue(c);
        }
        return result;
    }

    private static String fillZero(int len, String val) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) sb.append(0);
        sb.append(val);
        return sb.toString();
    }
}
