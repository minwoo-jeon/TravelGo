package com.travelGo.service;

import com.travelGo.domain.*;
import com.travelGo.global.common.Address;
import com.travelGo.global.common.ErrorCode;
import com.travelGo.global.exception.BusinessException;
import com.travelGo.query.RoomInventoryQueryRepository;
import com.travelGo.repository.*;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ReservationServiceTest {
    @Autowired private ReservationService reservationService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private HotelRepository hotelRepository;
    @Autowired private EntityManager em;

    private Member testMember;
    private Hotel testHotel;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        // 1. 멤버 생성
        testMember = Member.createMember("test@google.com", "1234", "테스터");
        em.persist(testMember);

        // 2. 호텔 생성
        Address addr = new Address("서울", "중구", "12345");
        testHotel = Hotel.createHotel("테스트 호텔", addr,"12312314");
        testHotel.setOwner(testMember);
        em.persist(testHotel);

        // 3. 방 추가 (InitDb 로직처럼 내부에서 90일치 인벤토리가 자동 생성됨)
        // addRoom(이름, 기준인원, 최대인원, 재고수량, 평일가, 주말가)
        testHotel.addRoom("디럭스", 2, 4, 10, 100000, 150000);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("성공: 오늘부터 2박 3일 예약을 진행하면 재고가 정상 차감된다")
    void createReservation_Success() {
        // Given
        LocalDate today = LocalDate.now();
        LocalDate checkIn = today;
        LocalDate checkOut = today.plusDays(2); // 2박 (오늘, 내일 재고 사용)
        int reserveCount = 1;

        Hotel hotel = hotelRepository.findById(testHotel.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.HOTEL_NOT_FOUND));

        for (Room rooms : hotel.getRooms()) {
            testRoom = rooms;
        }

        // When
        long reservationId = reservationService.createReservation(
                testMember.getId(), testHotel.getId(), testRoom.getId(), checkIn, checkOut, reserveCount);

        em.flush();
        em.clear();

        // Then
        Reservation result = em.find(Reservation.class, reservationId);

        // 1) 예약 상태 확인 (AssertJ assertThat 사용)
        assertThat(result.getReservationCount()).isEqualTo(reserveCount);
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(result.getCheckInDate()).isEqualTo(checkIn);

        // 2) 재고 차감 확인 (오늘 날짜 재고가 10 -> 9가 되었는지)
        RoomInventory todayInv = result.getRoom().getInventories().stream()
                .filter(inv -> inv.getDate().equals(today))
                .findFirst()
                .orElseThrow();

        assertThat(todayInv.getAvailableCount()).isEqualTo(9);
    }


}

