package com.travelGo.domain;


import com.travelGo.global.common.BaseEntity;
import com.travelGo.global.common.ErrorCode;
import com.travelGo.global.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Getter
@Slf4j
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id") //내 테이블에 fk컬럼을 만들건데, 이름은 hotel_id라고지어
    private Hotel hotel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private int reservationCount; //예약한 객실 수량

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private int totalPrice;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    protected Reservation() {
    }

    /**
     * 예약건 생성
     */
    public static Reservation createReservation(List<RoomInventory> inventories, Hotel hotel, Member member, Room room, LocalDate checkInDate, LocalDate checkOutDate, int count) {
        log.info("[Reservation Start] memberId: {}, roomId: {}, period: {} ~ {}, count: {}",
                member.getId(), room.getId(), checkInDate, checkOutDate, count);

        //1. 날짜검증 (연박 데이터까지 정확히 가져오는지)
        validateDateRange(inventories, checkInDate, checkOutDate);

        //2. 제고 검증
        validateStock(inventories, count);

        //3. 제고 차감
        decreaseStock(inventories, count);

        //4. 에약 생성
        Reservation reservation = new Reservation();
        reservation.hotel = hotel;
        reservation.member = member;
        reservation.room = room;
        reservation.checkInDate = checkInDate;
        reservation.checkOutDate = checkOutDate;
        reservation.status = ReservationStatus.CONFIRMED;
        reservation.reservationCount = count;


        return reservation;
    }



    private static void decreaseStock(List<RoomInventory> inventories, int count) {
        for (RoomInventory inventory : inventories) {
            inventory.decreaseStock(count);
        }
    }

    /**
     * 제고 검증
     */
    private static void validateStock(List<RoomInventory> inventories, int count) {
        for (RoomInventory inventory : inventories) {
            if(inventory.getAvailableCount() < count){
                log.warn("[Stock Shortage] Date: {}, Available: {}, Requested: {}",
                        inventory.getDate(), inventory.getAvailableCount(), count);

                throw new BusinessException(ErrorCode.ROOM_NOT_AVAILABLE);
            }
        }
    }


    /**
     * 날짜 검증(연박 데이터가 잘 값을 가져오는지 확인 )
     */
    private static void validateDateRange(List<RoomInventory> inventoryList, LocalDate checkIn, LocalDate checkOut){

        // 1) 요청 날짜 수 계산
        long expectedDays  = checkIn.datesUntil(checkOut).count();

        if (inventoryList.size() != expectedDays) {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_PERIOD);
        }
    }

    /**
     *
     */


    /**
     * 예약 취소
     */
    public Long cancelReservation(){

        // 1. 예약이 완료된 상태면 취소 불가
        if(status.equals(ReservationStatus.COMPLETED)){
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATE);
        }

        // 2. 재고 복구 (예약 시 점유했던 인벤토리들 대상)


        // 3. 예약건 취소
        this.status = ReservationStatus.CANCELLED;

        return id;
    }
}
