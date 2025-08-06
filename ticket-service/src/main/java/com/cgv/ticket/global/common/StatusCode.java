package com.cgv.ticket.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    // Ticket
    TICKET_FOUND(OK, "티켓 조회 완료"),
    TICKET_CREATE(OK, "티켓 생성 완료"),
    TICKET_CREATE_REQUEST(OK, "티켓 생성 요청"),
    TICKET_CANCEL(OK,"티켓 취소 완료"),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INPUT_VALUE_INVALID(BAD_REQUEST,"유효하지 않은 입력입니다."),
    DATA_INTEGRITY_VIOLATION(BAD_REQUEST,"이미 존재하는 값이거나 필수 필드에 null이 있습니다."),
    TICKET_UNAVAILABLE_CANCEL(BAD_REQUEST, "취소 처리 가능한 티켓이 아닙니다"),
    TICKET_UNAVAILABLE_AWAITING_PAYMENT(BAD_REQUEST, "결제 대기 가능한 티켓이 아닙니다"),
    TICKET_UNAVAILABLE_FAIL(BAD_REQUEST, "실패 처리 가능한 티켓이 아닙니다"),
    
    /* 403 FORBIDDEN : 권한 없음 */

    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    MOVIE_NOT_EXIST(NOT_FOUND,"존재하지 않는 영화입니다."),
    SCHEDULE_NOT_EXIST(NOT_FOUND,"존재하지 않는 영화 일정입니다."),
    SEAT_NOT_EXIST(NOT_FOUND, "존재하지 않는 좌석입니다."),
    TICKET_NOT_EXIST(NOT_FOUND,"존재하지 않는 티켓입니다.");


    private final HttpStatus status;
    private final String message;
}
