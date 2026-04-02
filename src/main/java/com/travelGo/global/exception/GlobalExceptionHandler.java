package com.travelGo.global.exception;

import com.travelGo.global.common.ApiResponse;
import com.travelGo.global.common.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        log.warn("[Validation Failed] : {}", errorMessage);

        // 2. 미리 정의한 ApiResponse 구조에 맞춰서 반환
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT_VALUE));
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e, HttpServletRequest request) {
                log.error("Business Error: Code={}, Detail={}, Path={}",
                        e.getErrorCode(), request.getRequestURI(),e);

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(e.getErrorCode()));
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse<?>> handleException(Exception e, HttpServletRequest request) {

        log.error("[Error] URL: {}, Method: {}", request.getRequestURI(), request.getMethod(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
