package com.cgv.queue.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    // Redis Queue
    QUEUE_USER_INSERT(OK,"대기열 입장"),
    QUEUE_USER_ISAVAILABLE(OK,"예매 가능 여부 반환"),
    QUEUE_USER_DELETE(OK,"대기열 퇴장"),
    QUEUE_USER_NOT_EXIST(NOT_FOUND,"대기열에 유저가 존재하지 않습니다.");



    private final HttpStatus status;
    private final String message;
}
