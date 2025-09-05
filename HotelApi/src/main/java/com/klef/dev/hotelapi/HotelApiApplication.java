package com.klef.dev.hotelapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.klef.dev")
@EntityScan(basePackages = "com.klef.dev.entity")
@EnableJpaRepositories(basePackages = "com.klef.dev.repository")
public class HotelApiApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {
    SpringApplication.run(HotelApiApplication.class, args);
    System.out.println("Running Successfully");
  }
}
