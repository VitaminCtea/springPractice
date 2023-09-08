package spring.differenceBetweenResourceAndAutowire;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.Objects;
import java.util.Optional;

@Component("customResource")
public class CustomResource {
    // Resource注解默认使用beanName来查找，如果没有使用name属性，则默认使用字段名称进行查找，如果按名字找不到的话，则退化为按Bean类型查找
    // Resource注解是JSR-250注释，是Java规范提案里的东西
    // Resource注解可以用在字段上，也可以用在Setter方法上，用在Setter方法上时，如果没有给Resource注解的name属性赋值的话，默认会使用Setter方法上的参数名字来查找Bean
    // 需要再maven中安装javax.annotation-api包，不能安装jakarta.annotation-api包，这个包不管用
    // 需要注意的是，不能再构造函数中调用字段上的东西，因为在CustomResource构造函数中调用resourceTest.description()会报NPE异常(空指针异常)
    @Resource private Description resourceTest;

    // 以下几种方式都可以做到@Autowire(required = false)注解的功能(即：Bean是可选的)
    // JSR-330 和 JSR-250 注释是不可组合的
    private Provider<Description> providerTest;
    private Optional<Description> optionalTest;
    private Description nullableTest;

    public void callResourceTestDescriptionMethod() { resourceTest.description(); }
    @Bean public Description test() { return new Test(); }

    @Inject
    @Named("resourceTest")
    public void setProviderTest(Provider<Description> providerTest) { this.providerTest = providerTest; }

    @Inject
    @Named("testDescription")
    public void setOptionalTest(Optional<Description> optionalTest) { this.optionalTest = optionalTest; }

    @Inject
    @Named("resourceTest")
    public void setNullableTest(@Nullable Description nullableTest) { this.nullableTest = nullableTest; }

    public void callJSR330ProviderInterface() { callJSR330Interface("Provider", () -> providerTest.get().description()); }
    public void callJSR330OptionalInterface() { callJSR330Interface("Optional", () -> optionalTest.ifPresent(Description::description)); }
    public void callJSR330NullableInterface() { callJSR330Interface("Nullable", () -> { if (Objects.nonNull(nullableTest)) nullableTest.description(); }); }
    private void callJSR330Interface(String type, Runnable runnable) {
        System.out.print("JSR-330 " + type + " 接口 -> ");
        runnable.run();
    }
}
