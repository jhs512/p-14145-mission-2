// 명언 앱의 등록, 목록, 삭제, 수정 기능 테스트
// TestUtil을 사용하여 실제 테스틀르 구현한다.


package com.back.domain.wiseSaying.controller;

import com.back.AppTestRunner;
// import com.back.global.app.AppConfig;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

public class WiseSayingControllerTest {

    @Test
    @DisplayName("enroll")
    void t3(){
        final String out = AppTestRunner.run("""
                등록
                현재를 사랑하라.
                작자미상
                exit
                """);
        assertTrue(out.contains("명언) : "));
        assertTrue(out.contains("작가) : "));
        assertTrue(out.contains("1번 명언이 등록되었습니다."));

    }
}
