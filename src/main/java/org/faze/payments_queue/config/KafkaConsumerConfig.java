package org.faze.payments_queue.config;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.faze.payments_queue.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.Scheduled;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.group-id}")
  private String kafkaGroupId;

  @Bean
  public ConsumerFactory<String, PaymentDto> paymentDtoConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(PaymentDto.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PaymentDto> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PaymentDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(paymentDtoConsumerFactory());
    return factory;
  }

  @KafkaListener(topics = "#{'${payment.kafka.topic}'.split(',')}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void listenGroupFoo(ConsumerRecord<String, PaymentDto> paymentDto) {
    System.out.println("Прочитана транзакция: "
        + paymentDto.offset() + " " + paymentDto.value().toString());
  }
}
