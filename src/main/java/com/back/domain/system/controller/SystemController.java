package com.back.domain.system.controller;

import com.back.domain.constant.GuideMsg;
import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.util.HashMap;
import java.util.Scanner;

import static com.back.domain.system.controller.SystemController.CmdMsg.*;

// 입력받을 명령어
public class SystemController {
    private final Scanner sc;
    private final WiseSayingController wiseSayingController;
    private static HashMap<String, String> queryParams = new HashMap<>(); // 다른 방법?
    public enum CmdMsg {
        NONE, DELETE, EDIT, FILTER, EXIT, REGISTER, VIEW, BUILD;
        public static CmdMsg from(String input) {
            // '?'로 시작하는 추가적인 요청이 있는가
            boolean hasQueryParam = parseQuery(input);
            // 삭제 및 수정
            if (hasQueryParam) {
                switch (input.substring(0, 2)) {
                    case "삭제" -> {
                        if (!queryParams.containsKey("id")) { return NONE;}
                        return DELETE;}
                    case "수정" -> {
                        if (!queryParams.containsKey("id")) { return NONE;}
                        return EDIT;}
                    case "목록" ->  {
                        if (queryParams.containsKey("keywordType")) { return FILTER;}
                        return VIEW;
                    }
                }
            }
            // 종료, 등록, 목록, 빌드
            return switch (input) {
                case "종료" -> EXIT;
                case "등록" -> REGISTER;
                case "목록" -> VIEW;
                case "빌드" -> BUILD;
                default -> NONE;
            };
        }
        private static boolean parseQuery(String input) {
            String[] tmp = input.split("\\?");
            if (tmp.length == 2) {
                String[] params = tmp[1].split("&");
                for (String s : params) {
                    String[] keyValue = s.split("=");
                    if (keyValue.length != 2
                            || keyValue[1].trim().isEmpty()
                            || keyValue[0].equals("page") && !keyValue[1].matches("\\d+")
                            || keyValue[0].equals("id") && !keyValue[1].matches("\\d+")) {
                        queryParams.clear();
                        return false;
                    }
                    queryParams.put(keyValue[0], keyValue[1]);
                }
                return true;
            } else { queryParams.clear(); return false; }
        }
    }

    public SystemController(Scanner sc, WiseSayingController wiseSayingController) {
        this.sc = sc;
        this.wiseSayingController = wiseSayingController;
    }

    public void run() {
        String input;
        while (true) {
            System.out.print(GuideMsg.CMD.getValue());
            input = sc.nextLine();
            CmdMsg currentCmd = from(input);
            switch (currentCmd) {
                case NONE -> System.out.print(GuideMsg.ERROR.getValue());
                case EXIT -> { return;}
                case REGISTER -> wiseSayingController.execute(REGISTER, queryParams);
                case VIEW -> wiseSayingController.execute(VIEW, queryParams);
                case FILTER -> wiseSayingController.execute(FILTER, queryParams);
                case DELETE -> wiseSayingController.execute(DELETE, queryParams);
                case EDIT -> wiseSayingController.execute(EDIT, queryParams);
                case BUILD -> wiseSayingController.execute(BUILD, queryParams);
            }
        }
    }
}