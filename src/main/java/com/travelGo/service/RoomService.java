package com.travelGo.service;

import com.travelGo.domain.Hotel;
import com.travelGo.domain.Room;
import com.travelGo.dto.RoomTypeDto;
import com.travelGo.global.common.ErrorCode;
import com.travelGo.global.exception.BusinessException;
import com.travelGo.query.RoomQueryRepository;
import com.travelGo.repository.HotelRepository;
import com.travelGo.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.travelGo.dto.RoomDto.*;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomQueryRepository roomQueryRepository;
    private final HotelRepository hotelRepository;

    /**
     * 객실 정보 수정
     */
    @Transactional
    public UpdateRoomResponse updateRoomInfo(Long hotelId, Long roomId, RoomUpdateDto updateForm) {

        Room room = roomRepository.findRoomInHotel(hotelId, roomId).orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        room.updateRoomInfo(updateForm.getTotalCount(), updateForm.getWeekdayPrice(), updateForm.getWeekendPrice());

        return new UpdateRoomResponse(room);
    }

    /**
     * 사용자가 요청한 호텔의 가능한 객실 조회
     */
    public RoomSearchResponse  getRoomsDetail(Long hotelId, LocalDate checkIn, LocalDate checkOut, int count) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new BusinessException(ErrorCode.HOTEL_NOT_FOUND));


        // 1. 모든 객실 데이터 로드 (마감된 방 포함)
        List<Room> rooms = roomQueryRepository.findRoomsWithInventories(hotelId, checkIn, checkOut);


        List<RoomTypeDto> roomTypeDtos = new ArrayList<>();

        // 2. 반복문으로 상태 확인
        for (Room room : rooms) {
            boolean isAvailable = room.checkAvailability(checkIn, checkOut, count);
            int totalPrice = room.calculateTotalPrice(checkIn, checkOut);

            // 3. DTO 생성
            RoomTypeDto dto = new RoomTypeDto();
            dto.setRoomId(room.getId());
            dto.setRoomName(room.getName());
            dto.setTotalPrice(totalPrice);
            dto.setAvailable(isAvailable);

            dto.setStatusMessage(isAvailable ? "예약 가능" : "판매 마감");

            roomTypeDtos.add(dto);
        }

        return new RoomSearchResponse(hotelId, hotel.getName(), roomTypeDtos);
    }
}

