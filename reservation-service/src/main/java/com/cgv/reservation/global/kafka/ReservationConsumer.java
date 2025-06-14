package com.cgv.reservation.global.kafka;


import com.cgv.reservation.domain.reservation.Reservation;
import com.cgv.reservation.domain.reservation.ReservationRepository;
import com.cgv.reservation.domain.reservation.ReservationService;
import com.cgv.reservation.domain.reservation.Status;
import com.cgv.reservation.global.kafka.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationConsumer {
    private final ReservationService reservationService;


    @KafkaListener(topicPattern = "reservation-.*", groupId = "reservation-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, ReservationEvent> record) {
        ReservationEvent request = record.value();

        log.info("예약 이벤트 수신: 사용자={}, 좌석ID={}, 스케줄ID={}", request.getUserName(), request.getSeatId(), request.getScheduleId());

        reservationService.createReservation(request.getUserName(),request.getSeatId());
    }
}
