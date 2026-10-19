// dummy input 입력 등 공통적인 기능을 제공함
// 자동 테스트에선 사람이 입력 x, 즉 사용자 입력을 흉내내고 출력된 결과를 문자열로 가져오는 도구

package com.back.standard.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class TestUtil {
    private static final PrintStream ORIGINAL_OUT = System.out;
    private static PrintStream CURRENT_OUT;

    public static Scanner genScanner(String input) {
        return new Scanner(input);
    }

    public static ByteArrayOutputStream setOutToByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CURRENT_OUT = new PrintStream(byteArrayOutputStream, true);
        System.setOut(CURRENT_OUT);

        return byteArrayOutputStream;
    }

    public static void clearSetOutToByteArray() {
        System.setOut(ORIGINAL_OUT);
        CURRENT_OUT.close();
        CURRENT_OUT = null;
    }
}