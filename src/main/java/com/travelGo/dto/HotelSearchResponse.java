package com.travelGo.dto;

import com.travelGo.domain.Hotel;
import com.travelGo.global.common.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchResponse {
    private Long hotelId;
    private String hotelName;
    private Address address;

    public HotelSearchResponse(Hotel hotel) {
        this.hotelId = hotel.getId();
        this.hotelName = hotel.getName();
        this.address = hotel.getAddress();
    }
}
