package com.travelGo.domain;

import com.travelGo.global.common.BaseEntity;
import com.travelGo.global.common.ErrorCode;
import com.travelGo.global.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "room_inventory", uniqueConstraints = {
        @UniqueConstraint(name = "uk_room_inventory_room_date", columnNames = {"room_id", "inventory_date"})
})
public class RoomInventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_inventory_id")
    private Long id;

    @Column(name = "inventory_date", nullable = false)
    private LocalDate date;

    private int salesPrice; //객실 가격

    private int totalCount; //해당 날짜의 객실 총 수량

    @Column(nullable = false)
    private int availableCount; //객실 제고 남은 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public static RoomInventory createInventory(Room room, LocalDate targetDate, int price, int totalCount) {
        RoomInventory inventory = new RoomInventory();
        inventory.room = room;
        inventory.date = targetDate;
        inventory.salesPrice = price;
        inventory.totalCount = totalCount;
        inventory.availableCount = totalCount;
        return inventory;
    }


    private boolean isAvailable(int requestedCount) {
        return (this.availableCount - requestedCount) >= 0;
    }


    /**
     * 제고 감소
     */
    public void decreaseStock(int count) {
        int restStock = this.availableCount - count;
        if (restStock < 0) {
            throw new BusinessException(ErrorCode.ROOM_NOT_AVAILABLE);
        }

        this.availableCount = restStock;
    }

    /**
     * 취소시 제고 증가
     */

    public void increaseStock(int reservationCount) {
        availableCount += reservationCount;
    }
}
