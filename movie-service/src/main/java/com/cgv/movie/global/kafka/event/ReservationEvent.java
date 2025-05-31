package com.cgv.movie.global.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationEvent {
    private Long scheduleId;
    private Long seatId;
    private String userName;

    public ReservationEvent(Long scheduleId, Long seatId, String userName) {
        this.scheduleId = scheduleId;
        this.seatId = seatId;
        this.userName = userName;
    }
}
