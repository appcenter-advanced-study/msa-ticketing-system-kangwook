package com.cgv.movie.global.kafka;


import com.cgv.movie.global.kafka.event.QueueEvent;
import com.cgv.movie.global.kafka.event.ReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendReservationRequest(Long scheduleId, Long seatId, String userName) {
        ReservationEvent request = new ReservationEvent(scheduleId, seatId, userName);
        String topic = "reservation-" + scheduleId;
        kafkaTemplate.send(topic, seatId.toString(), request); // seatId 기준 파티셔닝

        log.info("예메 요청 전송됨: 사용자={}, 좌석ID={}, 스케줄ID={}, 토픽={}",
                userName, seatId, scheduleId, topic);
    }

    public void sendQueueEvent(Long scheduleId, String userName) {
        QueueEvent request = new QueueEvent(scheduleId, userName);
        kafkaTemplate.send("redis-queue", scheduleId.toString(), request);

        log.info("Redis 큐 입장 요청 전송됨: 사용자={}, 영화 스케줄ID={}" , userName, scheduleId);
    }
}
