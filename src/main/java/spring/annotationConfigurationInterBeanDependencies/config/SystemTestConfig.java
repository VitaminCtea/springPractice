package spring.annotationConfigurationInterBeanDependencies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import spring.annotationConfigurationInterBeanDependencies.CustomDataSource;

@Configuration
@Import({ ServiceConfig.class, RepositoryConfig.class })
public class SystemTestConfig {
    @Bean public CustomDataSource customDataSource() {
        return new CustomDataSource();
    }
}
