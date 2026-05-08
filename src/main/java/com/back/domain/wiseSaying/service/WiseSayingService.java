package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.List;
import java.util.Optional;

public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository;

    public WiseSayingService() {
        this.wiseSayingRepository = new WiseSayingRepository();
    }

    public WiseSaying write(String content, String author) {
        return wiseSayingRepository.save(content, author);
    }

    public List<WiseSaying> findAll() {
        return wiseSayingRepository.findAll();
    }

    public Optional<WiseSaying> findById(int id) {
        return wiseSayingRepository.findById(id);
    }

    public boolean delete(int id) {
        return wiseSayingRepository.deleteById(id);
    }

    public boolean modify(int id, String content, String author) {
        Optional<WiseSaying> opt = wiseSayingRepository.findById(id);
        if (opt.isEmpty()) return false;

        WiseSaying ws = opt.get();
        ws.setContent(content);
        ws.setAuthor(author);
        wiseSayingRepository.save(ws);
        return true;
    }

    public void build() {
        wiseSayingRepository.buildDataJson();
    }
}