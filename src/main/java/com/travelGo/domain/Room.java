package com.travelGo.domain;


import com.travelGo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @OneToMany(mappedBy = "room" , cascade = CascadeType.ALL)
    private List<RoomInventory> inventories = new ArrayList<>();

    private String name;
    private int baseCapacity; //기준인원
    private int maxCapacity; //최대 입실인원
    private int totalCount; //객실 총 수량
    private int weekdayPrice; //주중가격
    private int weekendPrice; //주말격

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public static Room createRoom(Hotel hotel, String name, int baseCapacity, int maxCapacity, int totalCount, int weekendPrice, int weekdayPrice) {
        Room room = new Room();
        room.hotel = hotel;
        room.name = name;
        room.baseCapacity = baseCapacity;
        room.maxCapacity = maxCapacity;
        room.totalCount = totalCount;
        room.weekendPrice = weekendPrice;
        room.weekdayPrice = weekdayPrice;
        room.status = RoomStatus.OPEN;

        //2. 양방향 세팅
        room.changeHotel(hotel);

        //3. 해당 객실의 재고를 90일치를 생성한다
        room.generateInitialInventories(LocalDate.now(),90);

        return room;
    }


    // 스스로 재고를 생성하는 비지니스 로직
    public void generateInitialInventories(LocalDate startDate, int days) {

        // 오늘날짜부터 +90일 날짜를 추가한다
        for (int i = 0; i < days; i++) {
            LocalDate targetDate = startDate.plusDays(i);

            int price = isWeekend(targetDate) ? this.weekendPrice : this.weekdayPrice;
            RoomInventory inv = RoomInventory.createInventory(this, targetDate, price, this.totalCount);
            this.inventories.add(inv);
        }

    }


    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
    }

    /**
     * 객실 정보 수정(객실 총 수량 , 주말가격 , 주중가격) 만 수정 가능
     */
    public void updateRoomInfo(int totalCount,int weekdayPrice , int weekendPrice){
        this.totalCount =totalCount;
        this.weekdayPrice =weekdayPrice;
        this.weekendPrice = weekendPrice;
    }



    /**
     * 연관관계 편의 메서드
     */

    private void changeHotel(Hotel hotel) {
        this.hotel = hotel;
        hotel.getRooms().add(this);
    }



    // [핵심 로직] 연박 가능 여부 확인
    public boolean checkAvailability(LocalDate start, LocalDate end, int requested) {
        long stayNights = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        int matchedDays = 0;

        for (RoomInventory inv : this.inventories) {
            // 기간 내에 있고, 잔여 수량이 충분한지 확인
            if (!inv.getDate().isBefore(start) && inv.getDate().isBefore(end)) {
                if (inv.getAvailableCount() >= requested) {
                    matchedDays++;
                }
            }
        }
        // 모든 밤(Night)에 대한 재고가 완벽히 있어야 true
        return matchedDays == stayNights;
    }

    // 기간 내 총 요금 계산
    public int calculateTotalPrice(LocalDate start, LocalDate end) {
        int total = 0;
        for (RoomInventory inv : this.inventories) {
            if (!inv.getDate().isBefore(start) && inv.getDate().isBefore(end)) {
                total += inv.getSalesPrice();
            }
        }
        return total;
    }
}
