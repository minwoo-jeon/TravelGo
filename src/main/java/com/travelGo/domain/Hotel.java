package com.travelGo.domain;


import com.travelGo.global.common.Address;
import com.travelGo.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Hotel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long id;

    @Column(nullable = false, unique = true) //새로 추가한 컬럼이라  기존에 이미 테스트데이터에 들어가있어서 지금은 null허용
    private String businessNumber;//사업자 번호 (유니크 제약조건 추가)

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "hotel" ,cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    private int starRating; //호텔 성급.


    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    public static Hotel createHotel(String name, Address address, String businessNumber) {
        Hotel hotel = new Hotel();
        hotel.name = name;
        hotel.businessNumber = businessNumber;
        hotel.address = address;
        return hotel;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public void addRoom(String name, int baseCapacity, int maxCapacity, int totalCount, int weekdayPrice, int weekendPrice){
        //1. Room 생성(이떄 room 내부에는 hotel이 세팅됌)
         Room.createRoom(this, name, baseCapacity, maxCapacity, totalCount, weekdayPrice, weekdayPrice);
    }

    public void updateInfo(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
