package spring.annotationConfigurationInterBeanDependencies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.annotationConfigurationInterBeanDependencies.AccountRepository;
import spring.annotationConfigurationInterBeanDependencies.CustomDataSource;

@Configuration
public class RepositoryConfig {
    @Bean public AccountRepository accountRepository(CustomDataSource dataSource) {
        return new AccountRepository(dataSource);
    }
}
