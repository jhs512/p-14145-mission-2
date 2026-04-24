package com.back.domain.wiseSaying.entity;

import java.util.HashMap;
import java.util.Map;

public class WiseSayingEntity {
    int id;
    public String wise;
    public String author;

    public void setId(int newId) {
        id = newId;
    }
    public int getId() { return id; }
    public WiseSayingEntity(String wise, String author) {
        this.wise = wise;
        this.author = author;
    }

    public Map<String, Object> toMap() {

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("id", id);
        map.put("content", wise);
        map.put("author", author);

        return map;
    }
}
