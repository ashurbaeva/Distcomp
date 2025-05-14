package com.example.rv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.example.rv1")
//@EnableJpaAuditing
//@EnableJpaRepositories(basePackages = "com.example.rv1.repositories")
public class Rv1Application {

    public static void main(String[] args) {
        System.out.println("Program started");
        SpringApplication.run(Rv1Application.class, args);
    }

}
