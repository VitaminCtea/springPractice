package spring.testRef.propertyEditor;

public class DependsOnExoticType {
    private ExoticType exoticType;
    public void setExoticType(ExoticType exoticType) {
        this.exoticType = exoticType;
    }

    @Override public String toString() { return exoticType.toString(); }
}
