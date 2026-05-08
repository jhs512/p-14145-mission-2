package com.back.domain.wiseSaying.controller;

import com.back.AppTestRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {

    @BeforeEach
    void setUp() throws IOException {
        Path dbDir = Paths.get("db/wiseSaying");
        if (Files.exists(dbDir)) {
            Files.walk(dbDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try { Files.delete(p); }
                        catch (IOException e) { throw new RuntimeException(e); }
                    });
        }
    }

    @Test
    @DisplayName("앱 시작 메시지")
    void t1() {
        String out = AppTestRunner.run("");

        assertThat(out)
                .contains("== 명언 앱 ==")
                .contains("명령)");
    }

    @Test
    @DisplayName("등록")
    void t2() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                """);

        assertThat(out)
                .contains("명언 :")
                .contains("작가 :")
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록 - 두 건이면 id가 1, 2 순서로 부여된다")
    void t3() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 말라.
                홍길동
                """);

        assertThat(out)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("목록 - 최신 등록 순으로 출력된다")
    void t4() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 말라.
                홍길동
                목록
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("2 / 홍길동 / 과거에 집착하지 말라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");

        int idx2 = out.indexOf("2 / 홍길동");
        int idx1 = out.indexOf("1 / 작자미상");
        assertThat(idx2).isGreaterThan(0).isLessThan(idx1);
    }

    @Test
    @DisplayName("삭제")
    void t5() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                삭제?id=1
                """);

        assertThat(out).contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("삭제 - 존재하지 않는 id")
    void t6() {
        String out = AppTestRunner.run("""
                삭제?id=1
                """);

        assertThat(out).contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("수정")
    void t7() {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=1
                과거를 사랑하라.
                홍길동
                목록
                """);

        assertThat(out)
                .contains("명언(기존) : 현재를 사랑하라.")
                .contains("작가(기존) : 작자미상")
                .contains("1 / 홍길동 / 과거를 사랑하라.");
    }

    @Test
    @DisplayName("수정 - 존재하지 않는 id")
    void t8() {
        String out = AppTestRunner.run("""
                수정?id=1
                """);

        assertThat(out).contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("종료")
    void t9() {
        String out = AppTestRunner.run("""
                종료
                """);

        assertThat(out).contains("== 명언 앱 ==");
    }

    @Test
    @DisplayName("영속성 - 앱 재시작 후에도 데이터가 유지된다")
    void t10() {
        AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                """);

        String out = AppTestRunner.run("""
                목록
                """);

        assertThat(out)
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("빌드 - data.json 파일 생성")
    void t11() throws IOException {
        String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                빌드
                """);

        assertThat(out).contains("data.json 파일의 내용이 갱신되었습니다.");

        Path dataJson = Paths.get("db/wiseSaying/data.json");
        assertThat(Files.exists(dataJson)).isTrue();

        String content = Files.readString(dataJson);
        assertThat(content)
                .contains("\"id\": 1")
                .contains("\"content\": \"현재를 사랑하라.\"")
                .contains("\"author\": \"작자미상\"")
                .contains("\"id\": 2")
                .contains("\"content\": \"과거에 집착하지 마라.\"");
    }
}