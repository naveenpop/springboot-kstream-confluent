package com.example.demokstream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;

@SpringBootApplication
@EnableSchemaRegistryClient
@Slf4j
public class DemokstreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemokstreamApplication.class, args);
	}

}



