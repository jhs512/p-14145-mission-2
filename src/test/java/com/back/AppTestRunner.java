package com.back;

import java.io.*;

public class AppTestRunner {

    public static String run(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            System.setIn(in);
            System.setOut(new PrintStream(out));

            new App().run();

            return out.toString();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}