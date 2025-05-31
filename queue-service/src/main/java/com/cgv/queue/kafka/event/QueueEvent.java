package com.cgv.queue.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QueueEvent {
    private String userName;
    private Long scheduleId;

    public QueueEvent(String userName, Long scheduleId) {
        this.userName = userName;
        this.scheduleId = scheduleId;
    }
}
