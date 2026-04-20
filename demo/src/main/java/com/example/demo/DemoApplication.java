package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CLASSE PRINCIPALE dell'applicazione Spring Boot.
 * 
 * Avvia tutto il contesto:
 * - server web
 * - dependency injection
 * - configurazioni automatiche
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}