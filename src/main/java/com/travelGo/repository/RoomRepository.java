package com.travelGo.repository;


import com.travelGo.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    //객실 수정할떄 해당 호텔의 객실 가져오는
    @Query("select r from Room r where r.hotel.id = :hotelId and r.id = :roomId")
    Optional<Room> findRoomInHotel(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);
}
