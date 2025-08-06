package com.cgv.ticket.global.kafka;


import com.cgv.ticket.global.kafka.event.ticket.TicketCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTicketCreatedEvent(TicketCreatedEvent ticketCreatedEvent) {
        String topic = "ticket.created";
        kafkaTemplate.send(topic, ticketCreatedEvent);

        log.info("티켓 생성 이벤트 전송됨: 티켓ID={}, 토픽={}",
                ticketCreatedEvent.getTicketId(), topic);
    }
}
