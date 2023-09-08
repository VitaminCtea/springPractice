package spring.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CustomContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("ContextLoaderListener Initialized!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ContextLoaderListener Destroyed!");
    }
}
