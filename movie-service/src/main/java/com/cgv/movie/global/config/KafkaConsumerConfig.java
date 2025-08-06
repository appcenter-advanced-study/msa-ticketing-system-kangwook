package com.cgv.movie.global.config;

import com.cgv.movie.global.kafka.event.ticket.TicketCreatedEvent;
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
    private final String trustedPackages = "com.cgv.movie.global.kafka";


    /** 공통 Consumer 기본 설정 **/
    private Map<String, Object> baseConsumerConfigs() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return config;
    }

    // 티켓 생성 이벤트 수신
    @Bean
    public ConsumerFactory<String, TicketCreatedEvent> ticketCreatedConsumerFactory() {
        JsonDeserializer<TicketCreatedEvent> deserializer = new JsonDeserializer<>(TicketCreatedEvent.class);
        deserializer.addTrustedPackages(trustedPackages);
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(baseConsumerConfigs(), new StringDeserializer(),deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TicketCreatedEvent> ticketCreatedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TicketCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(ticketCreatedConsumerFactory());
        return factory;
    }
}

