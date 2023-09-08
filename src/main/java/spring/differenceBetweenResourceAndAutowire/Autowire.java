package spring.differenceBetweenResourceAndAutowire;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("customAutowire")
public class Autowire {
    // Autowired注解默认使用类型进行Bean装配
    public Autowire(@Autowired @Qualifier("testDescription") Description test) { test.description(); }

    @PostConstruct public void populateMovieCache() {
        System.out.println("populateMovieCache");
    }
    @PreDestroy public void clearMovieCache() {
        System.out.println("clearMovieCache");
    }
}
