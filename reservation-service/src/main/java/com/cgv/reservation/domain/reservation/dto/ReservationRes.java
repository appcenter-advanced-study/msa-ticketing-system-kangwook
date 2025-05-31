package com.cgv.reservation.domain.reservation.dto;

import com.cgv.reservation.domain.reservation.Reservation;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationRes {
    private final Long id;
    private final String userName;
    private final String status;
    private final Long seatId;

    @Builder
    public ReservationRes(Long id, String userName, String status, Long seatId) {
        this.id = id;
        this.userName = userName;
        this.status = status;
        this.seatId = seatId;
    }

    public static ReservationRes from(Reservation reservation){
        return ReservationRes.builder()
                .id(reservation.getId())
                .userName(reservation.getUserName())
                .status(reservation.getStatus().toString())
                .seatId(reservation.getSeatId())
                .build();
    }
}
