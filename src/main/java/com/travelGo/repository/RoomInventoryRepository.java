package com.travelGo.repository;

import com.travelGo.domain.RoomInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInventoryRepository extends JpaRepository<RoomInventory, Long> {

}
