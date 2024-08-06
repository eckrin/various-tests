package com.eckrin.test.csv;

import com.opencsv.CSVWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

@EnableAsync
//@EnableBatchProcessing
@SpringBootApplication
@EnableScheduling
public class UserCsv {

    private static final String FILE_PATH = "user_data.csv";
    private static final int RECORD_COUNT = 200000; // 20만 건
    private static final String[] HEADER = {"familyName", "givenName", "email", "role"};
    private static final String[] ROLES = {"USER", "ADMIN", "ANONYMOUS"};
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
            writer.writeNext(HEADER); // 헤더 작성

            for (int i = 0; i < RECORD_COUNT; i++) {
                String familyName = generateRandomString(8);
                String givenName = generateRandomString(6);
                String email = generateRandomEmail();
                String role = ROLES[RANDOM.nextInt(ROLES.length)];
                writer.writeNext(new String[]{familyName, givenName, email, role});
            }

            System.out.println("CSV 파일이 '" + FILE_PATH + "'에 생성되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + RANDOM.nextInt(26)));
        }
        return sb.toString();
    }

    private static String generateRandomEmail() {
        return generateRandomString(5).toLowerCase() + "@example.com";
    }
}
