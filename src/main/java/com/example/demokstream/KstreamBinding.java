package com.example.demokstream;

import com.example.avro.Organization;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KstreamBinding {

    String PAGE_VIEWS_OUT = "pvout";
    String PAGE_VIEWS_IN = "pvin";
    String PAGE_VIEWS_MV = "pcmv";
    String PAGE_COUNT_OUT = "pcout";
    String PAGE_COUNT_IN = "pcin";
    String ORGANIZATION= "organization";

    @Input(PAGE_VIEWS_IN)
    KStream<String, PageViewEvent> pageViewsIn();

    @Output(PAGE_VIEWS_OUT)
    MessageChannel pageViewsOut();

    @Output(PAGE_COUNT_OUT)
    KStream<String, PageViewEvent> pageCountOut();

    @Input(PAGE_COUNT_IN)
    KStream<String, PageViewEvent> pageCountIn();

    @Input(ORGANIZATION)
    KStream<String, Organization> organizationMessageChannel();
    //MessageChannel organizationMessageChannel();
}
