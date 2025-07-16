package com.cgv.ticket.global.kafka.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TicketCreatedEvent {
    private Long ticketId;
    private String userName;
    private Long seatId;

    @Builder
    public TicketCreatedEvent(Long ticketId, String userName, Long seatId) {
        this.ticketId = ticketId;
        this.userName = userName;
        this.seatId = seatId;
    }

}
