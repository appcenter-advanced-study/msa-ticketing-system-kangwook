package com.cgv.movie.domain.movie.dto;

import com.cgv.movie.domain.movie.Movie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MovieReq {
    private String title;
    private LocalDate releaseDate;

    public Movie toEntity(){
        return Movie.builder()
                .title(title)
                .releaseDate(releaseDate)
                .build();
    }
}
