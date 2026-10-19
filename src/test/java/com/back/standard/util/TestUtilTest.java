// TestUtil이 제대로 작동하는지 검증하는 파일

package com.back.standard.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtilTest {
    @Test
    @DisplayName("TestUtil.genScanner()")
    public void t1() {
        Scanner scanner = TestUtil.genScanner("""
                등록
                나의 죽음을 적들에게 알리지 말라!
                이순신
                """);

        String cmd = scanner.nextLine();
        String content = scanner.nextLine();
        String author = scanner.nextLine();

        assertEquals("등록", cmd);
        assertEquals("나의 죽음을 적들에게 알리지 말라!", content);
        assertEquals("이순신", author);
    }

    @Test
    @DisplayName("TestUtil.setOutToByteArray()")
    public void t2() {
        ByteArrayOutputStream byteArrayOutputStream = TestUtil.setOutToByteArray();

        System.out.println("2 / 이순신 / 나의 죽음을 적들에게 알리지 말라!");

        String out = byteArrayOutputStream.toString().trim();
        TestUtil.clearSetOutToByteArray();

        assertEquals("2 / 이순신 / 나의 죽음을 적들에게 알리지 말라!", out);
        System.out.println("이제는 화면에 출력됩니다.");
    }
}