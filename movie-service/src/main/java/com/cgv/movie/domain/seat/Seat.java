package com.cgv.movie.domain.seat;

import com.cgv.movie.domain.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_index",nullable = false)
    private Integer rowIndex;

    @Column(name = "column_index",nullable = false)
    private Integer columnIndex;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;


    @Builder
    public Seat(Integer rowIndex, Integer columnIndex, Status status, Schedule schedule) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.status = status;
        this.schedule = schedule;
    }

    public void changeStatusAvailable(){ this.status=Status.AVAILABLE; }

    public void changeStatusLocked(){ this.status=Status.LOCKED; }

    public void changeStatusReserved(){ this.status=Status.RESERVED; }
}
