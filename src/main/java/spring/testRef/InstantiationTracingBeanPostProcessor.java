package spring.testRef;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {
    private boolean isInitialized;  // 抑制，只观察一个Bean实例化的前后操作
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!isInitialized) print("postProcessBeforeInitialization", bean, beanName);
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!isInitialized) {
            print("postProcessAfterInitialization", bean, beanName);
            isInitialized = true;
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    private void print(String desc, Object bean, String beanName) {
        System.out.printf("%s, bean is: %s, beanName is: %s", desc, bean, beanName);
        System.out.println();
    }
}
