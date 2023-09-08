package spring.testRef;


import org.springframework.stereotype.Component;

@Component("testRefTest")
public class Test {
    public void print() {
        System.out.println(Test.class.getCanonicalName());
    }
}
