package com.travelGo.controller;


import com.travelGo.domain.Reservation;
import com.travelGo.dto.ReservationRequest;
import com.travelGo.global.common.ApiResponse;
import com.travelGo.repository.ReservationRepository;
import com.travelGo.service.ReservationService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;


    /**
     * 에약 생성
     * http://localhost:8080/api/v1/reservations
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> reservation(@RequestBody ReservationRequest request) {
        long reservationId = reservationService.createReservation(request.getMemberId(), request.getHotelId(),
                                                                    request.getRoomId(), request.getCheckIn(),
                                                                    request.getCheckOut(), request.getRoomCount());

        return ResponseEntity.ok(ApiResponse.success("객실 예약 성공", reservationId));
    }

    /**
     * 예약 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservationsWithNPlusOne() {

        // 1. 예약 목록 조회 (쿼리 1번)
        List<Reservation> reservations = reservationRepository.findAll();

        // 2. 내부 클래스 DTO로 변환
        List<ReservationResponse> response =  reservations.stream()
                .map(ReservationResponse::new)
                .toList();

        return ResponseEntity.ok(ApiResponse.success("예약건 전체 조회 성공",response));
    }

    /**
     * 예약 상세 조회
     * http://localhost:8080/api/v1/reservations/1
     */
    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse> getReservationDetail(@PathVariable(value = "reservationId") Long id) {
        Reservation oneDetailReservation = reservationService.getOneDetailReservation(id);

        ReservationDetailResponse response = new ReservationDetailResponse(oneDetailReservation);

        // 2. 성공 응답 반환
        return ResponseEntity.ok(ApiResponse.success("예약 상세 조회 성공",response));
    }


    /**
     * 예약 취소 http://localhost:8080/api/v1/reservations/1/cancel
     */
    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable(value = "reservationId") Long reservationId ) {
        Long cancelId = reservationService.cancelReservation(reservationId);

        return ResponseEntity.ok(ApiResponse.success("예약 취소 완료",cancelId));
    }


    /**
     * 마이페이지 예약 정보 전체 조회(호털이름,체크인,체크아웃,예약 상태(취소,완료))
     */

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ReservationResponse {
        private Long reservationId;
        private String hotelName;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private String reservationStatus;

        public ReservationResponse(Reservation reservation) {
            this.reservationId = reservation.getId();
            this.hotelName = reservation.getRoom().getHotel().getName(); // fetchJoin 필수!
            this.checkInDate = reservation.getCheckInDate();
            this.checkOutDate = reservation.getCheckOutDate();
            this.reservationStatus = reservation.getStatus().name();
        }
    }



    /**
     * 예약건 상세 조회 (호텔이름,객실이름, 객실예약수량, 예약당시 총가격,체크인/체크아웃, 예약상태)
     */
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ReservationDetailResponse {
        private Long reservationId;
        private String hotelName;
        private String roomName;
        private int reservationCount;
        private int totalPrice;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private String reservationStatus;

        public ReservationDetailResponse(Reservation reservation) {
            this.reservationId = reservation.getId();
            this.hotelName = reservation.getRoom().getHotel().getName(); // fetchJoin 필수
            this.roomName = reservation.getRoom().getName();
            this.reservationCount = reservation.getReservationCount();
            this.totalPrice = reservation.getTotalPrice(); // Long일 경우 int로 변환
            this.checkInDate = reservation.getCheckInDate();
            this.checkOutDate = reservation.getCheckOutDate();
            this.reservationStatus = reservation.getStatus().name();
        }
    }
}
