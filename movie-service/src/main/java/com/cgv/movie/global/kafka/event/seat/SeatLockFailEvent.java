package com.cgv.movie.global.kafka.event.seat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatLockFailEvent {
    private Long seatId;
    private String userName;
    private Long ticketId;

    @Builder
    public SeatLockFailEvent(Long seatId, String userName, Long ticketId) {
        this.seatId = seatId;
        this.userName = userName;
        this.ticketId = ticketId;
    }
}
