package com.travelGo.service;


import com.fasterxml.jackson.core.Base64Variant;
import com.travelGo.domain.*;
import com.travelGo.global.common.ErrorCode;
import com.travelGo.global.exception.BusinessException;
import com.travelGo.query.ReservationQueryRepository;
import com.travelGo.query.RoomInventoryQueryRepository;
import com.travelGo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.travelGo.global.common.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final HotelRepository hotelRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final RoomInventoryService roomInventoryService;

    //QueryDSL
    private final ReservationQueryRepository reservationQueryRepository;


    /**
     * 예약 생성 (호텔 -> 객실 -> 디테일 룸타입 클릭 발생시(ex: 스탠다드트윈) )
     */
    @Transactional
    public long createReservation(Long memberId, Long hotelId, Long roomId, LocalDate checkIn, LocalDate checkOut, int count) {
        //1. 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new BusinessException(MEMBER_NOT_FOUND));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()->new BusinessException(ROOM_NOT_FOUND));
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new BusinessException(HOTEL_NOT_FOUND));

        //2. 제고가 있는 객실 날짜들을 가져온다.
        List<RoomInventory> allRoomIdAndDateRage = roomInventoryService.getStayPeriodInventories(room.getId(), checkIn, checkOut);



        //3. 예약건을 생성 (제고 검증은 -> )
        Reservation reservation = Reservation.createReservation(allRoomIdAndDateRage, hotel,member, room, checkIn, checkOut, count);

        //4. 엔티티 저장
        Reservation createReservation = reservationRepository.save(reservation);

        return createReservation.getId();
    }

    /**
     * 해당 사용자의 전체 예약건 조회
     */

    public List<Reservation>  getAllReservation( ) {

        // 1. 예약 목록 조회 (쿼리 1번)
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations;
    }


    /**
     * 특정 예약건 1건 조회
     */
    public Reservation getOneDetailReservation(Long ReservationId){
        Reservation reservation = reservationQueryRepository.findDetailById(ReservationId)
                .orElseThrow(()-> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
                return reservation;
    }

    /**
     * 예약건 취소
     */
    @Transactional
    public Long cancelReservation(Long reservationId){
        Reservation reservation = reservationQueryRepository.findDetailById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));

      return reservation.cancelReservation();

    }

}



