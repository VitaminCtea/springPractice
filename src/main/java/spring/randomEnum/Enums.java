package spring.randomEnum;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Enums {
    private static final Random RANDOM = ThreadLocalRandom.current();
    public static <T extends Enum<T>> T random(Class<T> cls) { return random(cls.getEnumConstants()); }
    public static <T> T random(T[] values) { return random(0, values.length, values); }
    public static <T> T random(int begin, int end, T[] values) {
        if (begin < 0) begin = -begin;
        if (end < 0) end = -end;
        if (begin > end) begin = (begin - end) % values.length;
        if (end > values.length) end = values.length;
        return values[begin + RANDOM.nextInt(end - begin)];
    }
}
