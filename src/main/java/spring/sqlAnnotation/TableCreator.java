package spring.sqlAnnotation;

import spring.randomEnum.NoRepeatRandomColor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

import static spring.ConsoleOutputController.RangeRandomColor;
import static spring.ConsoleOutputController.generatorColorText;

abstract class AnnotationStrategy {
    protected String getConstraints(Constraints constraints) {
        StringBuilder sb = new StringBuilder();
        if (!constraints.allowNull()) sb.append(" NOT NULL");
        if (constraints.primaryKey()) sb.append(" PRIMARY KEY");
        if (constraints.unique()) sb.append(" UNIQUE");
        return sb.toString();
    }
    public abstract String createSQL(String fieldName, Annotation annotation);
}

class SQLIntegerAnnotationStrategy extends AnnotationStrategy {
    @Override public String createSQL(String fieldName, Annotation annotation) {
        return fieldName + " INT" + getConstraints(((SQLInteger) annotation).constraints());
    }
}

class SQLStringAnnotationStrategy extends AnnotationStrategy {
    @Override public String createSQL(String fieldName, Annotation annotation) {
        return fieldName + " VARCHAR(" + ((SQLString) annotation).value() + ")" + getConstraints(((SQLString) annotation).constraints());
    }
}

class ContextAnnotationStrategy {
    public static final Map<Class<? extends Annotation>, AnnotationStrategy> annotationStrategy = new HashMap<>();
    static {
        annotationStrategy.put(SQLString.class, new SQLStringAnnotationStrategy());
        annotationStrategy.put(SQLInteger.class, new SQLIntegerAnnotationStrategy());
    }
    private ContextAnnotationStrategy() throws IllegalAccessException { throw new IllegalAccessException("This is a Context policy class"); }
    public static String createSQL(Annotation annotation, String fieldName) {
        return annotationStrategy.get(annotation.annotationType()).createSQL(fieldName, annotation);
    }
}

public class TableCreator {
    public static void main(String[] args) throws IOException {
        String sql = create(Member.class);
        String[] sqlArray;

        makeSqlWriteToFile(sqlArray = sql.split("\n"), "C:/Users/jiazh/Desktop/SQL.txt");

        RangeRandomColor rangeRandomColor = new NoRepeatRandomColor();
        CustomStringJoiner<String> customStringJoiner =
                new CustomStringJoiner<String>("\n", content -> generatorColorText(content, rangeRandomColor.generatorRandomForegroundColor(1)))
                        .add(sqlArray, "");
        System.out.println(customStringJoiner);
    }

    public static String create(Class<?> tableClass) {
        return new CustomStringJoiner<Field>(",", field -> ContextAnnotationStrategy.createSQL(field.getDeclaredAnnotations()[0], field.getName()))
                .addPrefix("CREATE TABLE ")
                .addPrefix(getTableName(tableClass))
                .addPrefix("(")
                .addSuffix("\n);")
                .add(tableClass.getDeclaredFields(), "\n    ")
                .toString();
    }

    private static String getTableName(Class<?> beanClass) {
        DBTable tableAnnotation = beanClass.getAnnotation(DBTable.class);
        String tableName = tableAnnotation.name();
        return tableName.isEmpty() ? beanClass.getSimpleName() : tableName;
    }

    private static void makeSqlWriteToFile(String[] sqlArray, String path) throws IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path), determineBufferSize(sqlArray)));
        for (String sqlStatement: sqlArray) writer.println(sqlStatement);
        writer.close();
    }

    private static int determineBufferSize(String[] sqlArray) {
        int sizeByte = 0, len = sqlArray.length;
        for (int i = 0; i < len; sizeByte = Math.max(sqlArray[i].length() * 2, sizeByte), i++);
        return sizeByte;
    }

    private static class CustomStringJoiner<T> {
        private final String delimiter;
        private final StringBuilder content = new StringBuilder();
        private final StringBuilder prefix = new StringBuilder();
        private final StringBuilder suffix = new StringBuilder();
        private final Pattern pattern = Pattern.compile("^\\p{javaWhitespace}++$");
        private final Function<T, CharSequence> processor;

        public CustomStringJoiner(String delimiter) { this(delimiter, null); }
        public CustomStringJoiner(String delimiter, Function<T, CharSequence> processor) {
            this.delimiter = delimiter;
            this.processor = processor;
        }

        public CustomStringJoiner<T> addPrefix(CharSequence prefix) {
            this.prefix.append(prefix);
            return this;
        }

        public CustomStringJoiner<T> addSuffix(CharSequence suffix) {
            this.suffix.append(suffix);
            return this;
        }

        public CustomStringJoiner<T> add(CharSequence content) {
            this.content.append(content);
            if (isWhiteSpace(content)) this.content.append(delimiter);
            return this;
        }

        public CustomStringJoiner<T> add(CharSequence[] sequences) {
            for (CharSequence sequence: sequences) add(sequence);
            return this;
        }

        public CustomStringJoiner<T> add(T[] contents, CharSequence contentPrefix) {
            for (T content: contents) add(contentPrefix).add(processor != null ? processor.apply(content) : content.toString());
            return this;
        }

        @Override public String toString() { return prefix + content.substring(0, content.length() - 1) + suffix; }
        private boolean isWhiteSpace(CharSequence content) {
            return Objects.nonNull(content) && content.length() > 0 && !pattern.matcher(content).matches();
        }
    }
}
