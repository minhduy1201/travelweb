package com.travelweb.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dm3osob4x", 
                "api_key", "451462356355158",      
                "api_secret", "Axg66YsqYkKlcGoVySODquJ9hzw" 
        ));
    }
}

