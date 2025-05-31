package com.cgv.movie.domain.schedule;


import com.cgv.movie.domain.movie.Movie;
import com.cgv.movie.domain.movie.MovieRepository;
import com.cgv.movie.domain.schedule.dto.ScheduleReq;
import com.cgv.movie.domain.schedule.dto.ScheduleRes;
import com.cgv.movie.global.common.StatusCode;
import com.cgv.movie.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public ScheduleRes createSchedule(Long movieId, ScheduleReq scheduleReq){
        Optional<Movie> movie= movieRepository.findById(movieId);
        if(movie.isEmpty())
            throw new CustomException(StatusCode.MOVIE_NOT_EXIST);
        return ScheduleRes.from(scheduleRepository.save(scheduleReq.toEntity(movie.get())));
    }

    public List<ScheduleRes> getScheduleList(Long movieId){
        List<Schedule> schedules = scheduleRepository.findAllByMovie_Id(movieId);
        return schedules.stream()
                .map(ScheduleRes::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSchedule(Long scheduleId){
        scheduleRepository.deleteById(scheduleId);
    }
}
