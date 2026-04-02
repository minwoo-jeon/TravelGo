package com.travelGo.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelGo.domain.QRoom;
import com.travelGo.domain.QRoomInventory;
import com.travelGo.domain.Room;
import com.travelGo.domain.RoomStatus;
import com.travelGo.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.travelGo.domain.QRoom.room;
import static com.travelGo.domain.QRoomInventory.roomInventory;

@Repository
@RequiredArgsConstructor
public class RoomQueryRepository {

    private final JPAQueryFactory queryFactory;


        // 재고 데이터도 해당 기간 것만 조인되도록 필터링
        public List<Room> findRoomsWithInventories(Long hotelId, LocalDate start, LocalDate end) {
            return queryFactory
                    .selectFrom(room)
                    .leftJoin(room.inventories, roomInventory).fetchJoin()
                    .where(
                            room.hotel.id.eq(hotelId),
                            roomInventory.date.goe(start),
                            roomInventory.date.lt(end)
                    )
                    .fetch();
        }
    }

