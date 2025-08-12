package com.cgv.ticket.global.kafka.event.seat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatExpiredEvent {
    private Long ticketId;

    @Builder
    public SeatExpiredEvent(Long ticketId) {
        this.ticketId = ticketId;
    }
}
