package org.example;

import static org.assertj.core.api.Assertions.assertThat;
import org.example.AppTestRunner;
//import org.example.global.app.AppConfig;
import org.junit.jupiter.api.*;

public class WiseSayingControllerTest {
    @Test
    @DisplayName("등록")
    void t3() {
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

    // ...
}