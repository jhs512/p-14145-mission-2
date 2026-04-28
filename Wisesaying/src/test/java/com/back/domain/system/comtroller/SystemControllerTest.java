package com.back.domain.system.comtroller;

import com.back.AppTestRunner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SystemControllerTest {
    @Test
    @DisplayName("프로그램 종료")
    void t1() {
        final String out = AppTestRunner.run("종료");

        assertThat(out)
                .contains("프로그램을 종료합니다.");
    }
}
