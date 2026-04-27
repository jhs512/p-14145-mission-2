package com.back.domain.wiseSaying.controller;

import com.back.AppTestRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {
    @Test
    @DisplayName("종료 테스트")
    void t1() {
        final String out = AppTestRunner.run("""
                종료
                """);

        assertThat(out)
                .contains("명언 앱을 종료합니다.");
    }

    @Test
    @DisplayName("기본 등록")
    void t2() {
        final String out = AppTestRunner.run("""
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
    @DisplayName("목록 출력")
    void t3() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("--------------------------")
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .contains("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("데이터 없을 때 목록")
    void t4() {
        final String out = AppTestRunner.run("""
               목록
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("--------------------------");
    }

    @Test
    @DisplayName("정상 삭제")
    void t5() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                삭제?id=1
                """);

        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("미존재 삭제")
    void t6() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                등록
                과거에 집착하지 마라.
                작자미상
                목록
                삭제?id=1
                삭제?id=1
                """);

        assertThat(out)
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("내용 수정 후 목록에서 확인")
    void t7() {
        final String out = AppTestRunner.run("""
            등록
            현재를 사랑하라.
            작자미상
            수정?id=1
            현재와 자신을 사랑하라.
            홍길동
            목록
            """);

        assertThat(out)
                .contains("1 / 홍길동 / 현재와 자신을 사랑하라.") // 실제 데이터 변경 확인
                .contains("1번 명언이 수정되었습니다.");
    }

    @Test
    @DisplayName("잘못된 ID 파싱")
    void t8() {
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                수정?id=3
                """);

        assertThat(out)
                .contains("3번 명언은 존재하지 않습니다.");
    }
}

