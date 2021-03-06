package ru.digitaluniversity.configuration;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * The type Dispatcher servlet config.
 */
@Configuration
public class DispatcherServletConfig {

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    /**
     * Dispatch servlet registration
     *
     * @return the servlet registration bean
     */
    @Bean
    public ServletRegistrationBean dispatchServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                dispatcherServlet(), "/api/*");
        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        return registration;
    }
}
