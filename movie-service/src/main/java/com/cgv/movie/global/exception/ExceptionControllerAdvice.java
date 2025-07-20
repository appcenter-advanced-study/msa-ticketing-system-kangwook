package com.cgv.movie.global.exception;


import com.cgv.movie.global.common.ErrorResponse;
import com.cgv.movie.global.common.StatusCode;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    // 무슨 예외가 터졌는지 메시지만 담는다.
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        ErrorResponse errorResponse=ErrorResponse.builder()
                .message(e.getStatusCode().getMessage())
                .build();

        return ResponseEntity.status(e.getStatusCode().getStatus())
                .body(errorResponse);
    }

    // 좌석 예매 실패 시 락 획득에 실패
    @ExceptionHandler(PessimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(PessimisticLockingFailureException e){
        ErrorResponse errorResponse=ErrorResponse.builder()
                .message(StatusCode.SEAT_SOLD_OUT.getMessage())
                .build();

        return ResponseEntity.status(StatusCode.SEAT_SOLD_OUT.getStatus())
                .body(errorResponse);
    }
}
