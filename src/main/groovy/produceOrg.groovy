package groovy

@Grapes([
        @GrabConfig(systemClassLoader=true),
        @Grab(group='org.apache.kafka', module='kafka-clients', version='2.0.0'),
        @Grab(group='org.apache.avro', module='avro', version='1.8.2'),
        @Grab(group='io.confluent', module='kafka-avro-serializer', version='4.0.0')
])

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.Encoder
import org.apache.avro.io.EncoderFactory
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.avro.Schema

KafkaProducer producer

def Properties producerProps = new Properties()
def SCHEMA_REGISTRY_URL = "schema.registry.url"

producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 'localhost:9092')
producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, 'localhost:9092')

producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 'io.confluent.kafka.serializers.KafkaAvroSerializer')
producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 'io.confluent.kafka.serializers.KafkaAvroSerializer')
producerProps.put(SCHEMA_REGISTRY_URL, "http://localhost:8081");

producer = new KafkaProducer(producerProps)

def messageSender = { String topic, byte[] message ->
    println "Message : " + message
    println "Topic : " + topic
    String key = new Random().nextLong()
    producer.send(
            new ProducerRecord<String, byte[]>(topic, key, message),
            { RecordMetadata metadata, Exception e -> log.info "The offset of the record we just sent is: ${metadata.offset()}"
            } as Callback
    )
}

Schema schema = new Schema.Parser().parse(new File("/home/parameswarann/code/springboot-kstream-confluent/src/main/resources/avro/Organization.avsc"));

GenericRecord genericRecord = new GenericData.Record(schema);
genericRecord.put("orgId", "222")
genericRecord.put("orgName", "Organization Name")
genericRecord.put("orgType", "PARENT")
genericRecord.put("parentOrgId", "111")
println "sending genericRecord: " + genericRecord
messageSender("organization-updates", datumToByteArray(schema, genericRecord))
println "message sent"

producer.close()

static byte[] datumToByteArray(Schema schema, GenericRecord datum) throws IOException {
    GenericDatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
        Encoder e = EncoderFactory.get().binaryEncoder(os, null);
        writer.write(datum, e);
        e.flush();
        byte[] byteData = os.toByteArray();
        return byteData;
    } finally {
        os.close();
    }
}

