package com.travelGo.dto;

import com.travelGo.domain.Hotel;
import com.travelGo.global.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class HotelDto {

    /**
     * 호텔 등록 요청 DTO
     */
    @Getter
    @NoArgsConstructor
    @Schema(description = "호텔 등록 요청 정보")
    public static class CreateRequest {

        @NotBlank(message = "파트너 ID는 필수입니다.")
        @Schema(description = "등록자(파트너) ID", example = "1")
        private String memberId;


        @NotBlank(message = "사업자 등록번호.")
        @Schema(description = "사업자 등록번호")
        private String businessNumber;

        @NotBlank(message = "호텔 이름은 필수입니다.")
        @Schema(description = "호텔 이름", example = "파크 호텔")
        private String name;

        @NotNull(message = "호텔 주소 정보는 필수입니다.")
        @Valid // 내부에 있는 Address 필드들도 검증하기 위해 필수!
        @Schema(description = "호텔 주소 정보")
        private Address address;

        @NotEmpty(message = "최소 하나 이상의 객실을 등록해야 합니다.")
        @Valid // 리스트 안의 RoomCreateRequest 객체들을 하나하나 검증하기 위해 필수!
        @Schema(description = "등록할 객실 목록")
        private List<RoomCreateRequest> rooms;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "객실 등록 상세 정보")
    public static class RoomCreateRequest {

        @NotBlank(message = "객실 이름은 필수입니다.")
        @Schema(description = "객실 이름", example = "스탠다드 더블")
        private String name;

        @Min(value = 1, message = "기준 인원은 1명 이상이어야 합니다.")
        @Schema(description = "기준 인원", example = "2")
        private int baseCapacity;

        @Positive(message = "최대 인원은 양수여야 합니다.")
        @Schema(description = "최대 수용 인원", example = "4")
        private int maxCapacity;

        @Min(value = 1, message = "객실 수량은 최소 1개 이상이어야 합니다.")
        @Schema(description = "해당 타입의 전체 객실 수량", example = "15")
        private int totalCount;

        @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
        @Schema(description = "평일 1박 가격", example = "120000")
        private int weekDayPrice;

        @PositiveOrZero(message = "가격은 0원 이상이어야 합니다.")
        @Schema(description = "주말 1박 가격", example = "180000")
        private int weekendPrice;
    }
    /**
     * 호텔 등록 완료 응답 DTO
     */
    @Getter
    @Schema(description = "호텔 등록 결과 응답")
    public static class HotelResponse {

        @Schema(description = "생성된 호텔 고유 ID", example = "1")
        private Long id;

        @Schema(description = "등록된 호텔 이름", example = "파크 호텔")
        private String name;

        public HotelResponse(Hotel hotel) {
            this.id = hotel.getId();
            this.name = hotel.getName();
        }
    }
}