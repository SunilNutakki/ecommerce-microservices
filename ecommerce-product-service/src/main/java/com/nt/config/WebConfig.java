package com.nt.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.images.path}")
    private String imagePath;

    @PostConstruct
    public void init() {
        try {
            // Remove "file:///" prefix ONLY
            String folderPath = imagePath.replace("file:///", "");
            File dir = new File(folderPath);
            if (!dir.exists()) {
                dir.mkdirs();
                System.out.println("📁 Created image directory at: " + dir.getAbsolutePath());
            } else {
                System.out.println("📁 Image directory exists: " + dir.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error initializing image folder: " + e.getMessage());
        }
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///D:/Assets/images/")
                .setCachePeriod(3600);
    }
}
