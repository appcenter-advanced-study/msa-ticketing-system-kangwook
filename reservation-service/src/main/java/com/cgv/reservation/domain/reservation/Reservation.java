package com.cgv.reservation.domain.reservation;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;


    @Builder
    public Reservation(String userName, Status status, Long seatId) {
        this.userName= userName;
        this.status = status;
        this.seatId = seatId;
    }

    public void cancel(){ this.status=Status.CANCELED; }
}
