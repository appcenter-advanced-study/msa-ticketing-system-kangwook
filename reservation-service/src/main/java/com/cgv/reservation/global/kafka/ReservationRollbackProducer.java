package com.cgv.reservation.global.kafka;


import com.cgv.reservation.global.kafka.event.ReservationRollbackEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationRollbackProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendReservationRollbackEvent(Long seatId) {
        ReservationRollbackEvent event= new ReservationRollbackEvent(seatId);
        String topic = "reservation-rollback";
        kafkaTemplate.send(topic, event); // seatId 기준 파티셔닝

        log.info("예매 실패 롤백 이벤트 전송됨: 좌석ID={}, 토픽={}",
                seatId, topic);
    }
}
