package com.cgv.reservation.domain.reservation;


import com.cgv.reservation.domain.reservation.dto.ReservationRes;
import com.cgv.reservation.global.common.StatusCode;
import com.cgv.reservation.global.exception.CustomException;
import com.cgv.reservation.global.kafka.ReservationRollbackProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationRollbackProducer reservationRollbackProducer;

    public List<ReservationRes> getReservationList(String userName){
        List<Reservation> reservations= reservationRepository.findAllByUserName(userName);

        return reservations.stream()
                .map(ReservationRes::from)
                .toList();
    }
    @Transactional
    public void createReservation(String userName, Long seatId){
        try{

            Reservation reservation= Reservation.builder()
                    .userName(userName)
                    .status(Status.RESERVED)
                    .seatId(seatId)
                    .build();

            reservationRepository.save(reservation);

            log.info("예약 저장 완료: 사용자={}, 좌석ID={}", userName, seatId);
        } catch (Exception e) {
            log.error("예약 요청 오류, 사용자 이름:{}, 좌석 번호:{}", userName,seatId);
            reservationRollbackProducer.sendReservationRollbackEvent(seatId);
        }
    }

    @Transactional
    public void cancelReservation(Long reservationId){
        Reservation reservation= reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(StatusCode.RESERVATION_NOT_EXIST));

        if(reservation.getStatus()==Status.RESERVED)
            reservation.cancel();
        else throw new CustomException(StatusCode.RESERVATION_IS_DELETED);
    }

}
