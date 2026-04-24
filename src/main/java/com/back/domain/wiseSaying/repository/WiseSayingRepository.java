package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSayingEntity;

import com.back.global.util.json;

import java.io.IOException;
import java.util.*;
import java.nio.file.*;

import com.back.global.util.file;

public class WiseSayingRepository {

    private final List<WiseSayingEntity> wiseSayings = new ArrayList<>();
    private int _top = 0;

    public List<WiseSayingEntity> findForList() {
        return wiseSayings.reversed();
    }

    public WiseSayingRepository() {
        String idx = file.readFile("db/wiseSaying/lastIdx.txt");

        if (idx != null) {
            idx = idx.trim();
            _top = Integer.parseInt(idx);

            for (int i = 0; i < _top; i++) {
                String content = file.readFile(
                        "db/wiseSaying/%d.json".formatted(i + 1));
                if (content == null) continue;

                Map<String, Object> wise = json.toMap(content);

                if (wise == null) continue;

                WiseSayingEntity newWise = new WiseSayingEntity(
                        wise.get("content").toString(),
                        wise.get("author").toString());
                newWise.setId(Integer.parseInt(wise.get("id").toString()));
                wiseSayings.add(newWise);

            }
        }

        while (wiseSayings.size() < 10) {
            addWiseSaying(new WiseSayingEntity(
                    "명언 %d".formatted(wiseSayings.size() + 1),
                    "작자미상%d".formatted(wiseSayings.size() + 1)));
        }
    }

    private void save(int id, WiseSayingEntity wiseSaying) {

        String wiseSayingJsonStr =
                json.toString(wiseSaying.toMap());

        file.write("db/wiseSaying/%d.json".formatted(id),
                wiseSayingJsonStr);
        file.write("db/wiseSaying/lastIdx.txt",
                "%d".formatted(_top));

    }

    public void addWiseSaying(WiseSayingEntity newWise) {
        newWise.setId(++_top);
        wiseSayings.add(newWise);
        save(_top, newWise);
    }

    public boolean modifyWiseSaying(int id, WiseSayingEntity newWise) {
        int idx = findId(id);
        if (idx < 0) return false;

        newWise.setId(id);
        wiseSayings.set(idx, newWise);
        save(id, newWise);

        return true;
    }

    public WiseSayingEntity findWiseSaying(int id) {
        int idx = findId(id);
        if (idx < 0) return null;
        return wiseSayings.get(idx);
    }

    public boolean deleteWiseSaying(int id) {
        int idx = findId(id);
        if (idx < 0) return false;

        wiseSayings.remove(idx);

        file.delete("db/wiseSaying/%d.json".formatted(id));

        return true;
    }

    private int findId(int targetId) {
        int start = 0;
        int end = wiseSayings.size();
        while (start < end) {
            int now = (start + end) / 2;

            WiseSayingEntity entity = wiseSayings.get(now);

            if (entity.getId() == targetId) {
                return now;
            }
            if (entity.getId() < targetId) {
                start = now + 1;
                continue;
            }
            end = now;
        }
        return -1;
    }

    public boolean build() {
        List<Map<String, Object>> wiseList =
                new ArrayList<>(wiseSayings.size());

        for (WiseSayingEntity wiseSaying : wiseSayings) {
            wiseList.add(wiseSaying.toMap());
        }

        String wiseSayingJsonStr = json.toString(wiseList);

        return file.write("data.json",wiseSayingJsonStr);

    }
}
