//package com.travelGo.config;
//
//import com.travelGo.domain.*;
//import com.travelGo.global.common.Address;
//import com.travelGo.repository.RoomRepository;
//import jakarta.annotation.PostConstruct;
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static com.travelGo.domain.Member.createMember;
//
//@Component
//@RequiredArgsConstructor
//public class InitDb {
//
//    private final InitService initService;
//
//    @PostConstruct
//    public void init() {
//        initService.dbInit1();
//    }
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitService {
//        private final EntityManager em;
//
//
//        public void dbInit1() {
//            // 1. 멤버 생성
//            Member member = createMember("test1@google.com", "1234", "김철수");
//            em.persist(member);
//
//            // 2. 호텔 생성 및 영속화 (주소는 각각 다르게 주는 것이 좋지만 일단 통일)
//            Address addr = new Address("서울", "중구 동호로 249", "04605");
//            Hotel hotel1 = Hotel.createHotel("서울 신라 호텔", addr,"12345");
//            Hotel hotel2 = Hotel.createHotel("서울 하얏트 호텔", addr,"12346");
//            Hotel hotel3 = Hotel.createHotel("부산 파라다이스", addr,"12347");
//
//            hotel1.setOwner(member);
//            hotel2.setOwner(member);
//            hotel3.setOwner(member);
//
//            em.persist(hotel1);
//            em.persist(hotel2);
//            em.persist(hotel3);
//
//            // 3. 각 호텔에 방 추가 (내부에서 90일치 인벤토리 자동 생성)
//            hotel1.addRoom("디럭스 더블", 2, 4, 5, 180000, 220000);
//            hotel1.addRoom("스탠다드 더블", 2, 4, 0, 180000, 220000);
//
//            hotel2.addRoom("스위트", 2, 6, 2, 350000, 420000);
//            hotel3.addRoom("디럭스 더블", 2, 4, 7, 200000, 250000);
//
//            // 중요: addRoom으로 생성된 Room과 Inventory들을 DB에 먼저 반영해야 ID가 생깁니다.
//            em.flush();
//
//            // 4. N+1 시뮬레이션용 예약 생성 (서로 다른 방을 예약해야 쿼리가 여러 번 나갑니다)
//            // r1, r2, r3는 각각 다른 호텔의 방입니다.
//            createReservation(member,hotel1, hotel1.getRooms().get(0), 1);
//            createReservation(member,hotel2, hotel2.getRooms().get(0), 2);
//            createReservation(member,hotel3, hotel3.getRooms().get(0), 3);
//
//            System.out.println(">>> [TravelGo] 테스트 데이터 생성 완료 (Member, Hotel 3곳, 예약 3건)");
//        }
//
//        /**
//         * 예약 생성 편의 메서드
//         */
//        private void createReservation(Member member,Hotel hotel, Room room, int offset) {
//            LocalDate checkIn = LocalDate.now().plusDays(offset);
//            LocalDate checkOut = LocalDate.now().plusDays(offset + 1);
//
//
//
//            // 이미 생성된 90일치 재고 중 해당 날짜에 맞는 것만 필터링
//            List<RoomInventory> targetInventories = room.getInventories().stream()
//                    .filter(inv -> !inv.getDate().isBefore(checkIn) && inv.getDate().isBefore(checkOut))
//                    .toList();
//
//            Reservation reservation = Reservation.createReservation(
//                    targetInventories,
//                    hotel,
//                    member,
//                    room,
//                    checkIn,
//                    checkOut,
//                    1
//            );
//
//            em.persist(reservation);
//        }
//    }
//}