package com.cgv.ticket.global.kafka;


import com.cgv.ticket.domain.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketEventConsumer {
    private final TicketService ticketService;


//    @KafkaListener(topicPattern = "reservation-.*", groupId = "reservation-group", containerFactory = "kafkaListenerContainerFactory")
//    public void consume(ConsumerRecord<String, TicketCreatedEvent> record) {
//        TicketCreatedEvent request = record.value();
//
//        log.info("예약 이벤트 수신: 사용자={}, 좌석ID={}, 스케줄ID={}", request.getUserName(), request.getSeatId(), request.getScheduleId());
//
//        ticketService.createTicket(request.getUserName(),request.getSeatId());
//    }
}
