package com.cgv.movie.global.kafka.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationRollbackEvent {
    private Long seatId;

    public ReservationRollbackEvent(Long seatId) {
        this.seatId = seatId;
    }
}
