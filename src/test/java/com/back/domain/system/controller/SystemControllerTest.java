package com.back.domain.system.controller;

import com.back.AppTestRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

//import com.back.global.app.AppConfig;

public class SystemControllerTest {
    @Test
    @DisplayName("1: 종료")
    void t1() {
        final String out = AppTestRunner.run("""
                종료
                """);

        assertThat(out)
                .isEqualTo("""
                        == 명언 앱 ==
                        명령)\s""");
    }

    @Test
    @DisplayName("1-1: 없는명령")
    void t11() {
        final String out = AppTestRunner.run("""
                이상한명령
                종료
                """);

        assertThat(out)
                .contains("잘못된 명령입니다. 다시 입력해주세요. (예시 - 등록 / 목록 / 목록?page=숫자 / 목록?keywordType=author&keyword=작자 / 삭제?id=숫자 / 수정?id=숫자 / 종료)\n");
    }
//     ...
}