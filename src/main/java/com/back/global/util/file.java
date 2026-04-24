package com.back.global.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class file {
    public static boolean write(String path, String content) {
        Path p = Paths.get(path);
        Path parent = p.getParent();
        if (parent != null && !Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                return false;
            }
        }

        try {
            Files.writeString(p,
                    content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static String readFile(String path) {
        Path p = Paths.get(path);
        try {
            return Files.readString(p);
        } catch (IOException e) {
            return null;
        }
    }

    public static  boolean delete(String path) {
        Path p = Paths.get(path);
        try {
            Files.delete(p);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
