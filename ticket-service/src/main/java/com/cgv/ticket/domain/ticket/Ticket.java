package com.cgv.ticket.domain.ticket;


import com.cgv.ticket.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseEntity{

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
    public Ticket(String userName, Status status, Long seatId) {
        this.userName= userName;
        this.status = status;
        this.seatId = seatId;
    }

    public void changeStatusPending(){ this.status=Status.PENDING; }

    public void changeStatusFailed(){ this.status=Status.FAIL; }

    public void changeStatusAvailable(){ this.status=Status.AVAILABLE; }

    public void changeStatusCancel(){ this.status=Status.CANCEL; }

    public void changeStatusUsed(){ this.status=Status.USED; }
}
