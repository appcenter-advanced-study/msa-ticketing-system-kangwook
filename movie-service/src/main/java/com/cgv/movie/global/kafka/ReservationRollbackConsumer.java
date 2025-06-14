package com.cgv.movie.global.kafka;


import com.cgv.movie.domain.seat.SeatService;
import com.cgv.movie.global.kafka.event.ReservationRollbackEvent;
import com.cgv.reservation.domain.reservation.ReservationService;
import com.cgv.reservation.global.kafka.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationRollbackConsumer {
    private final SeatService seatService;


    @KafkaListener(topicPattern = "reservation-rollback", groupId = "reservation-rollback", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, ReservationRollbackEvent> record) {
        ReservationRollbackEvent request = record.value();

        log.info("예약 롤백 이벤트 수신: 좌석ID={}",  request.getSeatId());

        seatService.rollbackReservation(request.getSeatId());
    }
}
