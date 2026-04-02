package com.travelGo.repository;


import com.travelGo.domain.Hotel;
import com.travelGo.global.common.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>  {

    boolean existsByBusinessNumber(String businessNumber);






}
