package spring.randomEnum;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static spring.ConsoleOutputController.*;

public class NoRepeatRandomColor implements RangeRandomColor {
    private final Set<String> foregroundColorSet = new HashSet<>();
    private final Set<String> backgroundColorSet = new HashSet<>();
    private final RangeRandomColor randomColor = new RandomColor();

    public ForegroundColor generatorRandomForegroundColor(int begin) {
        return (ForegroundColor) generator(begin, index -> randomColor.generatorRandomForegroundColor(begin), foregroundColorSet);
    }

    @Override public BackGroundColor generatorRandomBackgroundColor(int begin) {
        return (BackGroundColor) generator(begin, index -> randomColor.generatorRandomBackgroundColor(begin), backgroundColorSet);
    }

    public Color generator(int begin, Function<Integer, Color> func, Set<String> colorSet) {
        Color color = func.apply(begin);
        String hashCode = color.toString();

        if (colorSet.contains(hashCode)) {
            return generator(begin, func, colorSet);
        } else colorSet.add(hashCode);

        if (isNeedClearSet(foregroundColorSet, begin, ForegroundColor.values())) clear(foregroundColorSet);
        if (isNeedClearSet(backgroundColorSet, begin, BackGroundColor.values())) clear(backgroundColorSet);

        return color;
    }

    private boolean isNeedClearSet(Set<String> colorSet, int begin, Enum<? extends Color>[] colors) { return colorSet.size() > colors.length - begin; }
    private void clear(Set<String> colorSet) { colorSet.clear(); }
}
