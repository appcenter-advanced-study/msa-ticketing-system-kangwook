package com.cgv.reservation.domain.reservation;


import com.cgv.reservation.domain.reservation.dto.ReservationRes;
import com.cgv.reservation.global.common.StatusCode;
import com.cgv.reservation.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public List<ReservationRes> getReservationList(String userName){
        List<Reservation> reservations= reservationRepository.findAllByUserName(userName);

        return reservations.stream()
                .map(ReservationRes::from)
                .toList();
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
