package org.mukulphougat.userindexerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UserIndexerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserIndexerServiceApplication.class, args);
    }

}
