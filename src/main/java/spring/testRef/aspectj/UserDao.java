package spring.testRef.aspectj;

import org.springframework.stereotype.Component;

@Component
public class UserDao {
    public String show() {
        System.out.println("show running...");
        return "show method";
    }

    public String showNext() {
        System.out.println("showNext running...");
        return "showNext method";
    }
}
