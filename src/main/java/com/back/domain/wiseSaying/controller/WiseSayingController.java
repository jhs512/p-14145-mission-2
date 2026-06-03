package com.back.domain.wiseSaying.controller;

import com.back.domain.constant.GuideMsg;
import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.service.WiseSayingService;
import com.back.util.PageStringUtil;

import java.util.HashMap;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc;
    private final WiseSayingService wiseSayingService;

    public WiseSayingController(Scanner sc, WiseSayingService wiseSayingService) {
        this.sc = sc;
        this.wiseSayingService = wiseSayingService;
    }

    public void execute(SystemController.CmdMsg cmd, HashMap<String,String> queryParams) {
        switch (cmd) {
            case REGISTER -> register(sc); // 없어도 되는 매개변수
            case VIEW -> view(queryParams);
            case FILTER -> filter(queryParams);
            case DELETE -> delete(queryParams);
            case EDIT -> edit(sc, queryParams);
            case BUILD -> build();
        }
    }
    private void register(Scanner sc) {
        String quote, author;
        System.out.print(GuideMsg.QUOTE.getValue());
        quote = sc.nextLine();
        System.out.print(GuideMsg.AUTHOR.getValue());
        author = sc.nextLine();

        int lastQuoteNo = wiseSayingService.register(quote, author);
        if (lastQuoteNo != -1) {
            System.out.printf(GuideMsg.REGISTER.getValue(), lastQuoteNo);
        } else {
            System.out.print(GuideMsg.EMPTY.getValue());
        }
    }
    private void view(HashMap<String, String> queryParams) {
        queryParams.putIfAbsent("page", "1");
        String page = wiseSayingService.readPage(Integer.parseInt(queryParams.get("page")));
        if (page.isEmpty()) {
            System.out.print(GuideMsg.PAGENOTEXIST.getValue().formatted(Integer.parseInt(queryParams.get("page"))));
        } else {
            System.out.print(GuideMsg.VIEW.getValue());
            System.out.print(page);
            System.out.print(GuideMsg.PAGE.getValue());
            System.out.print(PageStringUtil
                    .getPageList(Integer.parseInt(queryParams.get("page")), wiseSayingService.getPageCount()));
        }
    }
    private void filter(HashMap<String, String> queryParams) {
        System.out.printf(GuideMsg.FILTER.getValue(), queryParams.get("keywordType"), queryParams.get("keyword"));
        System.out.print(GuideMsg.VIEW.getValue());
        System.out.print(wiseSayingService.filter(queryParams.get("keywordType"), queryParams.get("keyword")));
    }
    private void delete(HashMap<String, String> queryParams) {
        int qid = Integer.parseInt(queryParams.get("id"));
        boolean isDeleted = wiseSayingService.delete(qid);
        if (isDeleted) {
            System.out.printf(GuideMsg.DELETE.getValue(), qid);
        } else {
            System.out.printf(GuideMsg.ABSENT.getValue(), qid);
        }
    }
    private void edit(Scanner sc, HashMap<String, String> queryParams) {
        int qid = Integer.parseInt(queryParams.get("id"));
        String[] tmpQ;
        try {
            tmpQ = wiseSayingService.read(qid);
        } catch (Exception e) {
            System.out.printf(GuideMsg.ABSENT.getValue(), qid); return;
        }
        System.out.print(GuideMsg.QUOTEOLD.getValue()+tmpQ[0]+"\n"+GuideMsg.QUOTE.getValue());
        String newQuote = sc.nextLine();
        if (newQuote.isEmpty()){ System.out.print(GuideMsg.EMPTY.getValue()); return;}
        System.out.print(GuideMsg.AUTHOROLD.getValue()+tmpQ[1]+"\n"+GuideMsg.AUTHOR.getValue());
        String newAuthor = sc.nextLine();
        wiseSayingService.edit(qid, newQuote, newAuthor);
    }
    private void build() {
        try{
            wiseSayingService.exportData();
            System.out.print(GuideMsg.BUILDSUCCESS.getValue());
        } catch (Exception e) {
            System.out.println(GuideMsg.BUILDFAIL.getValue() + e.getMessage());
        }
    }
}
