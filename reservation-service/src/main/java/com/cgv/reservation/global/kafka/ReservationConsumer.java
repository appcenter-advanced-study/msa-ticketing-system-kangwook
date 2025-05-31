package com.cgv.reservation.global.kafka;


import com.cgv.reservation.domain.reservation.Reservation;
import com.cgv.reservation.domain.reservation.ReservationRepository;
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
    private final ReservationRepository reservationRepository;


    @KafkaListener(topicPattern = "reservation-.*", groupId = "reservation-group", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consume(ConsumerRecord<String, ReservationEvent> record) {
        ReservationEvent request = record.value();

        log.info("예약 이벤트 수신: 사용자={}, 좌석ID={}, 스케줄ID={}", request.getUserName(), request.getSeatId(), request.getScheduleId());

        Reservation reservation= Reservation.builder()
                .userName(request.getUserName())
                .status(Status.RESERVED)
                .seatId(request.getSeatId())
                .build();

        reservationRepository.save(reservation);

        log.info("예약 저장 완료: 사용자={}, 좌석ID={}", request.getUserName(), request.getSeatId());
    }
}
