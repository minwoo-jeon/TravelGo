package com.travelGo.service;

import com.travelGo.domain.Hotel;
import com.travelGo.dto.HotelDto;
import com.travelGo.dto.HotelSearchResponse;
import com.travelGo.global.common.Address;
import com.travelGo.global.exception.BusinessException;
import com.travelGo.query.HotelQueryRepository;
import com.travelGo.repository.HotelRepository;
import com.travelGo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.travelGo.global.common.ErrorCode.HOTEL_DUPLICATE;
import static com.travelGo.global.common.ErrorCode.HOTEL_NOT_FOUND;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelQueryRepository hotelQueryRepository;


    /**
     *
     * 호텔, 객실등록 하면 기본 +90일 재고가 생성됌
     */
    @Transactional
    public Hotel registerHotelAndRoom(HotelDto.CreateRequest form) {

        validateDuplicateHotel(form.getBusinessNumber());

        //호텔 생성
        Hotel hotel = Hotel.createHotel(form.getName(), form.getAddress(), form.getBusinessNumber());

        //객실 생성 후 + 90재고 생성
        for (HotelDto.RoomCreateRequest room : form.getRooms()) {
            hotel.addRoom(
                    room.getName(),
                    room.getBaseCapacity(),
                    room.getMaxCapacity(),
                    room.getTotalCount(),
                    room.getWeekendPrice(),
                    room.getWeekDayPrice());
        }

        return hotelRepository.save(hotel);
    }

    /**
     * 단건 조회
     */

    public Hotel findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HOTEL_NOT_FOUND));
    }

    /**
     *  호텔 전체 조회
     */
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }


    /**
     * 연박 가능 호텔 검색 (도시, 체크인/체크아웃) + 호텔별 최저가
     * REST API: GET /api/hotels/search?city=서울&checkIn=2025-03-01&checkOut=2025-03-03
     */
    public List<HotelSearchResponse> getAvailableHotels(String city, LocalDate checkIn, LocalDate checkOut, int rooms,int page, int size) {


        List<Hotel> result = hotelQueryRepository.findAvailableHotels(city, checkIn, checkOut, rooms,page,size);
        log.info("page={}, size={}" , page, size);
        log.info("[검색] repository 결과 수={}", result.size());

        List<HotelSearchResponse> list = new ArrayList<>();

        for (Hotel hotel : result) {
            list.add(new HotelSearchResponse(hotel));
        }
        return list;
    }





    /**
     *
     * 호텔 생성하기전  유효성 검사
     */
    private void validateDuplicateHotel(String businessNumber) {
        if (hotelRepository.existsByBusinessNumber(businessNumber)) {
            throw new BusinessException(HOTEL_DUPLICATE);
        }
    }
}
