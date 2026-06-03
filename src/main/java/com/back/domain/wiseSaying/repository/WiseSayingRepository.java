package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.converter.WiseSayingJSONConverter;
import com.back.util.FileSortUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WiseSayingRepository {
    private final Path DB_DIRECTORY = Path.of("db/wiseSaying");
    private final Path LAST_ID_PATH = DB_DIRECTORY.resolve("lastId.txt");
    private final Path EXPORT_PATH = DB_DIRECTORY.resolve("../export/data.json"); // 아 이럼 꼬이는데
    private final int PAGE_SIZE = 5;

    public WiseSayingRepository() {}
    public int create(String quote, String author) {
        Path tmpQuotePath = DB_DIRECTORY.resolve(getLastId()+ ".tmp");
        Path tmpLastIdPath = LAST_ID_PATH.resolveSibling(LAST_ID_PATH.getFileName().toString()+".tmp");

        // WiseSaying 저장과 lastId 업데이트는 같이 되도록 처리
        try {
            int newLastId = getLastId() + 1;
            // 임시 파일에 작성
            Files.writeString(tmpQuotePath,  WiseSayingJSONConverter.toJsonString(newLastId, quote, author));
            Files.writeString(tmpLastIdPath, Integer.toString(newLastId));
            // 교체
            Files.move(tmpQuotePath, DB_DIRECTORY.resolve(newLastId + WiseSayingJSONConverter.EXTENSION),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.move(tmpLastIdPath, LAST_ID_PATH, StandardCopyOption.REPLACE_EXISTING);
            return newLastId;
        } catch (IOException e) {
            try {
                Files.deleteIfExists(tmpQuotePath);
                Files.deleteIfExists(tmpLastIdPath);
            } catch (IOException e2) {
                System.out.println("Exception at deleteIfExists(): " + e2.getMessage());
                throw new RuntimeException(e2);
            }
            return -1;
        }
    }

    public List<WiseSaying> fetchPage(int pageNo) {
        int num = countWiseSayings();
        if (num == 0) { return new ArrayList<>();}
        if (pageNo > getPageCount()) { return new ArrayList<>();}

        int start = (pageNo - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, num);
        try (Stream<Path> stream = Files.list(DB_DIRECTORY)) {
            return stream
                    .filter(path -> path.getFileName().toString().endsWith(WiseSayingJSONConverter.EXTENSION))
                    .sorted(FileSortUtil.byFileNameNumberDesc())
                    .skip(start)
                    .limit(end - start)
                    .map( path -> {
                        try {
                            return WiseSayingJSONConverter.fromJsonString(Files.readString(path));
                        } catch (IOException e2) {
                            System.out.println("Exception at fetchPage() map: " + e2.getMessage());
                            throw new RuntimeException(e2);
                        }
                    })
                    .toList();
        } catch (IOException e) {
            System.out.println("Exception at fetchPage(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void update(int qid, String quote, String author) {
        try {
            Files.writeString(DB_DIRECTORY.resolve(qid+WiseSayingJSONConverter.EXTENSION), WiseSayingJSONConverter.toJsonString(qid, quote, author), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Exception at update(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean delete(int qid) {
        try {
            return Files.deleteIfExists(DB_DIRECTORY.resolve(qid+WiseSayingJSONConverter.EXTENSION));
        } catch (IOException e) {
            System.out.println("Exception at delete(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<WiseSaying> filter(String keywordType, String keyword) {
        List<WiseSaying> filteredPage = new ArrayList<>();
        try (Stream<Path> stream = Files.list(DB_DIRECTORY)) {
            filteredPage = stream
                    .filter(path -> path.toString().endsWith(WiseSayingJSONConverter.EXTENSION))
                    .sorted(FileSortUtil.byFileNameNumberDesc())
                    .map(path -> {
                        try {
                            return WiseSayingJSONConverter.fromJsonString(Files.readString(path));
                        } catch (IOException e) {
                            System.out.println("Exception at filter(): " + e.getMessage());
                            throw new RuntimeException(e);
                        }
                            })
                    .filter(q -> q != null && containsKeyword(q, keywordType, keyword))
                    .collect(Collectors.toList());
        int start = Math.max(0, filteredPage.size() - PAGE_SIZE);
        return new ArrayList<>(filteredPage.subList(start, filteredPage.size()));
        } catch (IOException e) {
            System.out.println("Exception at filter(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private boolean containsKeyword(WiseSaying q, String keywordType, String keyword){
        return switch (keywordType) {
            case "author" -> q.getAuthor().contains(keyword);
            case "content" -> q.getQuote().contains(keyword);
            default -> false;
        };
    }

    public WiseSaying readFile(int qid) {
        try {
            return WiseSayingJSONConverter.fromJsonString(
                    Files.readString(DB_DIRECTORY.resolve(qid + WiseSayingJSONConverter.EXTENSION)));
        } catch (IOException e) {
            System.out.println("Exception at readFile(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int getPageCount() {
        return (countWiseSayings() + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    private int getLastId() {
        try {
            return Integer.parseInt(Files.readString(LAST_ID_PATH));
        } catch (Exception e) {
            System.out.println("Exception at getLastId(): " + e.getMessage() + "parseInt fail. Make new lastId: 0");
            try {Files.writeString(LAST_ID_PATH,"0");
            } catch (Exception e2) {
                System.out.println("Exception at writeString(): " + e2.getMessage());
            }
            return 0;
        }
    }

    private int countWiseSayings() {
        try (Stream<Path> stream = Files.list(DB_DIRECTORY)) {
            return (int) stream
                    .filter(path -> path.getFileName().toString().endsWith(WiseSayingJSONConverter.EXTENSION))
                    .count();
        } catch (IOException e) {
            System.out.println("Exception at countWiseSaying(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void export() {
        String exportString;
        try (Stream<Path> stream = Files.list(DB_DIRECTORY)) {
            exportString = stream
                    .filter(path -> path.getFileName().toString().endsWith(WiseSayingJSONConverter.EXTENSION))
                    .sorted(FileSortUtil.byFileNameNumberAsc())
                    .map(path -> {
                        try {
                            return Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8).stream()
                                    .map(line -> "\t" + line)
                                    .collect(Collectors.joining("\n"));
                        } catch (IOException e) {
                            System.out.println("Exception at export() map readAllLines: " + e.getMessage());
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.joining(",\n", "[\n", "\n]"));
        } catch (IOException e) {
            System.out.println("Exception at export() read: " + e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            Files.createDirectories(EXPORT_PATH.getParent());
            Files.writeString(EXPORT_PATH, exportString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Exception at export() write: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
