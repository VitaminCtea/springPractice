package spring.annotationConfigurationInterBeanDependencies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.annotationConfigurationInterBeanDependencies.AccountRepository;
import spring.annotationConfigurationInterBeanDependencies.TransferServiceImpl;

@Configuration
public class ServiceConfig {
    @Bean public TransferServiceImpl transferService(AccountRepository accountRepository) {
        return new TransferServiceImpl(accountRepository);
    }
}
