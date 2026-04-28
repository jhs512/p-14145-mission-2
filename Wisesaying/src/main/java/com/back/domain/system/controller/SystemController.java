package com.back.domain.system.controller;

import com.back.domain.wiseSaying.controller.WiseSayingController;

import javax.swing.*;
import java.util.Scanner;

public class SystemController {
    WiseSayingController wiseSayingController;

    public SystemController(Scanner sc) {
        wiseSayingController = new WiseSayingController(sc);
    }

    public void getCommand(String userCommand) {
        if (userCommand.contains("등록")) {
            wiseSayingController.createWiseSaying();
        } else if (userCommand.contains("빌드")) {
            wiseSayingController.buildWiseSaying();
        } else if (userCommand.contains("종료")) {
            System.out.println("프로그램을 종료합니다.");
        } else if (userCommand.contains("목록")) {
            int page = 1;
            String keywordType = "";
            String keyword = "";
            if (userCommand.contains("?")) {
                String[] splitCommand = userCommand.split("\\?");
                String[] detailCommand = splitCommand[1].split("&");
                for (String detail : detailCommand) {
                    if (detail.contains("page")) {
                        page = Integer.parseInt(detail.split("=")[1]);
                    }
                    if (detail.contains("keywordType")) {
                        keywordType = detail.split("=")[1];
                    }
                    if (detail.contains("keyword")) {
                        keyword = detail.split("=")[1];
                    }
                }
            }
            if (!keyword.isEmpty() && !keywordType.isEmpty()) {
                wiseSayingController.searchWiseSaying(keywordType, keyword, page);
            } else {
                wiseSayingController.getWiseSaying(page);
            }
        } else {
            String[] splitCommand = userCommand.split("\\?id=");
            switch (splitCommand[0]) {
                case "삭제":
                    wiseSayingController.deleteWiseSaying(Integer.parseInt(splitCommand[1]));
                    break;
                case "수정":
                    wiseSayingController.updateWiseSaying(Integer.parseInt(splitCommand[1]));
                    break;
            }
        }
    }
}
