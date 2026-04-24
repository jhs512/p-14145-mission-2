package com.back.domain.wiseSaying.service;

import com.back.AppContext;
import com.back.domain.wiseSaying.entity.WiseSayingEntity;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.*;

public class WiseSayingService {
    private final WiseSayingRepository wiseSayingRepository = AppContext.wiseSayingRepository;

    public List<WiseSayingEntity> findForList() {
        return wiseSayingRepository.findForList();
    }

    public int write(String content, String author) {
        WiseSayingEntity wiseSaying =
                new WiseSayingEntity(content, author);

        wiseSayingRepository.addWiseSaying(wiseSaying);

        return wiseSaying.getId();
    }

    public WiseSayingEntity findById(int id) {
        return wiseSayingRepository.findWiseSaying(id);
    }

    public boolean modify(int id, String content, String author) {
        WiseSayingEntity wiseSaying =
                new WiseSayingEntity(content, author);

        return wiseSayingRepository.modifyWiseSaying(id, wiseSaying);
    }

    public boolean delete(int id) {
        return wiseSayingRepository.deleteWiseSaying(id);
    }

    public boolean build() {
        return wiseSayingRepository.build();
    }
}
