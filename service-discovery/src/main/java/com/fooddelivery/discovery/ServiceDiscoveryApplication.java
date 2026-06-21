package com.fooddelivery.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceDiscoveryApplication.class, args);
        System.out.println("✅ Eureka Service Discovery Server started on port 8761");
        System.out.println("🔗 Access Eureka Dashboard: http://localhost:8761");
    }
}