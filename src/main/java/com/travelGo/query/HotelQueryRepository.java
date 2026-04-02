package com.travelGo.query;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelGo.domain.*;
import com.travelGo.dto.HotelSearchResponse;
import com.travelGo.global.common.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.travelGo.domain.QHotel.*;
import static com.travelGo.domain.QRoom.room;
import static com.travelGo.domain.QRoomInventory.roomInventory;

@Repository
@RequiredArgsConstructor

public class HotelQueryRepository {
    private final JPAQueryFactory queryFactory;



    public List<Hotel> findAvailableHotels(String city, LocalDate checkIn, LocalDate checkOut,int rooms,int page, int size) {
        // 숙박 일수 계산 (예: 20일~22일이면 2박)
        long nightCount = ChronoUnit.DAYS.between(checkIn, checkOut);
        long offset = (long) page * size; //0 * 10

        return queryFactory
                .selectDistinct(hotel)
                .from(hotel)
                .join(hotel.rooms, room)
                .join(room.inventories, roomInventory)
                .where(
                        hotel.address.city.eq(city),  //1. 도시 필터링
                        roomInventory.date.goe(checkIn),  //2. 날짜 필터링
                        roomInventory.date.lt(checkOut),
                        roomInventory.availableCount.goe(rooms) //3. 객실 예약 수량이> 요청 소량보다 커야함
                )
                .groupBy(hotel.id, room.id)// 호텔별 ,방별
                .having(roomInventory.count().eq(nightCount))
                .offset(offset)   // ★ page * size
                .limit(size)
                .fetch();
    }
}
