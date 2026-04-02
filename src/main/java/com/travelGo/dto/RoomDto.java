package com.travelGo.dto;

import com.travelGo.domain.Room;
import lombok.Getter;

import java.util.List;

public class RoomDto {
    /**
     * 객실 정보 수정
     */

    @Getter
    public static class RoomUpdateDto {
        private int totalCount;
        private int weekdayPrice;
        private int weekendPrice;
    }

    @Getter
    public static class UpdateRoomResponse {
        private Long id;
        private String name;
        private int totalCount;
        private int weekdayPrice;
        private int weekendPrice;

        public UpdateRoomResponse(Room room) {
            this.id = room.getId();
            this.name = room.getName();
            this.totalCount = room.getTotalCount();
            this.weekdayPrice = room.getWeekdayPrice();
            this.weekendPrice = room.getWeekendPrice();
        }
    }

    @Getter
    public static class RoomSearchResponse {
        private Long hotelId;
        private String hotelName;
        private List<RoomTypeDto> roomTypes;


        public RoomSearchResponse(Long hotelId,String hotelName, List<RoomTypeDto> roomTypes) {
            this.hotelId = hotelId;
            this.hotelName = hotelName;
            this.roomTypes = roomTypes;
        }
    }
}
