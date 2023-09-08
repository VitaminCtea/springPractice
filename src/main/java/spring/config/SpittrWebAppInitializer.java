package spring.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/*
* 对于使用基于Java的Spring配置的应用程序public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer { ... }
* public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { MyWebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
*
*
* 如果使用基于XML的Spring配置，则应直接从 AbstractDispatcherServletInitializer 扩展 AbstractDispatcherServletInitializer
*
public class MyWebAppInitializer extends AbstractDispatcherServletInitializer {

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        XmlWebApplicationContext cxt = new XmlWebApplicationContext();
        cxt.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");
        return cxt;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
*
* AbstractDispatcherServletInitializer还可以添加Filter
* public class MyWebAppInitializer extends AbstractDispatcherServletInitializer {

    // ...

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {
            new HiddenHttpMethodFilter(), new CharacterEncodingFilter() };
    }
}
* */
//public class SpittrWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//    @Override protected Class<?>[] getRootConfigClasses() { return new Class<?>[] { RootConfig.class }; }
//    @Override protected Class<?>[] getServletConfigClasses() { return new Class<?>[] { WebConfig.class }; }
//    @Override protected String[] getServletMappings() { return new String[] { "/" }; }
//
//    @Override public void onStartup(ServletContext context) throws ServletException {}
//}
