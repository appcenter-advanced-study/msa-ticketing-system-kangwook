package com.cgv.movie.global.kafka;


import com.cgv.movie.global.kafka.event.seat.SeatExpiredEvent;
import com.cgv.movie.global.kafka.event.seat.SeatLockFailEvent;
import com.cgv.movie.global.kafka.event.seat.SeatLockSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeatEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendSeatLockedSuccessEvent(SeatLockSuccessEvent event) {
        String topic = "seat.lock.success";
        kafkaTemplate.send(topic,event);

        log.info("좌석 선점 성공 이벤트 전송: 사용자={}, 좌석ID={}",
                event.getUserName(), event.getSeatId());
    }

    public void sendSeatLockedFailEvent(SeatLockFailEvent event) {
        String topic = "seat.lock.fail";
        kafkaTemplate.send(topic,event);

        log.info("좌석 선점 실패 이벤트 전송: 사용자={}, 좌석ID={}",
                event.getUserName(), event.getSeatId());
    }

    public void sendSeatExpiredEvent(SeatExpiredEvent event) {
        String topic = "seat.expired";
        kafkaTemplate.send(topic,event);

        log.info("좌석 선점 만료 이벤트 전송: 티켓ID={}",
               event.getTicketId());
    }

}
