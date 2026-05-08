package com.back;

import com.back.standard.util.TestUtil;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class AppTestRunner {
    public static String run(String input) {
        if (!input.endsWith("\n")) input += "\n";
        if (!input.contains("종료")) input += "종료\n";

        Scanner scanner = TestUtil.genScanner(input);
        ByteArrayOutputStream out = TestUtil.setOutToByteArray();

        String result;
        try {
            new App(scanner).run();
        } finally {
            // 테스트 실패 시에도 표준출력을 무조건 복구
            result = out.toString();
            TestUtil.clearSetOutToByteArray();
        }

        return result;
    }
}
