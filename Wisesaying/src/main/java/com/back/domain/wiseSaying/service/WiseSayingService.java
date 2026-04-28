package com.back.domain.wiseSaying.service;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.ArrayList;
import java.util.List;

public class WiseSayingService {
    WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();

    public int createService(String content, String author) {
        return wiseSayingRepository.create(content, author);
    }

    public List<String> getService() {
        List<String> getWise = new ArrayList<>();

        for (int i = wiseSayingRepository.getCount(); i >= 1; i--) {
            WiseSaying w = wiseSayingRepository.findById(i);
            if (w == null) continue;

            getWise.add(w.id + " / " + w.author + " / " + w.content);
        }

        return getWise;
    }

    public List<String> getSearchService(String type, String search) {
        List<String> getWise = new ArrayList<>();

        for (int i = wiseSayingRepository.getCount(); i >= 1; i--) {
            WiseSaying w = wiseSayingRepository.findById(i);
            if (w == null) continue;

            if (type.equals("author")) {
                if (w.author.contains(search)) getWise.add(w.id + " / " + w.author + " / " + w.content);
            }

            if (type.equals("content")) {
                if (w.content.contains(search)) getWise.add(w.id + " / " + w.author + " / " + w.content);
            }
        }

        return getWise;
    }

    public Boolean deleteService(int id) {
        if (!isExistWise(id)) return false;

        wiseSayingRepository.deleteById(id);
        return true;
    }

    public void updateService(int id, String new_content, String new_author) {
        wiseSayingRepository.update(id, new_content, new_author);
    }

    public Boolean isExistWise(int id) {
        return wiseSayingRepository.findById(id) != null;
    }

    public String getWiseById(int id) {
        return wiseSayingRepository.findById(id).content;
    }

    public String getAuthorById(int id) {
        return wiseSayingRepository.findById(id).author;
    }

    public void buildService() {
        wiseSayingRepository.build();
    }

}
