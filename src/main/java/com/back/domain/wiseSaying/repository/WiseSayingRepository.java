package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class WiseSayingRepository {
    private static final String DB_DIR = "db/wiseSaying";
    private static final Path LAST_ID_PATH = Paths.get(DB_DIR, "lastId.txt");
    private static final Path DATA_JSON_PATH = Paths.get(DB_DIR, "data.json");

    public WiseSayingRepository() {
        try {
            Files.createDirectories(Paths.get(DB_DIR));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int loadLastId() {
        if (!Files.exists(LAST_ID_PATH)) return 0;
        try {
            return Integer.parseInt(Files.readString(LAST_ID_PATH).trim());
        } catch (IOException e) {
            return 0;
        }
    }

    private void saveLastId(int id) {
        try {
            Files.writeString(LAST_ID_PATH, String.valueOf(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path jsonPath(int id) {
        return Paths.get(DB_DIR, id + ".json");
    }

    private String toJson(WiseSaying ws) {
        return "{\n" +
               "  \"id\": " + ws.getId() + ",\n" +
               "  \"content\": \"" + escape(ws.getContent()) + "\",\n" +
               "  \"author\": \"" + escape(ws.getAuthor()) + "\"\n" +
               "}";
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private WiseSaying fromJson(String json) {
        int id = Integer.parseInt(extractValue(json, "id"));
        String content = extractValue(json, "content");
        String author = extractValue(json, "author");
        return new WiseSaying(id, content, author);
    }

    private String extractValue(String json, String key) {
        int keyIdx = json.indexOf("\"" + key + "\"");
        int colonIdx = json.indexOf(":", keyIdx);
        int start = colonIdx + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;

        if (json.charAt(start) == '"') {
            StringBuilder sb = new StringBuilder();
            for (int i = start + 1; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '\\' && i + 1 < json.length()) {
                    char next = json.charAt(i + 1);
                    if (next == '"') { sb.append('"'); i++; }
                    else if (next == '\\') { sb.append('\\'); i++; }
                    else { sb.append(c); }
                } else if (c == '"') {
                    break;
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        return json.substring(start, end);
    }

    private void writeJson(WiseSaying ws) {
        try {
            Files.writeString(jsonPath(ws.getId()), toJson(ws));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WiseSaying save(String content, String author) {
        int id = loadLastId() + 1;
        saveLastId(id);
        WiseSaying ws = new WiseSaying(id, content, author);
        writeJson(ws);
        return ws;
    }

    public void save(WiseSaying ws) {
        writeJson(ws);
    }

    public List<WiseSaying> findAll() {
        try {
            return Files.list(Paths.get(DB_DIR))
                    .filter(p -> p.getFileName().toString().matches("\\d+\\.json"))
                    .map(p -> {
                        try { return fromJson(Files.readString(p)); }
                        catch (IOException e) { throw new RuntimeException(e); }
                    })
                    .sorted(Comparator.comparingInt(WiseSaying::getId))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<WiseSaying> findById(int id) {
        Path path = jsonPath(id);
        if (!Files.exists(path)) return Optional.empty();
        try {
            return Optional.of(fromJson(Files.readString(path)));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public boolean deleteById(int id) {
        try {
            return Files.deleteIfExists(jsonPath(id));
        } catch (IOException e) {
            return false;
        }
    }

    public void buildDataJson() {
        List<WiseSaying> all = findAll();
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < all.size(); i++) {
            String indented = Arrays.stream(toJson(all.get(i)).split("\n"))
                    .map(line -> "  " + line)
                    .collect(Collectors.joining("\n"));
            sb.append(indented);
            if (i < all.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        try {
            Files.writeString(DATA_JSON_PATH, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}