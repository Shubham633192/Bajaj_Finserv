package com.example.demo;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner run() {
		return args -> {

			try {
				// Creating a WebClient
				WebClient client = WebClient.create();

				// Generating a Webhook
				Map<String, String> req = Map.of(
						"name", "Shubham Mishra",
						"regNo", "REG12347",
						"email", "shubhampappumishra2000@gmail.com");

				Map response = client.post()
						.uri("https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA")
						.bodyValue(req)
						.retrieve()
						.bodyToMono(Map.class)
						.block();

				System.out.println("Full Response: " + response);

				if (response == null) {
					System.out.println(" API failed!");
					return;
				}

				String webhook = (String) response.get("webhook");
				String token = (String) response.get("accessToken");

				System.out.println("Webhook: " + webhook);
				System.out.println("Token: " + token);

				if (webhook == null || webhook.isEmpty()) {
					System.out.println(" Invalid webhook!");
					return;
				}

				if (token == null || token.isEmpty()) {
					System.out.println("Token missing!");
					return;
				}

				// FINALLY SQL QUERY
				String finalQuery = "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e1 JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME ORDER BY e1.EMP_ID DESC;";

				String result = client.post()
						.uri(webhook)
						.header("Authorization", token)
						.header("Content-Type", "application/json")
						.bodyValue(Map.of("finalQuery", finalQuery))
						.retrieve()
						.bodyToMono(String.class)
						.block();

				System.out.println("Submitted Successfully!");
				System.out.println("Server Response: " + result);

			} catch (Exception e) {
				System.out.println(" Error occurred:");
				e.printStackTrace();
			}
		};
	}
}