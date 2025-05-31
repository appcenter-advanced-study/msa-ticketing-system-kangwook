package com.cgv.movie.domain.seat.dto;

import com.cgv.movie.domain.schedule.Schedule;
import com.cgv.movie.domain.seat.Seat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatReq {
    private Integer row;

    private Integer column;

    public SeatReq(Integer row, Integer column) {
        this.row = row;
        this.column = column;
    }

    public Seat toEntity(int row, int column, Schedule schedule){
        return Seat.builder()
                .rowIndex(row)
                .columnIndex(column)
                .isReserved(false)
                .schedule(schedule)
                .build();
    }
}
