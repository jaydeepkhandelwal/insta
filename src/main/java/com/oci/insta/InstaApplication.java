package com.oci.insta;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration(value = "com.oci.insta")
@ComponentScan(value = "com.oci.insta")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class},scanBasePackages = "com.oci.insta")
@EnableAutoConfiguration
public class InstaApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(InstaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}
