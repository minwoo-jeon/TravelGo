package com.travelGo.controller;


import com.travelGo.dto.RoomSearchRequest;
import com.travelGo.global.common.ApiResponse;
import com.travelGo.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import static com.travelGo.dto.RoomDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hotels")
@Tag(name = "Room API", description = "객실 정보 및 객실 관련 API")
public class RoomController {
    private final RoomService roomService;


    /**
     * 객실 정보 수정
     */
    @Operation(summary = "객실 정보 수정", description = "특정 호텔의 객실 정보를 업데이트합니다.")
    @PatchMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<ApiResponse<UpdateRoomResponse>> modify(@PathVariable(value = "hotelId") Long hotelId, @PathVariable(value = "roomId") Long roomId,
                                                     @RequestBody RoomUpdateDto updateForm) {
        UpdateRoomResponse response = roomService.updateRoomInfo(hotelId, roomId, updateForm);

        return ResponseEntity.ok(ApiResponse.success("객실 정보 수정 완료", response));
    }

    /**
     * 사용자가 특정 호텔(예)신라호텔) 클릭했을떄 해당 호텔의 객실룸타입 조회
     */
    @Operation(
            summary = "예약 가능 객실 조회",
            description = "특정 호텔의 상세 정보와 선택한 날짜에 예약 가능한 객실 목록을 조회합니다."
    )

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<ApiResponse<RoomSearchResponse>> searchRooms(
            @Parameter(description = "호텔 고유 ID", example = "1")
            @PathVariable(value = "hotelId") Long hotelId,

            @Valid @ModelAttribute RoomSearchRequest request
    ) {
        RoomSearchResponse response = roomService.getRoomsDetail(
                hotelId,
                request.getCheckIn(),
                request.getCheckOut(),
                request.getRooms()
        );

        return ResponseEntity.ok(ApiResponse.success("객실 조회 성공" ,response));
    }
}
