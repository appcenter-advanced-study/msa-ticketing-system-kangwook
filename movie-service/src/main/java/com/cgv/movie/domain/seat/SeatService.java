package com.cgv.movie.domain.seat;


import com.cgv.movie.domain.schedule.Schedule;
import com.cgv.movie.domain.schedule.ScheduleRepository;
import com.cgv.movie.domain.seat.dto.SeatReq;
import com.cgv.movie.domain.seat.dto.SeatRes;
import com.cgv.movie.global.common.StatusCode;
import com.cgv.movie.global.exception.CustomException;
import com.cgv.movie.global.kafka.SeatEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SeatService {
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final SeatEventProducer seatEventProducer;

    @Transactional
    public List<SeatRes> createSeat(Long scheduleId, SeatReq seatReq) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (schedule.isEmpty())
            throw new CustomException(StatusCode.SCHEDULE_NOT_EXIST);
        Schedule movieschedule=schedule.get();
        for(int i=1;i<=seatReq.getRow();i++)
            for(int j=1;j<=seatReq.getColumn();j++){
                seatRepository.save(seatReq.toEntity(i,j,movieschedule));
            }
        List<Seat> seats = seatRepository.findAllBySchedule_IdOrderByRowIndexAscColumnIndexAsc(scheduleId);
        return seats.stream()
                .map(SeatRes::from)
                .toList();
    }


    @Transactional
    public void tryLockSeat(Long seatId){
        Seat seat=seatRepository.findBySeatIdWithRock(seatId)
                .orElseThrow(() -> new CustomException(StatusCode.SEAT_LOCKED));

        if(seat.getStatus().equals(Status.AVAILABLE))
            seat.changeStatusLocked();
        else throw new CustomException(StatusCode.SEAT_UNAVAILABLE);

    }

//    // 예매 생성 실패 보상 트랜잭션
//    @Transactional
//    public void rollbackReservation(Long seatId){
//        Seat seat=seatRepository.findBySeatIdWithRock(seatId)
//                .orElseThrow(() -> new CustomException(StatusCode.SEAT_NOT_EXIST));
//
//        if(seat.getIsReserved())
//            seat.rollback();
//
//        log.info("예약 롤백 이벤트 처리: 좌석ID={}", seatId);
//    }

    public List<SeatRes> getSeatList(Long scheduleId) {
        List<Seat> seats = seatRepository.findAllBySchedule_IdOrderByRowIndexAscColumnIndexAsc(scheduleId);
        return seats.stream()
                .map(SeatRes::from)
                .toList();
    }

    @Transactional
    public void deleteSeatAll(Long scheduleId) {
        seatRepository.deleteAllBySchedule_Id(scheduleId);
    }

}
