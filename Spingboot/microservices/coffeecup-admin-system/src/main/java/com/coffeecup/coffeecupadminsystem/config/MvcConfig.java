package com.coffeecup.coffeecupadminsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Đường dẫn tương đối đến thư mục 'image/products' từ gốc project
        String relativeUploadDir = "image/products";
        Path uploadPath = Paths.get(relativeUploadDir);
        String uploadAbsolutePath = uploadPath.toFile().getAbsolutePath();

        // Map URL /uploads/products/** tới thư mục image/products/ trong gốc project
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:" + uploadAbsolutePath + "/");

        // --- QUAN TRỌNG: Thêm dòng này để phục vụ các tài nguyên trong static ---
        // Nếu không có dòng này, CSS, JS trong static có thể không được load đúng
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}