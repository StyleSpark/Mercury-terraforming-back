package com.matdongsan.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MatdongsanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatdongsanApiApplication.class, args);
	}

}
