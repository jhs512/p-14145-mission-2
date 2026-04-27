package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class WiseSayingRepository {
    private final ArrayList<WiseSaying> wiseSayings = new ArrayList<>();
    private int lastId = 0;

    // 파일 경로 설정을 상수로 관리
    private final String DB_PATH = "db/wiseSaying";
    private final String LAST_ID_FILE = DB_PATH + "/lastId.txt";

    public WiseSayingRepository() {
        init();
    }

    // 앱 시작 시 폴더와 lastId를 준비하는 로직
    private void init() {
        try {
            // 1. 저장소 폴더가 없으면 생성
            Files.createDirectories(Paths.get(DB_PATH));

            // 2. lastId.txt 파일이 있는지 확인
            Path lastIdPath = Paths.get(LAST_ID_FILE);
            if (Files.exists(lastIdPath)) {
                // 파일이 있으면 읽어서 숫자로 변환
                String content = Files.readString(lastIdPath).trim();
                this.lastId = Integer.parseInt(content);
            }

            // 3. 데이터 복구
            loadWiseSayings();

        } catch (IOException e) {
            // 파일 입출력 중 에러가 발생하면 앱이 멈추지 않게 예외 처리가 필요해요
            System.out.println("데이터 초기화 중 오류 발생: " + e.getMessage());
        }
    }

    // 명언 저장하기
    public int save(String content, String author) {
        lastId++; // 새 번호 생성
        int savedId = lastId;

        // 1. 객체 생성
        WiseSaying wiseSaying = new WiseSaying(savedId, content, author);

        // 2. 메모리(리스트)에 저장
        wiseSayings.add(wiseSaying);

        // 3. 파일에 저장 (영속성 부여)
        saveLastId(savedId);   // lastId.txt 갱신
        saveFile(wiseSaying);  // {id}.json 파일 생성

        return savedId;
    }

    // 명언 리스트 불러오기
    public ArrayList<WiseSaying> findAll() {
        // 새 리스트에 담아서 반환
        return new ArrayList<>(wiseSayings);
    }

    // 명언 수정하기
    public void update(WiseSaying wiseSaying, String content, String author) {
        wiseSaying.setContent(content);
        wiseSaying.setAuthor(author);

        saveFile(wiseSaying); // 파일 시스템에 갱신된 내용 저장
    }

    // 명언 삭제하기
    public void remove(WiseSaying wiseSaying) {
        wiseSayings.remove(wiseSaying);

        // 실제 파일 삭제 로직
        String filePath = String.format("%s/%d.json", DB_PATH, wiseSaying.getId());
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            System.out.println(wiseSaying.getId() + "번 파일 삭제 실패");
        }
    }

    // ID로 명언 찾기
    public WiseSaying findById(int id) {
        for (WiseSaying ws : wiseSayings) {
            if (ws.getId() == id) {
                return ws;
            }
        }
        return null;
    }

    // 명언 객체를 JSON 파일로 저장하는 메서드
    private void saveFile(WiseSaying wiseSaying) {
        // 파일 경로: db/wiseSaying/1.json
        String filePath = String.format("%s/%d.json", DB_PATH, wiseSaying.getId());

        // JSON 형식의 문자열 만들기
        String jsonContent = String.format(
                "{\n  \"id\": %d,\n  \"content\": \"%s\",\n  \"author\": \"%s\"\n}",
                wiseSaying.getId(),
                wiseSaying.getContent(),
                wiseSaying.getAuthor()
        );

        try {
            // 파일 쓰기
            Files.writeString(Paths.get(filePath), jsonContent);
        } catch (IOException e) {
            System.out.println(wiseSaying.getId() + "번 명언 파일 저장 중 오류: " + e.getMessage());
        }
    }

    // lastId를 파일에 물리적으로 저장하는 메서드
    private void saveLastId(int id) {
        try {
            Files.writeString(Paths.get(LAST_ID_FILE), String.valueOf(id));
        } catch (IOException e) {
            System.out.println("lastId 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 파일 시스템에 저장된 명언 데이터를 읽어와 리스트에 복구
    private void loadWiseSayings() {
        File dir = new File(DB_PATH);
        File[] files = dir.listFiles(); // 폴더 안의 모든 파일 가져오기

        if (files == null) return;

        for (File file : files) {
            // 파일명이 숫자로 된 .json 파일인지 확인
            if (file.getName().endsWith(".json")) {
                try {
                    String jsonContent = Files.readString(file.toPath());
                    // JSON 문자열에서 데이터 추출 (앞서 배운 extract 방식 사용)
                    int id = Integer.parseInt(extract(jsonContent, "id"));
                    String content = extract(jsonContent, "content");
                    String author = extract(jsonContent, "author");

                    // 리스트에 복구된 객체 추가
                    wiseSayings.add(new WiseSaying(id, content, author));
                } catch (IOException e) {
                    System.out.println("파일 로드 중 오류: " + file.getName());
                }
            }
        }
    }

    // JSON 파싱용 보조 메서드
    private String extract(String json, String key) {
        try {
            String splitKey = "\"" + key + "\": ";
            String[] parts = json.split(splitKey);
            String value = parts[1].split(",|\n|}")[0].trim();
            return value.replace("\"", "");
        } catch (Exception e) { return ""; }
    }

}
