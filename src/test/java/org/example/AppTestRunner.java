package org.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AppTestRunner {
    public static String run(String input) {

        if (!input.contains("종료")) {
            input += "\n종료\n";
        }

        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        ByteArrayOutputStream byteArrayOutputStream = TestUtil.setOutToByteArray();

        try {
            new App().run();
        } finally {
            System.setIn(originalIn);
            TestUtil.clearSetOutToByteArray();
        }

        return byteArrayOutputStream.toString().trim();
    }
}
