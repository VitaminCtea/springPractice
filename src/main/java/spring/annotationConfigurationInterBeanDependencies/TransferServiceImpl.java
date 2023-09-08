package spring.annotationConfigurationInterBeanDependencies;

public class TransferServiceImpl {
    private final AccountRepository repository;
    public TransferServiceImpl(AccountRepository repository) {
        this.repository = repository;
        System.out.println("初始化：" + this.getClass().getSimpleName());
    }
    public void transfer(double amount, String username, String password) {
        System.out.println("转给" + username + ", 金额：" + amount + ", 密码：" + password);
    }
}
