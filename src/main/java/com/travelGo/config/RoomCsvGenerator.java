package com.travelGo.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RoomCsvGenerator {
    public static void main(String[] args) {
        String directoryPath = "C:/temp";
        String filePath = directoryPath + "/room_100k.csv"; // 파일명 명확하게 수정

        File directory = new File(directoryPath);
        if (!directory.exists()) directory.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 헤더 추가 (DB 컬럼 순서와 매칭 확인용)
            writer.write("room_id,name,base_capacity,max_capacity,total_count,weekday_price,weekend_price,status,hotel_id");
            writer.newLine();

            int roomId = 1;
            // 1. 호텔 1만 개 순회
            for (int hotelId = 1; hotelId <= 10000; hotelId++) {

                // 2. 호텔 하나당 현실적인 객실 타입 10개 생성
                for (int i = 1; i <= 10; i++) {
                    String roomName = "Standard Room " + i;

                    // 형식: room_id, name, base_capacity, max_capacity, total_count, weekday_price, weekend_price, status, hotel_id
                    String line = String.format("%d,%s,2,4,10,100000,150000,OPEN,%d",
                            roomId, roomName, hotelId);

                    writer.write(line);
                    writer.newLine();
                    roomId++;
                }

                // 진행률 표시
                if (hotelId % 2000 == 0) {
                    System.out.println("호텔 " + hotelId + "개째 객실 데이터 생성 중...");
                }
            }
            System.out.println("총 " + (roomId - 1) + "건의 객실 CSV 생성 완료! (경로: " + filePath + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}