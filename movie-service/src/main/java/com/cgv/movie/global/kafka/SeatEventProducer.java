package com.cgv.movie.global.kafka;


import com.cgv.movie.global.kafka.event.queue.QueueEvent;
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

        log.info("좌석 선점 성공 이벤트 전송됨: 사용자={}, 좌석ID={}",
                event.getUserName(), event.getSeatId());
    }

    public void sendSeatLockedFailEvent(SeatLockFailEvent event) {
        String topic = "seat.lock.fail";
        kafkaTemplate.send(topic,event);

        log.info("좌석 선점 실패 이벤트 전송됨: 사용자={}, 좌석ID={}",
                event.getUserName(), event.getSeatId());
    }

//    public void sendQueueEvent(Long scheduleId, String userName) {
//        QueueEvent request = new QueueEvent(scheduleId, userName);
//        kafkaTemplate.send("redis-queue", scheduleId.toString(), request);
//
//        log.info("Redis 큐 입장 요청 전송됨: 사용자={}, 영화 스케줄ID={}" , userName, scheduleId);
//    }
}
