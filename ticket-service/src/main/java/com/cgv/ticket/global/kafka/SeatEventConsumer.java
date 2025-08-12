package com.cgv.ticket.global.kafka;


import com.cgv.ticket.domain.ticket.TicketService;
import com.cgv.ticket.global.kafka.event.seat.SeatExpiredEvent;
import com.cgv.ticket.global.kafka.event.seat.SeatLockFailEvent;
import com.cgv.ticket.global.kafka.event.seat.SeatLockSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeatEventConsumer {
    private final TicketService ticketService;

    @Transactional
    @KafkaListener(topics = "seat.lock.success",
            groupId = "ticket-group",
            containerFactory = "seatLockSuccessKafkaListenerContainerFactory")
    public void handleSeatLockSuccessEvent(ConsumerRecord<String, SeatLockSuccessEvent> record) {
        SeatLockSuccessEvent event = record.value();
        log.info("티켓 결제 대기 이벤트 수신: 티켓ID={}",  event.getTicketId());

        ticketService.awaitingPaymentTicket(event.getTicketId());
    }


    @Transactional
    @KafkaListener(topics = "seat.lock.fail",
            groupId = "ticket-rollback-group",
            containerFactory = "seatLockFailKafkaListenerContainerFactory")
    public void handleSeatLockFailEvent(ConsumerRecord<String, SeatLockFailEvent> record) {
        SeatLockFailEvent event = record.value();
        log.info("티켓 실패 이벤트 수신: 티켓ID={}",  event.getTicketId());

        ticketService.failTicket(event.getTicketId());
    }

    @Transactional
    @KafkaListener(topics = "seat.expired",
            groupId = "ticket-expired-group",
            containerFactory = "seatLockFailKafkaListenerContainerFactory")
    public void handleSeatLockExpiredEvent(ConsumerRecord<String, SeatExpiredEvent> record) {
        SeatExpiredEvent event = record.value();
        log.info("티켓 만료 이벤트 수신: 티켓ID={}",  event.getTicketId());

        ticketService.failTicket(event.getTicketId());
    }
}
