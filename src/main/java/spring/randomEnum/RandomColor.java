package spring.randomEnum;
import static spring.ConsoleOutputController.*;

public class RandomColor implements RangeRandomColor {
    public ForegroundColor generatorRandomForegroundColor() {
        return generatorRandomForegroundColor(0);
    }
    public ForegroundColor generatorRandomForegroundColor(int begin) {
        return (ForegroundColor) generator(begin, ForegroundColor.values());
    }
    public BackGroundColor generatorRandomBackgroundColor() {
        return generatorRandomBackgroundColor(0);
    }
    public BackGroundColor generatorRandomBackgroundColor(int begin) {
        return (BackGroundColor) generator(begin, BackGroundColor.values());
    }
    private Color generator(int begin, Enum<? extends Color>[] colors) {
        return (Color) Enums.random(begin, colors.length, colors);
    }
}
