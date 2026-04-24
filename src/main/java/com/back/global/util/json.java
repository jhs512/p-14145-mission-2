package com.back.global.util;

import java.util.*;

public class json {
    public static String toString(Map<String, Object> map) {
        return toString(map, "");
    }
    public static String toString(Map<String, Object> map, String indend) {
        StringBuilder builder = new StringBuilder();
        builder.append("%s{\n".formatted(indend));
        for (var kv : map.entrySet()) {
            builder.append("%s\t\"%s\" : \"%s\",\n".formatted(
                    indend, kv.getKey(), kv.getValue().toString()));
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append("%s}".formatted(indend));
        return builder.toString();
    }

    public static String toString(List<Map<String, Object>> mapList) {
        return toString(mapList, "");
    }

    public static String toString(List<Map<String, Object>> mapList, String indend) {
        StringBuilder builder = new StringBuilder();
        builder.append("%s[\n".formatted(indend));
        for (var entry : mapList) {
            builder.append(toString(entry, "%s\t".formatted(indend)));
            builder.append(",\n");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append("%s]".formatted(indend));
        return builder.toString();
    }

    public static Map<String, Object> toMap(String string) {
        Map<String, Object> map = new HashMap<>();

        string = string.substring(1, string.length() - 1);

        String[] kv = string.split(",");

        for (String k : kv) {
            String[] kv2 = k.split(":");
            for (int i = 0; i < kv2.length; i++) {
                kv2[i] = kv2[i].trim();
                kv2[i] = kv2[i].substring(1, kv2[i].length() - 1);
            }
            map.put(kv2[0], kv2[1]);
        }
        return map;
    }
}
