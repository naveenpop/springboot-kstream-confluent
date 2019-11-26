package com.example.demokstream;

import com.test.demo.avro.Organization;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;

@Slf4j
@EnableBinding(KstreamBinding.class)
public class KafkaConsumer {

    /*@StreamListener(KstreamBinding.ORGANIZATION)
    public void processOrganization(Organization organization) {
        log.info("Organization Received:" + organization);
    }*/

    @StreamListener
    public void processOrganization(@Input(KstreamBinding.ORGANIZATION) KStream<String, Organization> organization) {
        log.info("Organization Received:" + organization);
    }
}