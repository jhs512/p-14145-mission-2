package com.back.standard.util;

import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonUtilTest {
    private Map<Path, byte[]> backupFiles;

    @BeforeEach
    void beforeEach() {
        backupFiles = TestDbUtil.backupWiseSayingDb();
        TestDbUtil.resetWiseSayingDb();
    }

    @AfterEach
    void afterEach() {
        TestDbUtil.restoreWiseSayingDb(backupFiles);
    }

    @Test
    @DisplayName("명언 등록 시 JSON 파일 생성")
    void t1() throws IOException {
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();

        wiseSayingRepository.create("현재를 사랑하라.", "작자미상");

        JsonObject jsonObject = JsonParser
                .parseString(Files.readString(TestDbUtil.WISE_SAYING_DB_PATH.resolve("1.json")))
                .getAsJsonObject();

        assertThat(jsonObject.get("id").getAsInt()).isEqualTo(1);
        assertThat(jsonObject.get("content").getAsString()).isEqualTo("현재를 사랑하라.");
        assertThat(jsonObject.get("author").getAsString()).isEqualTo("작자미상");
    }

    @Test
    @DisplayName("data.json 파일 생성")
    void t2() throws IOException {
        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        wiseSayingRepository.create("현재를 사랑하라.", "작자미상");
        wiseSayingRepository.create("나의 죽음을 적들에게 알리지 말라!", "이순신");

        wiseSayingRepository.build();

        JsonArray jsonArray = JsonParser
                .parseString(Files.readString(TestDbUtil.WISE_SAYING_DB_PATH.resolve("data.json")))
                .getAsJsonArray();

        assertThat(jsonArray).hasSize(2);
        assertThat(jsonArray.get(0).getAsJsonObject().get("id").getAsInt()).isEqualTo(1);
        assertThat(jsonArray.get(0).getAsJsonObject().get("content").getAsString()).isEqualTo("현재를 사랑하라.");
        assertThat(jsonArray.get(0).getAsJsonObject().get("author").getAsString()).isEqualTo("작자미상");
        assertThat(jsonArray.get(1).getAsJsonObject().get("id").getAsInt()).isEqualTo(2);
        assertThat(jsonArray.get(1).getAsJsonObject().get("content").getAsString()).isEqualTo("나의 죽음을 적들에게 알리지 말라!");
        assertThat(jsonArray.get(1).getAsJsonObject().get("author").getAsString()).isEqualTo("이순신");
    }
}
