package com.example.demokstream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableSchemaRegistryClient
@Slf4j
public class DemokstreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemokstreamApplication.class, args);
	}

	@Component
	public  static class organizationProducer implements ApplicationRunner {

		@Autowired
		private KafkaProducer kafkaProducer;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			log.info("Starting: Run method");
			List<String> names = Arrays.asList("blue", "red", "green", "black", "white");
			List<String> pages = Arrays.asList("whiskey", "wine", "rum", "jin", "beer");
			Runnable runnable = () -> {
				String rPage = pages.get(new Random().nextInt(pages.size()));
				String rName = names.get(new Random().nextInt(names.size()));

				try {
					this.kafkaProducer.produceOrganization(rPage, rName, "PARENT", "111");
				} catch (Exception e) {
					log.info("Exception :" +e);
				}
			};
			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable ,1 ,1, TimeUnit.SECONDS);
		}
	}
}




