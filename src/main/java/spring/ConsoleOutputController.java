package spring;

// \033[显示方式;前景色;背景色m...placeholder(%s等等)\033[0m
public final class ConsoleOutputController {
    public interface Config { int getCode(); }
    public interface StyleConfig extends Config { String getDescribe(); }
    public interface Color extends StyleConfig {}
    public interface NonStyleConfig extends Config {}

    public interface RangeRandomColor {
        ForegroundColor generatorRandomForegroundColor(int begin);
        BackGroundColor generatorRandomBackgroundColor(int begin);
    }

    public enum DisplayMode implements StyleConfig {
        DEFAULT(0, "默认值"), HIGHLIGHT(1, "高亮"), NON_BOLD(22, "非粗体"), UNDERLINE(4, "下划线"),
        NON_UNDERLINE(24, "非下划线"), FLICKER(5, "闪烁"), NON_FLICKER(25, "非闪烁"), ANTI_SHOW(7, "反显"), NON_ANTI_SHOW(27, "非反显");
        private final int code;
        private final String describe;

        DisplayMode(int code, String describe) {
            this.code = code;
            this.describe = describe;
        }

        @Override public int getCode() { return code; }
        @Override public String getDescribe() { return describe; }
    }

    public enum ForegroundColor implements Color {
        BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE;
        private static final String[] DESCRIBE_LIST = { "黑色", "红色", "绿色", "黄色", "蓝色", "洋红", "青色", "白色" };
        @Override public int getCode() { return 30 + ordinal(); }
        @Override public String getDescribe() { return DESCRIBE_LIST[ordinal()]; }
    }

    public enum BackGroundColor implements Color {
        BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE;
        private static final ForegroundColor[] FOREGROUND_COLORS = ForegroundColor.values();
        @Override public int getCode() { return FOREGROUND_COLORS[ordinal()].getCode() + 10; }
        @Override public String getDescribe() { return FOREGROUND_COLORS[ordinal()].getDescribe(); }
    }

    public static NonStyle getNonStyleInstance() { return NonStyle.NON_STYLE; }

    public static class NonStyle implements NonStyleConfig {
        private static final NonStyle NON_STYLE = new NonStyle();
        @Override public int getCode() { return 1; }
    }

    public static <T> String generatorColorText(T content, ForegroundColor fgColor) {
        return generatorBaseStyleText(content, getNonStyleInstance(), fgColor, getNonStyleInstance());
    }

    public static <T> String generatorFontTypeText(T content, DisplayMode displayMode) {
        return generatorBaseStyleText(content, displayMode, getNonStyleInstance(), getNonStyleInstance());
    }

    public static <T> String generatorBackgroundColorText(T content, BackGroundColor bgColor) {
        return generatorBaseStyleText(content, getNonStyleInstance(), getNonStyleInstance(), bgColor);
    }

    public static <T> String generatorFontTypeAndForegroundColorText(T content, DisplayMode displayMode, ForegroundColor fgColor) {
        return generatorBaseStyleText(content, displayMode, fgColor, getNonStyleInstance());
    }

    public static <T> String generatorForegroundAndBackgroundColorText(T content, ForegroundColor fg, BackGroundColor bg) {
        return generatorBaseStyleText(content, getNonStyleInstance(), fg, bg);
    }

    private static <T> String generatorBaseStyleText(T content, Config displayMode, Config fgColor, Config bgColor) {
        int dmCode = displayMode.getCode();
        int fgCode = fgColor.getCode();
        int bgCode = bgColor.getCode();

        if (dmCode == 0 || (dmCode | fgCode | bgCode) == 1) return content.toString();

        int[] controlCodeList = { dmCode, fgCode, bgCode };

        String finalPlaceholder = removeRedundantSemicolons(generatePlaceholder(controlCodeList));
        Object[] actualParameters = buildActualParameters(content, controlCodeList);
        String finalFormattedExpression = buildFinalFormattedExpression(finalPlaceholder);

        return String.format(finalFormattedExpression, actualParameters);
    }

    private static String generatePlaceholder(int[] controlCodeList) {
        StringBuilder placeholder = new StringBuilder();
        for (int code: controlCodeList) {
            if (shouldNotNonStyle(code)) placeholder.append("%d");
            placeholder.append(";");
        }
        return placeholder.toString();
    }

    private static String removeRedundantSemicolons(String placeholder) {
        char[] placeholderCharArray = placeholder.toCharArray();
        int endIndex = placeholderCharArray.length - 1;
        for (int i = endIndex; i >= 0; i--) {
            if (shouldSpecifiedCharacter(placeholderCharArray, i, ';')) {
                if (shouldSpecifiedCharacter(placeholderCharArray, i - 1, 'd')) break;
                endIndex--;
            }
        }

        return placeholder.substring(0, endIndex);
    }

    private static <T> Object[] buildActualParameters(T content, int[] controlCodeList) {
        Object[] actualParameters = new Object[3];
        int index = 0;
        for (int controlCode: controlCodeList)
            if (shouldNotNonStyle(controlCode)) actualParameters[index++] = controlCode;
        actualParameters[index] = content.toString();
        return actualParameters;
    }

    private static boolean shouldNotNonStyle(int code) { return code != getNonStyleInstance().getCode(); }
    private static boolean shouldSpecifiedCharacter(char[] placeholderCharArray, int index, char c) {
        return placeholderCharArray[index] == c;
    }
    private static String buildFinalFormattedExpression(String placeholder) { return "\033[" + placeholder + "m%s\033[0m"; }
}
