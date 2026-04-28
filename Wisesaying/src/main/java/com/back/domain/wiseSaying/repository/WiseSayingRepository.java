package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingRepository {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public int getCount() {
        int count = -1;
        try (FileReader reader = new FileReader("db/wiseSaying/lastId.txt")) {
            count = gson.fromJson(reader, int.class);  // ⭐ 핵심
        } catch (IOException e) {
            System.out.println("마지막 번호 불러오기 실패");
        }
        return count;
    }

    public int create(String content, String author) {
        int count = getCount() + 1;

        WiseSaying ws = new WiseSaying(count, content, author);

        try (FileWriter writer = new FileWriter("db/wiseSaying/%d.json".formatted(count))) {
            gson.toJson(ws, writer);  // ⭐ 핵심
        } catch (IOException e) {
            System.out.println("등록 실패");
        }

        try (FileWriter writer = new FileWriter("db/wiseSaying/lastId.txt")) {
            gson.toJson(count, writer);  // ⭐ 핵심
        } catch (IOException e) {
            System.out.println("마지막 번호 저장 실패");
        }

        return count;
    }

    public WiseSaying findById(int id) {
        try (FileReader reader = new FileReader("db/wiseSaying/%d.json".formatted(id))) {
            return gson.fromJson(reader, WiseSaying.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void deleteById(int id) {
        Path path = Path.of("db/wiseSaying/%d.json".formatted(id));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                System.out.println("삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다.");
        }

    }

    public void update(int id, String new_content, String new_author) {
        try {
            FileReader reader = new FileReader("db/wiseSaying/%d.json".formatted(id));
            WiseSaying ws = gson.fromJson(reader, WiseSaying.class);
            reader.close();

            if (ws.id == id) {
                ws.author = new_author;
                ws.content = new_content;
            }

            FileWriter writer = new FileWriter("db/wiseSaying/%d.json".formatted(id));
            gson.toJson(ws, writer);
            writer.close();

        } catch (IOException e) {
            System.out.println("업데이트 실패");
        }
    }

    public void build() {
        List<WiseSaying> ws = new ArrayList<>();

        for (int i = 1; i <= getCount(); i++) {
            Path path = Path.of("db/wiseSaying/%d.json".formatted(i));

            if (Files.exists(path)) {
                try (FileReader reader = new FileReader(path.toFile())) {
                    ws.add(gson.fromJson(reader, WiseSaying.class));
                } catch (IOException e) {
                    System.out.println("빌드 실패");
                }
            }
        }

        try {
            FileWriter writer = new FileWriter("db/wiseSaying/data.json");
            gson.toJson(ws, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("저장 실패");
        }
    }
}