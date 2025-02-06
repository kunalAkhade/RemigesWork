package com.example.advancedjava.helper;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomStringGenerator {


    @Bean
    public String generate(){
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<10;i++){
            Random random=new Random();
            int randomNumber=random.nextInt(26)+97;
            builder.append((char)randomNumber);
        }
        return builder.toString();
    }
}
