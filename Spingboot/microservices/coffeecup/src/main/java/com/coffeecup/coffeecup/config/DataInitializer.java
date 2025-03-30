package com.coffeecup.coffeecup.config;

import com.coffeecup.coffeecup.model.Authority;
import com.coffeecup.coffeecup.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    @Transactional // Đảm bảo thao tác trong một transaction
    public void run(String... args) throws Exception {
        log.info("Checking for initial roles...");

        if (authorityRepository.findByName("ROLE_USER").isEmpty()) {
            authorityRepository.save(new Authority("ROLE_USER"));
            log.info("Initialized ROLE_USER");
        } else {
            log.info("ROLE_USER already exists.");
        }

        if (authorityRepository.findByName("ROLE_ADMIN").isEmpty()) {
            authorityRepository.save(new Authority("ROLE_ADMIN"));
            log.info("Initialized ROLE_ADMIN");
        } else {
            log.info("ROLE_ADMIN already exists.");
        }

        // Có thể tạo user Admin mẫu ở đây nếu cần cho Project 2
        // Ví dụ:
        // if (userRepository.findByUsername("admin").isEmpty()) {
        //    User adminUser = new User();
        //    adminUser.setUsername("admin");
        //    adminUser.setPassword(passwordEncoder.encode("admin123")); // Nhớ inject PasswordEncoder
        //    adminUser.setEnabled(true);
        //    Authority adminRole = authorityRepository.findByName("ROLE_ADMIN").get();
        //    adminUser.setAuthorities(Collections.singleton(adminRole));
        //    userRepository.save(adminUser);
        //    log.info("Initialized sample ADMIN user.");
        // }
    }
}