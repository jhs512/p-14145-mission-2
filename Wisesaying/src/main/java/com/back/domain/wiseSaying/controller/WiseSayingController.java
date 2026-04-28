package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc;
    WiseSayingService wiseSayingService = new WiseSayingService();

    public WiseSayingController(Scanner sc) {
        this.sc = sc;
    }

    public void createWiseSaying() {
        System.out.print("명언 : ");
        String content = sc.nextLine();
        System.out.print("작가 : ");
        String author = sc.nextLine();
        int id = wiseSayingService.createService(content, author);
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    public void getWiseSaying(int page) {
        List<String> getWise = wiseSayingService.getService();

        pagePrint(page, getWise);
    }

    public void searchWiseSaying(String type, String search, int page) {
        System.out.println("----------------------");
        System.out.println("검색타입 : " + type);
        System.out.println("검색어 : " + search);

        List<String> getWise = wiseSayingService.getSearchService(type, search);

        pagePrint(page, getWise);
    }

    public void deleteWiseSaying(int id) {
        if (wiseSayingService.deleteService(id)) {
            System.out.println(id + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public void updateWiseSaying(int id) {
        if (!wiseSayingService.isExistWise(id)) {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        } else {
            System.out.println("명언(기존) : " + wiseSayingService.getWiseById(id));
            System.out.print("명언 : ");
            String new_content = sc.nextLine();
            System.out.println("작가(기존) : " + wiseSayingService.getAuthorById(id));
            System.out.print("작가 : ");
            String new_author = sc.nextLine();

            wiseSayingService.updateService(id, new_content, new_author);
        }
    }

    public void buildWiseSaying() {
        wiseSayingService.buildService();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    private void pagePrint(int page, List<String> getWise) {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for (int i = (page - 1) * 5; i < Math.min((page - 1) * 5 + 5, getWise.size()); i++) {
            System.out.println(getWise.get(i));
        }

        System.out.println("----------------------");
        System.out.println("페이지 : " + page + " / [" + (int)Math.ceil((double)getWise.size() / 5) + "]");
    }
}
