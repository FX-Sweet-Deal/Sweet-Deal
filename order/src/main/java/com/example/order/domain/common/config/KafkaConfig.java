package com.example.order.domain.common.config;

import com.example.order.domain.order.controller.model.request.MessageUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.application.name}-group")
  private String groupId;

  @Bean
  public ConsumerFactory<String, MessageUpdateRequest> consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

    props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

    // 들어오는 타입 상관없이 무조건 내 DTO로 역직렬화
    props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
        "com.example.order.domain.order.controller.model.request.MessageUpdateRequest");

    return new DefaultKafkaConsumerFactory<>(props);
  }


  @Bean
  public ProducerFactory<String, MessageUpdateRequest> producerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, MessageUpdateRequest> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean(name = "kafkaRetryListenerContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<String, MessageUpdateRequest>
  kafkaRetryListenerContainerFactory(ConsumerFactory<String, MessageUpdateRequest> consumerFactory,
      KafkaTemplate<String, MessageUpdateRequest> kafkaTemplate) {

    ConcurrentKafkaListenerContainerFactory<String, MessageUpdateRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);

    // DefaultErrorHandler: Spring Kafka 2.8 이상에서 기본 에러 핸들러
    factory.setCommonErrorHandler(new DefaultErrorHandler(
        new DeadLetterPublishingRecoverer(kafkaTemplate), // 실패한 메시지를 Dead Letter Topic으로 전송
        new FixedBackOff(1000L, 3)) // 메시지 재처리를 위한 고정 백오프 간격과 재시도 횟수를 설정
    );

    return factory;
  }
}
