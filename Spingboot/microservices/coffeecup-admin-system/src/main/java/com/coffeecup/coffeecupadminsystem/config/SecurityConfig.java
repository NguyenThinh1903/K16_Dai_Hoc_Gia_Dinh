package com.coffeecup.coffeecupadminsystem.config;

import com.coffeecup.coffeecupadminsystem.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/manage/login").permitAll() // Login admin
                        // *** CHO PHÉP API GET CÔNG KHAI ***
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()
                        // *** CÁC API KHÁC CẦN QUYỀN ADMIN ***
                        .requestMatchers("/api/**").hasRole("ADMIN") // Ví dụ: Mọi API khác cần ADMIN
                        // Phân quyền trang quản trị
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/system/**").hasRole("SYSTEM")
                        .anyRequest().hasAnyRole("ADMIN", "SYSTEM") // Các trang khác của admin/system
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login") // Giữ nguyên
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true") // Giữ nguyên
                        .permitAll() // Vẫn giữ permitAll ở đây
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider());
        // .csrf(csrf -> csrf.disable());

        return http.build();
    }
}