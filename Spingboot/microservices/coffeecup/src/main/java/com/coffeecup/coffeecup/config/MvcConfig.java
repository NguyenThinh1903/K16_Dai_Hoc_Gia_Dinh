package com.coffeecup.coffeecup.config; // Package của Project 1

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // *** ĐƯỜNG DẪN TUYỆT ĐỐI ĐẾN THƯ MỤC LƯU ẢNH CHUNG ***
    private final String productImagesDir = "D:/Documents/K16_Dai_Hoc_Gia_Dinh/Spingboot/microservices/image/products/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path productUploadPath = Paths.get(productImagesDir);
        // Kiểm tra thư mục tồn tại (tùy chọn nhưng nên có)
        if (!Files.exists(productUploadPath)) {
            System.err.println("ERROR: Product image directory specified in MvcConfig does not exist: " + productImagesDir);
            // Có thể không đăng ký handler nếu thư mục không tồn tại
            // return;
        } else if (!Files.isDirectory(productUploadPath)) {
            System.err.println("ERROR: Path specified in MvcConfig is not a directory: " + productImagesDir);
            // return;
        }

        String productUploadPathAbsolute = productUploadPath.toFile().getAbsolutePath();
        System.out.println("Mapping /uploads/products/** to physical path: " + "file:" + productUploadPathAbsolute + "/"); // Thêm log để kiểm tra

        // Map URL /uploads/products/** tới thư mục ảnh vật lý
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations("file:" + productUploadPathAbsolute + "/"); // QUAN TRỌNG: "file:" và "/" ở cuối

        // Phục vụ các tài nguyên trong static của Project 1
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}