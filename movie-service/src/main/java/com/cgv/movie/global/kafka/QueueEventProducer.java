package com.cgv.movie.global.kafka;


import com.cgv.movie.global.kafka.event.queue.QueueEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendQueueEvent(Long scheduleId, String userName) {
        QueueEvent request = new QueueEvent(scheduleId, userName);
        kafkaTemplate.send("redis-queue", scheduleId.toString(), request);

        log.info("Redis 큐 입장 요청 전송됨: 사용자={}, 영화 스케줄ID={}" , userName, scheduleId);
    }
}
