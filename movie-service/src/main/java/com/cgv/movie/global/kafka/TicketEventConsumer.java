package com.cgv.movie.global.kafka;


import com.cgv.movie.domain.seat.SeatService;
import com.cgv.movie.global.exception.CustomException;
import com.cgv.movie.global.kafka.event.seat.SeatLockFailEvent;
import com.cgv.movie.global.kafka.event.seat.SeatLockSuccessEvent;
import com.cgv.movie.global.kafka.event.ticket.TicketCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketEventConsumer {
    private final SeatService seatService;
    private final SeatEventProducer seatEventProducer;


    @Transactional
    @KafkaListener(topics = "ticket.created",
            groupId = "seat-group",
            containerFactory = "ticketCreatedKafkaListenerContainerFactory")
    public void handleTicketCreatedEvent(ConsumerRecord<String, TicketCreatedEvent> record) {
        TicketCreatedEvent event = record.value();

        log.info("티켓 생성 이벤트 수신: 좌석ID={}",  event.getSeatId());

        try{
            seatService.tryLockSeat(event.getSeatId());

            SeatLockSuccessEvent seatLockSuccessEvent = SeatLockSuccessEvent.builder()
                    .seatId(event.getSeatId())
                    .userName(event.getUserName())
                    .ticketId(event.getTicketId())
                    .build();

            seatEventProducer.sendSeatLockedSuccessEvent(seatLockSuccessEvent);
        }
        catch (CustomException e){

            SeatLockFailEvent seatLockFailEvent = SeatLockFailEvent.builder()
                    .seatId(event.getSeatId())
                    .userName(event.getUserName())
                    .ticketId(event.getTicketId())
                    .build();

            seatEventProducer.sendSeatLockedFailEvent(seatLockFailEvent);

        }

    }
}
