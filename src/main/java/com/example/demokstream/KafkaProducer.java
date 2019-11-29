package com.example.demokstream;

import com.test.demo.avro.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
@EnableBinding(KstreamBinding.class)
public class KafkaProducer {

    @Autowired
    private KstreamBinding kstreamBinding;

    public void produceOrganization(String orgId, String orgName, String orgType, String parentOrgId) {

        try {
            Organization organization = Organization.newBuilder()
                    .setOrgId(orgId)
                    .setOrgName(orgName)
                    .setOrgType(orgType)
                    .setParentOrgId(parentOrgId)
                    .build();

            kstreamBinding.organizationOutputMessageChannel()
                            .send(MessageBuilder.withPayload(organization)
                            .setHeader(KafkaHeaders.MESSAGE_KEY, orgName)
                            .build());

        } catch (Exception e){
            log.error("Failed to produce Organization Message:" +e);
        }
    }
}