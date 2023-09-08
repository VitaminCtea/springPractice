package spring.annotationConfigurationInterBeanDependencies;

public class CustomDataSource {
    public CustomDataSource() { System.out.println("初始化：" + this.getClass().getSimpleName()); }
    @Override public String toString() { return "CustomDataSource{}"; }
}
