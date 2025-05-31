package com.cgv.queue.kafka;


import com.cgv.queue.kafka.event.QueueEvent;
import com.cgv.queue.redis.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
@Slf4j
public class QueueConsumer {
    private final WaitingQueueService waitingQueueService;

    @KafkaListener(topics = "redis-queue", groupId = "queue-group")
    public void consume(ConsumerRecord<String, QueueEvent> record) {
        Long offset= record.offset();
        QueueEvent request = record.value();

        waitingQueueService.enterWaitingQueueWithKafka(offset, request.getUserName(), request.getScheduleId());
    }

}
