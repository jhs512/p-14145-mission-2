package com.back.util;

import java.nio.file.Path;
import java.util.Comparator;

public class FileSortUtil {
    public static Comparator<Path> byFileNameNumberDesc() {
        return byFileNameNumberAsc().reversed();
    }
    public static Comparator<Path> byFileNameNumberAsc() {
        return Comparator
                .comparingInt((Path path) -> Integer.parseInt(path.getFileName().toString().split("\\.")[0]));
    }
}
