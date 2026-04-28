package com.back.standard.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TestDbUtil {
    public static final Path WISE_SAYING_DB_PATH = Path.of("db/wiseSaying");

    public static Map<Path, byte[]> backupWiseSayingDb() {
        Map<Path, byte[]> backupFiles = new HashMap<>();

        if (!Files.exists(WISE_SAYING_DB_PATH)) return backupFiles;

        try (Stream<Path> paths = Files.list(WISE_SAYING_DB_PATH)) {
            for (Path path : paths.toList()) {
                if (Files.isRegularFile(path)) {
                    backupFiles.put(path, Files.readAllBytes(path));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return backupFiles;
    }

    public static void resetWiseSayingDb() {
        try {
            Files.createDirectories(WISE_SAYING_DB_PATH);
            deleteAllWiseSayingDbFiles();
            Files.writeString(WISE_SAYING_DB_PATH.resolve("lastId.txt"), "0");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void restoreWiseSayingDb(Map<Path, byte[]> backupFiles) {
        try {
            Files.createDirectories(WISE_SAYING_DB_PATH);
            deleteAllWiseSayingDbFiles();

            for (Map.Entry<Path, byte[]> entry : backupFiles.entrySet()) {
                Files.write(entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteAllWiseSayingDbFiles() throws IOException {
        if (!Files.exists(WISE_SAYING_DB_PATH)) return;

        try (Stream<Path> paths = Files.list(WISE_SAYING_DB_PATH)) {
            for (Path path : paths.toList()) {
                Files.deleteIfExists(path);
            }
        }
    }
}
