package com.travelGo.controller;

import com.travelGo.domain.Hotel;
import com.travelGo.dto.HotelDto;
import com.travelGo.dto.HotelSearchResponse;
import com.travelGo.global.common.ApiResponse;
import com.travelGo.repository.HotelRepository;
import com.travelGo.repository.MemberRepository;
import com.travelGo.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


import static com.travelGo.dto.HotelDto.*;

@Slf4j
@RestController
@RequestMapping("/api/stays")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @Operation(
            summary = "호텔 및 객실 등록 (90일 재고 생성)",
            description = "새로운 호텔 정보와 객실 타입을 등록하며, 등록 시점부터 향후 90일간의 객실 재고(Inventory)를 자동으로 생성합니다."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<HotelResponse>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 호텔, 주소 및 객실 상세 정보", required = true)
            @Valid @RequestBody CreateRequest form  //
    ) {
        // 서비스 로직에서 호텔 저장 -> 객실 저장 -> 90일치 재고 생성이 진행됩니다.
        Hotel hotel = hotelService.registerHotelAndRoom(form);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("객실 등록 성공", new HotelResponse(hotel)));
    }




    @Operation(summary = "호텔 통합 검색",
            description = "도시 이름과 체크인/체크아웃 날짜를 기준으로 예약 가능한 호텔 목록을 조회합니다."
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<HotelSearchResponse>>> search(
            @Parameter(description = "검색할 도시명", example = "서울")
            @RequestParam(value = "city") String city,

            @Parameter(description = "체크인 날짜", example = "2026-03-20")
            @RequestParam(value = "checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,

            @Parameter(description = "체크아웃 날짜", example = "2026-03-22")
            @RequestParam(value = "checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut,

            @Parameter(description = "객실 수량", example = "1")
            @RequestParam(value = "roomCount") int roomCount,

            @RequestParam(value = "page", defaultValue = "0") int page,  // 페이지
            @RequestParam(value = "size", defaultValue = "10") int size  // 페이지 당 개수
            )
    {
        List<HotelSearchResponse>  list=  hotelService.getAvailableHotels(city, checkIn, checkOut,roomCount,page,size);
        return ResponseEntity.ok(ApiResponse.success("객실 조회성공", list));
    }
}
