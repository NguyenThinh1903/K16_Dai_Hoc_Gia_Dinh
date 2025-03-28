package com.coffeecup.coffeecupadminsystem.config; // Sửa package nếu cần

import com.coffeecup.coffeecupadminsystem.model.Authority; // Sửa import nếu cần
import com.coffeecup.coffeecupadminsystem.model.User; // Sửa import nếu cần
import com.coffeecup.coffeecupadminsystem.repository.AuthorityRepository; // Sửa import nếu cần
import com.coffeecup.coffeecupadminsystem.repository.UserRepository; // Sửa import nếu cần
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Initializing roles and default users for Admin System...");

        // --- Đảm bảo Roles tồn tại ---
        Authority roleAdmin = authorityRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    log.info("Creating ROLE_ADMIN");
                    return authorityRepository.save(new Authority("ROLE_ADMIN"));
                });

        Authority roleSystem = authorityRepository.findByName("ROLE_SYSTEM")
                .orElseGet(() -> {
                    log.info("Creating ROLE_SYSTEM");
                    return authorityRepository.save(new Authority("ROLE_SYSTEM"));
                });

        // Đảm bảo ROLE_USER cũng tồn tại (do dùng chung DB)
        authorityRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    log.info("Creating ROLE_USER");
                    return authorityRepository.save(new Authority("ROLE_USER"));
                });


        // --- Tạo User Admin mẫu nếu chưa có ---
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123")); // Đặt mật khẩu mặc định
            adminUser.setEnabled(true);
            adminUser.setAuthorities(Collections.singleton(roleAdmin)); // Gán ROLE_ADMIN
            userRepository.save(adminUser);
            log.info("Created default ADMIN user with username 'admin' and password 'admin123'");
        } else {
            log.info("Admin user 'admin' already exists.");
        }

        // --- Tạo User System mẫu nếu chưa có ---
        if (userRepository.findByUsername("system").isEmpty()) {
            User systemUser = new User();
            systemUser.setUsername("system");
            systemUser.setPassword(passwordEncoder.encode("system123")); // Đặt mật khẩu mặc định
            systemUser.setEnabled(true);
            systemUser.setAuthorities(Collections.singleton(roleSystem)); // Gán ROLE_SYSTEM
            userRepository.save(systemUser);
            log.info("Created default SYSTEM user with username 'system' and password 'system123'");
        } else {
            log.info("System user 'system' already exists.");
        }
    }
}