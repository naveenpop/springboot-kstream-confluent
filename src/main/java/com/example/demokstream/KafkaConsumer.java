package com.example.demokstream;

import com.example.avro.Organization;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

@Slf4j
@EnableBinding(KstreamBinding.class)
public class KafkaConsumer {

    @StreamListener
    @SendTo(KstreamBinding.PAGE_COUNT_OUT)
    public KStream<String, PageViewEvent> process(@Input(KstreamBinding.PAGE_VIEWS_IN) KStream<String, PageViewEvent> events) {

        log.info("Processing PageViewEvent:: " +events);
        return events.filter((s, pageViewEvent) -> pageViewEvent.getUserId().equals("white"));
    }

    @StreamListener
    public void processPage(@Input(KstreamBinding.PAGE_COUNT_IN) KStream<String, PageViewEvent> pageCount) {
        pageCount.foreach((s, pageViewEvent) -> log.info(" Message User Received:: User: "+pageViewEvent.getUserId() + " App:: " +pageViewEvent.getPage()));
    }

    /*@StreamListener(KstreamBinding.ORGANIZATION)
    public void processOrganization(Organization organization) {
        log.info("Organization Received:" + organization);
    }*/

    @StreamListener
    public void processOrganization(@Input(KstreamBinding.ORGANIZATION)KStream<String, Organization> organization) {
        log.info("Organization Received:" + organization);
    }
}