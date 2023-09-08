package spring.factoryMethodComponent;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("private")
@interface PrivateInstance {}

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("publicInstance")
@interface PublicInstance {}

@Component
public class FactoryMethodComponent {

    private static int i;

    @Bean
    @PublicInstance
    public TestBean publicInstance() { return new TestBean("publicInstance"); }

    // use of a custom qualifier and autowiring of method parameters
    @Bean
    protected TestBean protectedInstance(@PrivateInstance TestBean spouse, @Value("#{privateInstance.age}") String country) {
        TestBean tb = new TestBean("protectedInstance", 1);
        tb.setSpouse(spouse);
        tb.setCountry(country);
        return tb;
    }

    @Bean
    @PrivateInstance
    private TestBean privateInstance() { return new TestBean("privateInstance", i++); }
}
