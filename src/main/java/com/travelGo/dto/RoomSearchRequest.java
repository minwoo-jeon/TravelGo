package com.travelGo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "객실 검색 요청 파라미터")
public class RoomSearchRequest {
    @NotNull(message = "체크인 날짜는 필수입니다.")
    @FutureOrPresent(message = "체크인 날짜는 오늘 또는 미래 날짜여야 합니다.") // 과거 날짜 차단
    @Schema(description = "체크인 날짜", example = "2026-03-20")
    private LocalDate checkIn;

    @NotNull(message = "체크아웃 날짜는 필수입니다.")
    @Schema(description = "체크아웃 날짜", example = "2026-03-22")
    private LocalDate checkOut;

    @Min(value = 1, message = "객실 수량은 최소 1개 이상이어야 합니다.")
    @Schema(description = "객실 수량", example = "1")
    private int rooms = 1;

    // 추가 검증: 체크인 < 체크아웃 로직
    @AssertTrue(message = "체크아웃 날짜는 체크인 날짜보다 나중이어야 합니다.")
    public boolean isValidPeriod() {
        if (checkIn == null || checkOut == null) return false;
        return checkOut.isAfter(checkIn);
    }
}
