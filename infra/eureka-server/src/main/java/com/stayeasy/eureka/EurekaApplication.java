package com.stayeasy.eureka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@SpringBootApplication @EnableEurekaServer
public class EurekaApplication { public static void main(String[] a){ SpringApplication.run(EurekaApplication.class,a); } }
