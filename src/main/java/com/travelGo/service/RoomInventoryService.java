package com.travelGo.service;

import com.travelGo.domain.RoomInventory;
import com.travelGo.query.RoomInventoryQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomInventoryService {

    private final RoomInventoryQueryRepository roomInventoryQueryRepository;


    public List<RoomInventory> getStayPeriodInventories(Long roomId, LocalDate checkIn, LocalDate checkOut){
        log.info("객실 재고 조회 요청 - roomId: {}, checkIn: {}, checkOut: {}", roomId, checkIn, checkOut);
        return roomInventoryQueryRepository.findAllRoomIdAndDateRage(
                roomId,
                checkIn,
                checkOut
        );
    }

}
