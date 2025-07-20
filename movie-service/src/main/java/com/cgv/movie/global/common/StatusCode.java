package com.cgv.movie.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    // Movie
    MOVIE_CREATE(CREATED, "영화 생성 완료"),
    MOVIE_FOUND(OK, "영화 조회 완료"),
    MOVIE_UPDATE(OK, "영화 수정 완료"),
    MOVIE_DELETE(OK, "영화 삭제 완료"),
    MOVIE_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 영화입니다."),


    // Schedule
    SCHEDULE_CREATE(CREATED, "스케줄 생성 완료"),
    SCHEDULE_FOUND(OK, "스케줄 조회 완료"),
    SCHEDULE_DELETE(OK, "스케줄 삭제 완료"),

    // Seat
    SEAT_CREATE(CREATED, "좌석 생성 완료"),
    SEAT_FOUND(OK, "좌석 조회 완료"),
    SEAT_DELETE(OK, "좌석 삭제 완료"),
    SEAT_LOCKED(CONFLICT,"이미 선택된 좌석입니다."),
    SEAT_UNAVAILABLE(BAD_REQUEST,"선택할 수 없는 좌석입니다."),


    /* 400 BAD_REQUEST : 잘못된 요청 */
    INPUT_VALUE_INVALID(BAD_REQUEST,"유효하지 않은 입력입니다."),


    /* 404 NOT_FOUND : 존재하지 않는 리소스 */
    MOVIE_NOT_EXIST(NOT_FOUND,"존재하지 않는 영화입니다."),
    SCHEDULE_NOT_EXIST(NOT_FOUND,"존재하지 않는 영화 일정입니다."),
    SEAT_NOT_EXIST(NOT_FOUND, "존재하지 않는 좌석입니다.");

    private final HttpStatus status;
    private final String message;
}
