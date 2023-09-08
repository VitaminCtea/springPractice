package spring.annotationConfigurationInterBeanDependencies;

public class AccountRepository {
    private final CustomDataSource source;
    public AccountRepository(CustomDataSource source) {
        this.source = source;
        System.out.println("初始化：" + this.getClass().getSimpleName());
    }
}
