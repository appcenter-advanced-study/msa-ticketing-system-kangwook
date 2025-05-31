package com.cgv.movie.domain.schedule.dto;

import com.cgv.movie.domain.movie.Movie;
import com.cgv.movie.domain.schedule.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ScheduleReq {
    private LocalDate date;

    @Schema(example = "14:30", type = "string")
    private LocalTime time;

    private Integer runningTime;

    public Schedule toEntity(Movie movie){
        return Schedule.builder()
                .date(date)
                .time(time)
                .runningTime(runningTime)
                .movie(movie)
                .build();
    }
}
