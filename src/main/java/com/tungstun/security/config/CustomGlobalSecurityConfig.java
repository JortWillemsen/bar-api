package com.tungstun.security.config;

import com.tungstun.security.config.evaluator.BarApiPermissionEvaluator;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class CustomGlobalSecurityConfig extends GlobalMethodSecurityConfiguration {

    public CustomGlobalSecurityConfig() { }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler =
                new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new BarApiPermissionEvaluator());
        return expressionHandler;
    }

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(
            prePostEnabled = true,
            securedEnabled = true,
            jsr250Enabled = true)
    public static class EmbeddedWebWebSecurityConfigurerAdapter extends WebSecurityConfig { }
}
