package com.cgv.movie.global.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QueueEvent {
    private Long scheduleId;
    private String userName;

    public QueueEvent(Long scheduleId, String userName) {
        this.scheduleId = scheduleId;
        this.userName = userName;
    }
}
