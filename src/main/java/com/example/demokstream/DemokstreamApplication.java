package com.example.demokstream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
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

	@Component
	public  static class PageViewEventSource implements ApplicationRunner {

		private final MessageChannel pageViewOut;

		public PageViewEventSource(KstreamBinding kstreamBinding) {
			this.pageViewOut = kstreamBinding.pageViewsOut();
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
			log.info("Starting: Run method");
			List<String> names = Arrays.asList("blue", "red", "green", "black", "white");
			List<String> pages = Arrays.asList("whiskey", "wine", "rum", "jin", "beer");
			Runnable runnable = () -> {
				String rPage = pages.get(new Random().nextInt(pages.size()));
				String rName = names.get(new Random().nextInt(names.size()));

				PageViewEvent pageViewEvent = new PageViewEvent(rName, rPage, Math.random() > .5 ? 10L : 1000L);

				Message<PageViewEvent> message = MessageBuilder
						.withPayload(pageViewEvent)
						.setHeader(KafkaHeaders.MESSAGE_KEY, pageViewEvent.getUserId().getBytes())
						.build();
				try {
					this.pageViewOut.send(message);
					log.info("Message Sent:" +message);
				} catch (Exception e) {
					log.info("Exception :" +e);
				}
			};
			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable ,1 ,1, TimeUnit.SECONDS);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DemokstreamApplication.class, args);
	}

}



