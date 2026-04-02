package com.travelGo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ReservationRequest {
    private Long hotelId;
    private Long roomId;
    private Long memberId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkIn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate checkOut;


    private int roomCount = 1;

}
