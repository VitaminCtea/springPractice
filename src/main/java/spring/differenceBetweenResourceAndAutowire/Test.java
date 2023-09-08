package spring.differenceBetweenResourceAndAutowire;

import org.springframework.stereotype.Component;

@Component("testDescription")
public class Test implements Description {
    public void description() { System.out.println("我是Test."); }
}
