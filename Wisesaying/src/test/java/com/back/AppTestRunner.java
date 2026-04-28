package com.back;

import com.back.standard.util.TestUtil;
import com.back.standard.util.TestDbUtil;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public class AppTestRunner {
    public static String run(String input) {
        Map<Path, byte[]> backupFiles = TestDbUtil.backupWiseSayingDb();
        TestDbUtil.resetWiseSayingDb();

        Scanner scanner = TestUtil.genScanner(inputWithExit(input));
        ByteArrayOutputStream output = TestUtil.setOutToByteArray();

        try {
            new App(scanner).run();
            return output.toString();
        } finally {
            TestUtil.clearSetOutToByteArray();
            TestDbUtil.restoreWiseSayingDb(backupFiles);
        }
    }

    private static String inputWithExit(String input) {
        if (input.strip().endsWith("종료")) return input;

        return input + "\n종료";
    }
}
