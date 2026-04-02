package com.travelGo.config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class HotelCsvGenerator {
    public static void main(String[] args) {
        String filePath = "C:/temp/hotel.csv";
        // 테스트할 도시 목록
        String[] cities = {"Seoul", "Busan", "Jeju", "Gangneung", "Incheon"};
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,business_number,name,star_rating,city,street,zipcode,owner_id");
            writer.newLine();

            for (int i = 1; i <= 10000; i++) {
                // 도시를 랜덤하게 선택 (각 도시당 약 2,000건씩 분산)
                String city = cities[random.nextInt(cities.length)];

                String line = String.format("%d,BIZ-%06d,TravelHotel_%d,%d,%s,Road_%d,12345,1",
                        i, i, i, (int)(Math.random() * 5) + 1, city, i);

                writer.write(line);
                writer.newLine();
            }
            System.out.println("5개 도시로 분산된 호텔 1만 건 생성 완료!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}