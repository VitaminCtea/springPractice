package spring.testRef.propertyEditor;

public class ExoticType {
    private final String name;

    public ExoticType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExoticType{" +
                "name='" + name + '\'' +
                '}';
    }
}
