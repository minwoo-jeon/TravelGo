package com.travelGo.global.exception;

import com.travelGo.global.common.ErrorCode;
import lombok.Getter;

@Getter
public  class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    // ErrorCode의 기본 메시지만 사용하는 경우
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }


    // 원인 예외(Cause)를 보존해야 하는 경우
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(),cause);
        this.errorCode = errorCode;
    }

}
