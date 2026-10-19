package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

public class WiseSayingService {

    private final WiseSayingRepository wiseSayingRepository;

    public WiseSayingService() {
        this.wiseSayingRepository = new WiseSayingRepository();
    }

    // 명언 enroll - service에서 content, author를 받아옴
    public WiseSaying enroll(String content, String author) {
        return wiseSayingRepository.saveNew(content, author);
    }

    // 명언 목록 조회
    public WiseSaying[] getAllList() {
        return wiseSayingRepository.findAll();
    }

    // id로 WiseSaying 조회
    public WiseSaying findById(int id) {
        return wiseSayingRepository.findById(id);
    }

    // 명언 삭제
    public boolean deleteById(int id) {
        WiseSaying saying = wiseSayingRepository.findById(id);

        if(saying == null) return false; // 삭제하지 못했으면 false

        wiseSayingRepository.deleteById(id);
        return true;
    }

    // 명언 수정
    public boolean editById(int id, String content, String author) {
        WiseSaying saying = wiseSayingRepository.findById(id);

        if (saying == null) {
            return false;
        }

        saying.setContent(content);
        saying.setAuthor(author);
        wiseSayingRepository.editById(saying);

        return true;
    }



}
