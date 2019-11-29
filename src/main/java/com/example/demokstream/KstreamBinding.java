package com.example.demokstream;

import com.test.demo.avro.Organization;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface KstreamBinding {

    String ORGANIZATION_INPUT= "organizationInput";
    String ORGANIZATION_OUTPUT= "organizationOutput";

    @Input(ORGANIZATION_INPUT)
    KStream<String, Organization> organizationInputMessageChannel();
    //MessageChannel organizationMessageChannel();

    @Output(ORGANIZATION_OUTPUT)
    MessageChannel organizationOutputMessageChannel();
}
