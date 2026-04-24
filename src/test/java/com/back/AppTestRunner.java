package com.back;

import com.back.standard.util.TestUtil;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class AppTestRunner {

    public static String run(String input) {
        Scanner scanner = TestUtil.genScanner(input + "\n종료");

        ByteArrayOutputStream output = TestUtil.setOutToByteArray();
        AppContext.renew(scanner, false);
        new App().run();

        return output.toString();
    }
}