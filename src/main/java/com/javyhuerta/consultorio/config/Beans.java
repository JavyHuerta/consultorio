package com.javyhuerta.consultorio.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class Beans {


    @Bean
    public Faker fakerBean(){
        return new Faker(Locale.forLanguageTag("es"));
    }

}

