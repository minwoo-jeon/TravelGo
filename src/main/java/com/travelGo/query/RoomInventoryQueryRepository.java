package com.travelGo.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelGo.domain.QRoomInventory;
import com.travelGo.domain.RoomInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomInventoryQueryRepository {
    private final JPAQueryFactory queryFactory;


    public List<RoomInventory> findAllRoomIdAndDateRage(Long roomId, LocalDate checkIn, LocalDate checkOut) {

        QRoomInventory roomInventory = QRoomInventory.roomInventory;

        return queryFactory.
                selectFrom(roomInventory)
                .where(
                        roomInventory.room.id.eq(roomId),
                        roomInventory.date.goe(checkIn),
                        roomInventory.date.lt(checkOut)
                )
                .orderBy(roomInventory.date.asc())
                .fetch();
    }
}
