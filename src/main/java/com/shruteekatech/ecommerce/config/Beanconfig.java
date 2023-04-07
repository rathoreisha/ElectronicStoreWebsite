package com.shruteekatech.ecommerce.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beanconfig {
    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }
}
