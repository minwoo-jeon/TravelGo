package com.travelGo.global.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 공통 예외(global
    INVALID_INPUT_VALUE(400,"G001", "입력 값이 유효하지 않습니다."),


    // 회원 관련
    DUPLICATE_EMAIL(400, "M001", "이미 가입된 이메일입니다."),
    MEMBER_NOT_FOUND(404, "M002", "사용자를 찾을 수 없습니다."),


    // 호텔
    HOTEL_DUPLICATE(400, "H001", "이미 등록되어 있는 호텔입니다."),
    HOTEL_NOT_FOUND(404, "H002", "존재하지 않는 호텔입니다."),


    //객실관련
    ROOM_NOT_FOUND(404, "RM001", "존재하지 않는 객실입니다."),
    ROOM_NOT_AVAILABLE(409, "RM002", "죄송합니다. 해당 객실은 이미 만실입니다."),

    //예약
    RESERVATION_NOT_FOUND(404, "RV001", "존재하지 않는 예약건입니다"),
    INVALID_RESERVATION_STATE(400, "RVOO2", "이미 완료된 예약은 취소 불가입니다"),
    INVALID_RESERVATION_PERIOD(404, "RV003", "일부 날짜에 대한 객실 정보가 존재하지 않습니다"),

    //서버에러
    INTERNAL_SERVER_ERROR(500, "I001", "서비스 이용에 불편을 드려 죄송합니다.잠시 후 다시 시도해 주세요.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
