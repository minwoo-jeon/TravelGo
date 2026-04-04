package com.travelGo.controller;

import com.travelGo.domain.Reservation;
import com.travelGo.dto.ReservationRequest;
import com.travelGo.global.common.ApiResponse;
import com.travelGo.repository.ReservationRepository;
import com.travelGo.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController // @Controller에서 @RestController로 변경 (API 중심)
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
@Tag(name = "Reservation API", description = "예약 생성, 조회 및 취소 API")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @Operation(summary = "객실 예약 생성", description = "회원 정보와 객실 정보를 이용해 새로운 예약을 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> reservation(
            @RequestBody ReservationRequest request) {

        long reservationId = reservationService.createReservation(
                request.getMemberId(), request.getHotelId(),
                request.getRoomId(), request.getCheckIn(),
                request.getCheckOut(), request.getRoomCount());

        return ResponseEntity.ok(ApiResponse.success("객실 예약 성공", reservationId));
    }

    @Operation(summary = "예약 목록 전체 조회", description = "사용자의 전체 예약 내역을 조회합니다. (마이페이지용)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getReservations() {
        // 실제 서비스에서는 memberId 등으로 필터링이 필요할 수 있습니다.
        List<Reservation> reservations = reservationRepository.findAll();

        List<ReservationResponse> response = reservations.stream()
                .map(ReservationResponse::new)
                .toList();

        return ResponseEntity.ok(ApiResponse.success("예약건 전체 조회 성공", response));
    }

    @Operation(summary = "예약 상세 조회", description = "특정 예약의 상세 정보(호텔명, 객실명, 가격 등)를 조회합니다.")
    @GetMapping("/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationDetailResponse>> getReservationDetail(
            @Parameter(description = "예약 고유 ID", example = "1")
            @PathVariable(value = "reservationId") Long id) {

        Reservation oneDetailReservation = reservationService.getOneDetailReservation(id);
        ReservationDetailResponse response = new ReservationDetailResponse(oneDetailReservation);

        return ResponseEntity.ok(ApiResponse.success("예약 상세 조회 성공", response));
    }

    @Operation(summary = "예약 취소", description = "진행 중인 예약을 취소 상태로 변경합니다.")
    @PostMapping("/{reservationId}/cancel")
    public ResponseEntity<ApiResponse<Long>> cancelReservation(
            @Parameter(description = "취소할 예약 고유 ID", example = "1")
            @PathVariable(value = "reservationId") Long reservationId) {

        Long cancelId = reservationService.cancelReservation(reservationId);

        return ResponseEntity.ok(ApiResponse.success("예약 취소 완료", cancelId));
    }

    // --- 내부 DTO 클래스들 (Swagger 문서화를 위해 필드 설명 생략 가능하나 필요시 추가) ---

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ReservationResponse {
        private Long reservationId;
        private String hotelName;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private String reservationStatus;

        public ReservationResponse(Reservation reservation) {
            this.reservationId = reservation.getId();
            this.hotelName = reservation.getRoom().getHotel().getName();
            this.checkInDate = reservation.getCheckInDate();
            this.checkOutDate = reservation.getCheckOutDate();
            this.reservationStatus = reservation.getStatus().name();
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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
            this.hotelName = reservation.getRoom().getHotel().getName();
            this.roomName = reservation.getRoom().getName();
            this.reservationCount = reservation.getReservationCount();
            this.totalPrice = reservation.getTotalPrice();
            this.checkInDate = reservation.getCheckInDate();
            this.checkOutDate = reservation.getCheckOutDate();
            this.reservationStatus = reservation.getStatus().name();
        }
    }
}