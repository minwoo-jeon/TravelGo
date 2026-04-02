package com.travelGo.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

public class InventoryCsvGenerator {
    public static void main(String[] args) {
        String directoryPath = "C:/temp";
        String filePath = directoryPath + "/inventory_3m.csv";
        LocalDate startDate = LocalDate.now();

        File directory = new File(directoryPath);
        if (!directory.exists()) directory.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // DB 컬럼 순서에 맞춘 헤더
            writer.write("inventory_date,sales_price,total_count,available_count,room_id");
            writer.newLine();

            Random random = new Random();

            // 1. 앞서 생성한 객실(Room) 100,000개를 순회
            for (long roomId = 1; roomId <= 100000; roomId++) {

                // 2. 각 객실당 한 달(30일) 치 재고 데이터 생성 (100,000 * 30 = 3,000,000)
                for (int i = 0; i < 30; i++) {
                    LocalDate targetDate = startDate.plusDays(i);

                    // 현실적인 가격 설정 (10만 원 ~ 19만 원 사이)
                    int price = 100000 + (random.nextInt(10) * 10000);
                    int totalStock = 10;
                    int availableStock = 10;

                    String line = String.format("%s,%d,%d,%d,%d",
                            targetDate, price, totalStock, availableStock, roomId);

                    writer.write(line);
                    writer.newLine();
                }

                // 대용량 작업이므로 진행 상황 모니터링 (1만 개 객실마다 출력)
                if (roomId % 10000 == 0) {
                    System.out.println(roomId + "개 객실의 한 달치 재고 생성 중...");
                }
            }
            System.out.println("총 3,000,000건의 재고 CSV 생성 완료! (경로: " + filePath + ")");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}