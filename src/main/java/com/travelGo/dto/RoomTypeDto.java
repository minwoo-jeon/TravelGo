package com.travelGo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomTypeDto {
    private Long roomId;
    private String roomName;
    private int totalPrice;
    private boolean isAvailable;
    private String statusMessage;

}
