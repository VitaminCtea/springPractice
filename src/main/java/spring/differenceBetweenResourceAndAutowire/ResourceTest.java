package spring.differenceBetweenResourceAndAutowire;

import org.springframework.stereotype.Component;

@Component
public class ResourceTest implements Description {
    public void description() { System.out.println("我是ResourceTest."); }
}
