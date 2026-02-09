package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.demo.entity")
public class DemoApplication {

	public static void main(String[] args) {
		System.out.println(">>> APP VERSION: 2026-01-26-TEST <<<");
		SpringApplication.run(DemoApplication.class, args);
	}

}
