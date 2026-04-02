package com.travelGo.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private String errorCode;
    private String errorMessage;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse<?> error(ErrorCode errorCode) {
        return ApiResponse.builder()
                .status(errorCode.getStatus())
                .errorCode(errorCode.name())
                .errorMessage(errorCode.getMessage())
                .build();
    }

}
