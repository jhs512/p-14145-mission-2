package com.back.domain.wiseSaying.controller;
import com.back.AppContext;
import com.back.Cmd;
import com.back.domain.wiseSaying.entity.WiseSayingEntity;

import java.util.*;

public class WiseSayingController {

    private List<WiseSayingEntity> filterKeyword(List<WiseSayingEntity> list,
                               String type, String value) {
        if (type == null || value == null) return list;

        return list.stream().filter((var) -> {
            if (type.equals("content")) {
                return var.wise.contains(value);
            }
            if (type.equals("author")) {
                return var.author.contains(value);
            }
            return false;
        }).toList();
    }

    private int getPage(Cmd cmd) {
        String pageValue = cmd.getArg("page");
        if (pageValue != null) {
            return Integer.parseInt(pageValue) - 1;
        }
        return 0;
    }

    private void printPage(int now, int max) {


        System.out.print("페이지 : ");
        for (int i = 0; i <= max; i++) {
            if (i == now) {
                System.out.printf("[%d]", i + 1);
            }
            else {
                System.out.printf("%d", i + 1);
            }
            if (i < max) {
                System.out.print(" / ");
            }
        }
        System.out.println();
    }

    public void actionList(Cmd cmd) {
        List<WiseSayingEntity> entities =
                AppContext.wiseSayingService.findForList();

        entities = filterKeyword(entities,
                cmd.getArg("keywordType"),
                cmd.getArg("keyword"));

        int page = getPage(cmd);
        int startIdx = page * 5;
        int endIdx = page * 5 + 5;

        System.out.println("번호 / 작가 / 명언");
        System.out.println("====================");

        for (int i = startIdx; i < endIdx; i++) {
            if (i >= entities.size()) break;
            WiseSayingEntity entity = entities.get(i);
            System.out.printf("%d / %s / %s\n",
                    entity.getId(), entity.author, entity.wise);
        }

        printPage(page, (entities.size() - 1) / 5);
    }

    public void actionWrite() {
        System.out.print("명언 : ");
        String content = AppContext.scanner.nextLine();
        System.out.print("작가 : ");
        String author = AppContext.scanner.nextLine();
        int id = AppContext.wiseSayingService.write(content, author);
        System.out.printf("%d번 명언이 등록되었습니다.\n", id);
    }

    public void actionDelete(Cmd cmd) {
        String id = cmd.getArg("id");
        if (id == null) return;

        int targetId = Integer.parseInt(id);
        if (!AppContext.wiseSayingService.delete(targetId)) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId);
            return;
        }
        System.out.printf("%d번 명언이 삭제되었습니다.\n", targetId);
    }

    public void actionModify(Cmd cmd) {
        String id = cmd.getArg("id");
        if (id == null) return;

        int targetId = Integer.parseInt(id);

        WiseSayingEntity entity =
                AppContext.wiseSayingService.findById(targetId);

        if (entity == null) {
            System.out.printf("%d번 명언은 존재하지 않습니다.\n", targetId);
            return;
        }

        System.out.printf("명언(기존) : %s\n", entity.wise);
        System.out.print("명언 : ");
        String content = AppContext.scanner.nextLine();
        System.out.printf("작가(기존) : %s\n", entity.author);
        System.out.print("작가 : ");
        String author = AppContext.scanner.nextLine();
        AppContext.wiseSayingService.modify(targetId, content, author);
    }

    public void actionBuild() {

        if (AppContext.wiseSayingService.build())
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        else
            System.out.println("빌드 실패");
    }
}
