package com.coffeecup.coffeecup.config;

import com.coffeecup.coffeecup.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    // Không cần @Autowired UserDetailsServiceImpl ở đây nếu dùng cách @Bean này

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        // Spring sẽ tự inject UserRepository vào đây nếu cần
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Lấy UserDetailsService từ context thông qua method userDetailsService() ở trên
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/", "/home", "/about", "/menu", "/blog", "/shop", "/contact", "/products/**").permitAll()
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/webjars/**").permitAll()
                        .requestMatchers("/login", "/register").permitAll()
                        // Chỉ định các URL cần quyền ADMIN (ví dụ cho project 2 sau này)
                        // .requestMatchers("/manage/**", "/admin/**").hasRole("ADMIN")
                        // Chỉ định các URL cần đăng nhập (bất kỳ role nào)
                        // .requestMatchers("/profile/**", "/cart/**", "/order/**").authenticated()
                        .anyRequest().authenticated() // Mọi request khác cần đăng nhập
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                // Quan trọng: Thêm authenticationProvider
                .authenticationProvider(authenticationProvider());

        // Bỏ comment dòng dưới nếu bạn muốn disable CSRF tạm thời để test (không khuyến khích cho production)
        // .csrf(csrf -> csrf.disable());

        return http.build();
    }
}