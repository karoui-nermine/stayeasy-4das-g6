package com.stayeasy.colocation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ColocationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColocationApplication.class, args);
    }
}
