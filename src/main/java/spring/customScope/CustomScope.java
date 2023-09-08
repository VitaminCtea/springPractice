package spring.customScope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class CustomScope implements Scope {
    private final static ThreadLocal<Object> THREAD_LOCAL = new ThreadLocal<>();

    @Override public Object get(String name, ObjectFactory<?> objectFactory) {
        Object val = THREAD_LOCAL.get();
        if (val != null) return val;
        Object obj = objectFactory.getObject();
        THREAD_LOCAL.set(obj);
        return obj;
    }

    @Override public Object remove(String name) {
        THREAD_LOCAL.remove();
        return null;
    }

    @Override public void registerDestructionCallback(String name, Runnable callback) {
        System.out.println("销毁啦");
    }

    @Override public Object resolveContextualObject(String key) { return null; }

    @Override public String getConversationId() { return "CustomScope"; }
}
