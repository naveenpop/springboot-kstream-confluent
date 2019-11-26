1. produceOrg.groovy is a simple groovy script to produce the message onto topic.

        /code/springboot-kstream-confluent/src/main/groovy$ groovy produceOrg.groovy 
        SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
        SLF4J: Defaulting to no-operation (NOP) logger implementation
        SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
        sending genericRecord: {"orgId": "222", "orgName": "Organization Name", "orgType": "PARENT", "parentOrgId": "111"}
        Message : [6, 50, 50, 50, 34, 79, 114, 103, 97, 110, 105, 122, 97, 116, 105, 111, 110, 32, 78, 97, 109, 101, 12, 80, 65, 82, 69, 78, 84, 6, 49, 49, 49]
        Topic : organization-updates
        message sent


2.When i try to consume message as MessageChannel and with the below change in my code repo. I was able to consume the message.

      public interface KstreamBinding {
    
        String ORGANIZATION= "organization";
    
        @Input(ORGANIZATION)
        MessageChannel organizationMessageChannel();
    }

    @EnableBinding(KstreamBinding.class)
    public class KafkaConsumer {
    
        @StreamListener(KstreamBinding.ORGANIZATION)
        public void processOrganization(Organization organization) {
            log.info("Organization Received:" + organization);
        }
    }


Logs:

    2019-11-26 14:32:37.032  INFO 19472 --- [container-0-C-1] o.a.k.c.c.internals.AbstractCoordinator  : [Consumer clientId=consumer-2, groupId=demokstream.org] (Re-)joining group
    2019-11-26 14:32:37.055  INFO 19472 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
    2019-11-26 14:32:37.057  INFO 19472 --- [           main] c.e.demokstream.DemokstreamApplication   : Started DemokstreamApplication in 4.699 seconds (JVM running for 5.169)
    2019-11-26 14:32:38.046  INFO 19472 --- [container-0-C-1] o.a.k.c.c.internals.AbstractCoordinator  : [Consumer clientId=consumer-2, groupId=demokstream.org] Successfully joined group with generation 9
    2019-11-26 14:32:38.048  INFO 19472 --- [container-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-2, groupId=demokstream.org] Setting newly assigned partitions [organization-updates-0]
    2019-11-26 14:32:38.053  INFO 19472 --- [container-0-C-1] o.s.c.s.b.k.KafkaMessageChannelBinder$1  : partitions assigned: [organization-updates-0]
    2019-11-26 14:32:38.198  INFO 19472 --- [container-0-C-1] com.example.demokstream.KafkaConsumer    : Organization Received:{"orgId": "222", "orgName": "Organization Name", "orgType": "PARENT", "parentOrgId": "111"}



3.When i try to consume the message as `KStream<String, Organization>` and stays with below log message and no error in the console.

        public interface KstreamBinding {
        
            String ORGANIZATION= "organization";
        
            @Input(ORGANIZATION)
            KStream<String, Organization> organizationMessageChannel();
        }
        
        
        @EnableBinding(KstreamBinding.class)
        public class KafkaConsumer {
        
            @StreamListener
            public void processOrganization(@Input(KstreamBinding.ORGANIZATION) KStream<String, Organization> organization) {
                log.info("Organization Received:" + organization);
            }
        }

Logs:

    2019-11-26 13:20:10.069  INFO 4394 --- [-StreamThread-1] o.a.k.s.p.internals.StreamThread         : stream-thread [demokstream.org-8cb8b2e3-0e61-4e0e-8ab7-22ce8c7d33b9-StreamThread-1] partition assignment took 34 ms.
        current active tasks: [0_0]
        current standby tasks: []
        previous active tasks: []
    
    2019-11-26 13:20:10.103  INFO 4394 --- [-StreamThread-1] o.a.k.s.p.internals.StreamThread         : stream-thread [demokstream.org-8cb8b2e3-0e61-4e0e-8ab7-22ce8c7d33b9-StreamThread-1] State transition from PARTITIONS_ASSIGNED to RUNNING
    2019-11-26 13:20:10.104  INFO 4394 --- [-StreamThread-1] org.apache.kafka.streams.KafkaStreams    : stream-client [demokstream.org-8cb8b2e3-0e61-4e0e-8ab7-22ce8c7d33b9]State transition from REBALANCING to RUNNING


