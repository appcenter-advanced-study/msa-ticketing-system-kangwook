package com.cgv.reservation.domain.reservation;


import com.cgv.reservation.domain.reservation.dto.ReservationRes;
import com.cgv.reservation.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cgv.reservation.global.common.StatusCode.*;

@Tag(name = "[예매]", description = "예매 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "예매내역 조회", description = "나의 모든 예매 내역을 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<List<ReservationRes>>> getReservationList(
            @RequestParam String userName) {
        return ResponseEntity
                .status(RESERVATION_FOUND.getStatus())
                .body(CommonResponse.from(RESERVATION_FOUND.getMessage(),
                        reservationService.getReservationList(userName)));
    }

    @Operation(summary = "예매 취소", description = "해당 예매를 취소합니다.")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Object> deleteReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity
                .status(RESERVATION_DELETE.getStatus())
                .body(CommonResponse.from(RESERVATION_DELETE.getMessage()));
    }

}
