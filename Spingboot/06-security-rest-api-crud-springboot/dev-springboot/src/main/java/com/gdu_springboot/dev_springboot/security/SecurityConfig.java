package com.gdu_springboot.dev_springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        UserDetails user1 = User.builder().username("user1").password("{noop}123").roles("STUDENT").build();
//        UserDetails user2 = User.builder().username("user2").password("{noop}123").roles("STUDENT","MANAGER").build();
//        UserDetails user3 = User.builder().username("user3").password("{noop}123").roles("STUDENT","MANAGER","ADMIN").build();
//        return new InMemoryUserDetailsManager(user1, user2, user3);
//    }
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        userDetailsManager.setUsersByUsernameQuery("select * from users where username=?");
        userDetailsManager.setAuthoritiesByUsernameQuery("select * from authorities where username=?");
        return  userDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer->configurer
                .requestMatchers(HttpMethod.GET,"api/students").hasRole("STUDENT")
                .requestMatchers(HttpMethod.GET,"api/students/**").hasRole("STUDENT")
                .requestMatchers(HttpMethod.POST,"api/students").hasRole("MANAGER")
                .requestMatchers(HttpMethod.PUT, "api/students").hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "api/students/**").hasRole("ADMIN")
        );
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf->csrf.disable());
        return http.build();
    }
}
