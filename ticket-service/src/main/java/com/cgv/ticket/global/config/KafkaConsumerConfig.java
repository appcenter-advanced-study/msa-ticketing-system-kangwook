package com.cgv.ticket.global.config;

import com.cgv.ticket.global.kafka.event.seat.SeatLockFailEvent;
import com.cgv.ticket.global.kafka.event.seat.SeatLockSuccessEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private final String trustedPackages = "com.cgv.ticket.global.kafka";


    /** 공통 Consumer 기본 설정 **/
    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return config;
    }

    // 좌석 락 성공 이벤트 수신
    @Bean
    public ConsumerFactory<String, SeatLockSuccessEvent> seatLockSuccessConsumerFactory() {
        JsonDeserializer<SeatLockSuccessEvent> deserializer = new JsonDeserializer<>(SeatLockSuccessEvent.class);
        deserializer.addTrustedPackages(trustedPackages);
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(baseConsumerConfigs(), new StringDeserializer(),deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SeatLockSuccessEvent> seatLockSuccessKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SeatLockSuccessEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(seatLockSuccessConsumerFactory());
        return factory;
    }

    // 좌석 락 실패 이벤트 수신
    @Bean
    public ConsumerFactory<String, SeatLockFailEvent> seatLockFailConsumerFactory() {
        JsonDeserializer<SeatLockFailEvent> deserializer = new JsonDeserializer<>(SeatLockFailEvent.class);
        deserializer.addTrustedPackages(trustedPackages);
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(baseConsumerConfigs(), new StringDeserializer(),deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SeatLockFailEvent> seatLockFailKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SeatLockFailEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(seatLockFailConsumerFactory());
        return factory;
    }


}

