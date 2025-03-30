package com.coffeecup.coffeecup.config; // *** Package của Project 1 ***

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // *** Đặt đường dẫn TUYỆT ĐỐI chính xác đến thư mục chứa ảnh của Project 2 ***
    // Đảm bảo dùng dấu gạch chéo đúng cho hệ điều hành của bạn hoặc dùng '/'
    private final String productImagesDir = "D:/Documents/K16_Dai_Hoc_Gia_Dinh/Spingboot/coffeecup-admin-system/image/products/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // --- Cấu hình phục vụ ảnh Products từ thư mục vật lý ---
        Path productUploadPath = Paths.get(productImagesDir);
        String productUploadPathAbsolute = productUploadPath.toFile().getAbsolutePath();

        // Map URL /uploads/products/** tới thư mục ảnh của Project 2
        // Quan trọng: "file:" và dấu "/" ở cuối đường dẫn thư mục vật lý
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:" + productUploadPathAbsolute + "/");

        // --- QUAN TRỌNG: Phục vụ các tài nguyên trong static của Project 1 ---
        // Để đảm bảo CSS, JS, images của chính Project 1 vẫn hoạt động
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}