package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;

    // 리포지토리 주입
    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;
    }

    // 명언 등록
    public int write(String content, String author) {
        return wiseSayingRepository.save(content, author);
    }

    // 명언 목록 (검색기능 가능)
    public List<WiseSaying> findAll(String keywordType, String keyword) {
        // 키워드가 없다면 기본 명언 목록 반환

        if (keyword.isEmpty()) {
            List<WiseSaying> sortedList = new ArrayList<>(wiseSayingRepository.findAll());
            Collections.reverse(sortedList);
            return sortedList;
        }
        
        // 키워드가 있다면 필터링된 명언 목록 반환
        List<WiseSaying> wiseSayings = wiseSayingRepository.findAll();
        List<WiseSaying> filteredList = new ArrayList<>();

        for (WiseSaying ws : wiseSayings) {
            if (keywordType.equals("content")) {
                if (ws.getContent().contains(keyword)) {
                    filteredList.add(ws);
                }
            } else if (keywordType.equals("author")) {
                if (ws.getAuthor().contains(keyword)) {
                    filteredList.add(ws);
                }
            }
        }
        Collections.reverse(filteredList);
        return filteredList;
    }

    // 명언 수정
    public boolean modify(int id, String content, String author) {
        WiseSaying foundWiseSaying = findById(id);

        if (foundWiseSaying == null) {
            return false;
        }

        // 객체가 존재하는 경우 리포지토리의 수정 호출
        wiseSayingRepository.update(foundWiseSaying, content, author);
        return true;
    }

    // 명언 삭제
    public boolean remove(int id) {
        WiseSaying foundWiseSaying = findById(id);

        if (foundWiseSaying == null) {
            return false;
        }

        // 객체가 존재하는 경우 리포지토리의 삭제 호출
        wiseSayingRepository.remove(foundWiseSaying);
        return true;
    }

    // ID로 명언 조회
    public WiseSaying findById(int id) {
        return wiseSayingRepository.findById(id);
    }

    // 명언이 없을 경우 생성 기능
    public void makeSampleData() {
        if (wiseSayingRepository.findAll().isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                write("명언 " + i, "작자미상 " + i);
            }
        }
    }

}
