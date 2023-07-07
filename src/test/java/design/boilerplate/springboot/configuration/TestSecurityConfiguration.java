package design.boilerplate.springboot.configuration;

import design.boilerplate.springboot.security.jwt.JwtAuthenticationEntryPoint;
import design.boilerplate.springboot.security.jwt.JwtProperties;
import design.boilerplate.springboot.security.jwt.JwtTokenManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(JwtProperties.class)
public class TestSecurityConfiguration {

    @Bean
    public JwtTokenManager tokenManager(JwtProperties properties){
        return new JwtTokenManager(properties);
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(){
        return new JwtAuthenticationEntryPoint();
    }
}
