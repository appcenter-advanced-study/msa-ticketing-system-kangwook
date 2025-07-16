package com.cgv.ticket.domain.ticket.dto;

import com.cgv.ticket.domain.ticket.Ticket;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TicketRes {
    private final Long id;
    private final String userName;
    private final String status;
    private final Long seatId;

    @Builder
    public TicketRes(Long id, String userName, String status, Long seatId) {
        this.id = id;
        this.userName = userName;
        this.status = status;
        this.seatId = seatId;
    }

    public static TicketRes from(Ticket ticket){
        return TicketRes.builder()
                .id(ticket.getId())
                .userName(ticket.getUserName())
                .status(ticket.getStatus().toString())
                .seatId(ticket.getSeatId())
                .build();
    }
}
