package com.back.domain.wiseSaying.repository.converter;

import com.back.domain.wiseSaying.entity.WiseSaying;

public class WiseSayingJSONConverter {
    public static final String FORMAT_STRING = """
                {
                    "id": %d,
                    "content": "%s",
                    "author": "%s"
                }
                """;
    public static final String EXTENSION = ".json";

    public static String toJsonString(int quoteId, String quote, String author) {
        return FORMAT_STRING.formatted(quoteId, quote, author);
    }

    public static WiseSaying fromJsonString(String jsonString) {
        int qid = 0;
        String quote = null, author = null;
        for(String s: jsonString.split("[,}]")) {
            if (s.contains("id")) {
                qid = Integer.parseInt(s.split(":")[1].trim());
            } else if (s.contains("content")) {
                quote = s.split(":")[1].trim();
                quote = quote.substring(1, quote.length()-1); // 앞뒤 '"' 제거
            } else if (s.contains("author")) {
                author = s.split(":")[1].trim();
                author = author.substring(1, author.length()-1); // 앞뒤 '"' 제거
            }
        }
        if (qid == 0 || quote == null || author == null) {
            return null;
        }
        return new WiseSaying(qid, quote, author);
    }
}
