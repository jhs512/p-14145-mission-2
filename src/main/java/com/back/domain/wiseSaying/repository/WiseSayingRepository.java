// 데이터의 조회, 수정, 삭제, 생성 (CRUD)를 담당
// 스캐너 , 출력 사용 금지

package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WiseSayingRepository {

    private final Path dbPath = Paths.get("db/wiseSaying");
    private final Path lastIdPath =dbPath.resolve("lastId.txt");

    // 최대 배열 길이 1001
    private final WiseSaying[] sayings = new WiseSaying[1001];
    // 0-base index와 사용자의 입력.출력 number 간 괴리를 줄이기 위해 아예 index = 0은 null로 유지할 것
    // sayings[0] = null;
    private int index = 1;

    public WiseSayingRepository() {
        try {
            Files.createDirectories(dbPath);

            // 마지막 명언 번호 복원하고, 그 다음부터 작성해야 할 index를 가져옴
            if (Files.exists(lastIdPath)){
                index = Integer.parseInt(Files.readString(lastIdPath).trim()) + 1;
            }

            for (int i=1 ; i<index ; i++){
                Path filePath = dbPath.resolve(i+".json");

                if(Files.exists(filePath)){
                    String json = Files.readString(filePath);
                    sayings[i] = fromJson(json);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 명언 등록
    public WiseSaying saveNew(String content, String author) {
        if (index >= sayings.length) {
            throw new IllegalStateException("더 이상 명언을 등록할 수 없습니다.");
        }

        WiseSaying saying = new WiseSaying(index, content, author);

        editById(saying);

        try {
            // 마지막 명언 번호 저장
            Files.writeString(lastIdPath, String.valueOf(index));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        index++;

        return saying;
    }

    // 명언 목록
    public WiseSaying[] findAll() {
        WiseSaying[] result = new WiseSaying[index - 1];

        int count = 0;

        // 최신순으로 배열에 저장
        for (int i = index - 1; i >= 1; i--) {
            if (sayings[i] != null) {
                result[count] = sayings[i];
                count++;
            }
        }

        return java.util.Arrays.copyOf(result, count);
    }


    // 명언 조회
    public WiseSaying findById(int id) {
        if (id < 1 || id >= index) {
            return null;
        }

        return sayings[id];
    }

    // 명언 저장 및 수정
    public void editById(WiseSaying saying) {
        int id = saying.getId();

        if (id < 1 || id >= sayings.length) {
            throw new IllegalArgumentException("유효하지 않은 명언 번호입니다.");
        }

        Path filePath = dbPath.resolve(id + ".json");

        try {
            Files.writeString(filePath, toJson(saying));
            sayings[id] = saying;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 명언 삭제
    public void deleteById(int id) {
        if (id < 1 || id >= index) {
            return;
        }

        Path filePath = dbPath.resolve(id + ".json");

        try {
            Files.deleteIfExists(filePath);
            sayings[id] = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 명언을 Json 형태로 변환
    private String toJson(WiseSaying saying) {
        return """
                {
                    "id": %d,
                    "content": "%s",
                    "author": "%s"
                }
                """.formatted(
                saying.getId(),
                saying.getContent(),
                saying.getAuthor()
        );
    }

    // json을 명언 형태로 변환
    private WiseSaying fromJson(String json) {
        String[] lines = json.split("\n");

        int id = Integer.parseInt(
                lines[1].split(":")[1].trim().replace(",", "")
        );

        String content = lines[2].split(":", 2)[1].trim()
                .replace("\"", "").replaceAll(",$", "");

        String author = lines[3].split(":", 2)[1].trim()
                .replace("\"", "").replaceAll(",$", "");

        return new WiseSaying(id, content, author);
    }
}
