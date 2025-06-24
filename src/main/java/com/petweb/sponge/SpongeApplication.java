package com.petweb.sponge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpongeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpongeApplication.class, args);
    }

}
