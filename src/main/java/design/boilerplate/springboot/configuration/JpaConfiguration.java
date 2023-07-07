package design.boilerplate.springboot.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"design.boilerplate.springboot.repository.**"})
public class JpaConfiguration {
}
