package spring.testRef;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import spring.validatorTest.DateParametersConsistent;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;

/*
* BeanFactory ApplicationContext Environment ResourceLoader ApplicationEventPublisher MessageSource ConfigurableApplicationContext ResourcePatternResolver
* 这些类将由Spring容器自动注册，无需开发人员进行干预(无需手动配置)
* */
public class Outer {
    @Autowired private ApplicationContext context; // 自动会被注入
    @Autowired private BeanFactory factory;
    @Autowired private Environment environment;
    @Autowired private ResourceLoader loader;
    @Autowired private ApplicationEventPublisher publisher;
    @Autowired private MessageSource messageSource;
    @Autowired private ConfigurableApplicationContext configurableApplicationContext;
    @Autowired private ResourcePatternResolver resourcePatternResolver;
    private Resource resource;

    private Inner target;

    @PostConstruct public void init() throws IOException {
        System.out.println("Outer类执行初始化...");
        System.out.println();
        System.out.println("查看Resource接口的方法：");
        System.out.println("\tContentLength: " + resource.contentLength());
        System.out.println("\tFilename: " + resource.getFilename());
        System.out.println("\tDescription: " + resource.getDescription());
        System.out.println("\tFile: " + resource.getFile());
        System.out.println("\tisFile: " + resource.isFile());
        System.out.println("\texists: " + resource.exists());
        System.out.println("\tURI: " + resource.getURI());
        System.out.println("\tURL: " + resource.getURL());
        System.out.println("\tisOpen: " + resource.isOpen());
        System.out.println("\tisReadable: " + resource.isReadable());
        System.out.println("\tlastModified: " + resource.lastModified());
        System.out.println();
    }
    @PreDestroy public void destroy() { System.out.println("Outer类执行销毁..."); }
    public void print(Object instance) {
        try {
            Class<?> cls = Outer.class;
            for (Field field: cls.getDeclaredFields())
                if (field.isAnnotationPresent(Autowired.class))
                    System.out.println(field.getName() + ": " + field.get(instance).getClass().getCanonicalName());
            System.out.println("Properties文件注入到Spring运行环境，当前testBean.publicInstance值为：" + environment.getProperty("testBean.publicInstance"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        target.print();
    }

    public void setTarget(Inner target) { this.target = target; }

    public void setResource(Resource resource) { this.resource = resource; }

    @DateParametersConsistent(message = "我是你爸")
    public void validationMethodConstraints(Object obj, LocalDate d1, LocalDate d2) {

    }
}
