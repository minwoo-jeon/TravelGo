package com.travelGo.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.travelGo.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.travelGo.domain.QHotel.hotel;
import static com.travelGo.domain.QReservation.reservation;
import static com.travelGo.domain.QRoom.room;

@Repository
@RequiredArgsConstructor
public class ReservationQueryRepository {

    private final JPAQueryFactory queryFactory;




    /**
     * 회원의 예약건 전체조회 (test 회원은 생략 )
     */

    public List<Reservation> findAll() {
        return queryFactory
                .selectFrom(reservation)
                .join(reservation.room,room).fetchJoin()
                .join(room.hotel,hotel).fetchJoin()
                .fetch();
    }

    /**
     *
     * 단건 조회
     */

    public Optional<Reservation> findDetailById(Long reservationId){
        Reservation result = queryFactory
                .selectFrom(reservation)
                .join(reservation.room, room).fetchJoin()
                .join(room.hotel, hotel).fetchJoin()
                .where(reservation.id.eq(reservationId))
                .fetchOne();
        return Optional.ofNullable(result); // 단건 조회
    }

}
