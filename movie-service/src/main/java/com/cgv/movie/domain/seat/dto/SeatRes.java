package com.cgv.movie.domain.seat.dto;

import com.cgv.movie.domain.seat.Seat;
import com.cgv.movie.domain.seat.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SeatRes {
    private final Long id;
    private final Integer rowIndex;
    private final Integer columnIndex;
    private final Boolean isReserved;

    @Builder
    public SeatRes(Long id, Integer rowIndex, Integer columnIndex, Boolean isReserved) {
        this.id = id;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.isReserved = isReserved;
    }

    public static SeatRes from(Seat seat){
        return SeatRes.builder()
                .id(seat.getId())
                .rowIndex(seat.getRowIndex())
                .columnIndex(seat.getColumnIndex())
                .isReserved(seat.getStatus() == Status.AVAILABLE)
                .build();
    }
}
