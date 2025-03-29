package com.coffeecup.coffeecup.config; // Package của Project 1

import org.springframework.beans.factory.annotation.Value; // *** BỎ COMMENT IMPORT ***
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // *** BỎ COMMENT @Value VÀ ĐỌC TỪ PROPERTIES ***
    @Value("${app.upload.dir}")
    private String uploadDir; // Biến này sẽ chứa đường dẫn tuyệt đối từ properties

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // *** SỬ DỤNG BIẾN uploadDir ĐÃ ĐỌC TỪ PROPERTIES ***
        Path uploadPath = Paths.get(uploadDir); // Tạo Path từ đường dẫn tuyệt đối
        String uploadAbsolutePath = uploadPath.toFile().getAbsolutePath();

        // Map URL /uploads/products/** tới thư mục ảnh của Project 2
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:" + uploadAbsolutePath + "/"); // Dùng đường dẫn tuyệt đối

        // Phục vụ các tài nguyên trong static của Project 1
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}