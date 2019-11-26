package com.example.demokstream;

import com.test.demo.avro.Organization;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface KstreamBinding {

    String ORGANIZATION= "organization";

    @Input(ORGANIZATION)
    KStream<String, Organization> organizationMessageChannel();
    //MessageChannel organizationMessageChannel();
}
