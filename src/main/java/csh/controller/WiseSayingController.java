package csh.controller;

import csh.entity.WiseSaying;
import csh.service.WiseSayingService;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc;
    private final WiseSayingService service = new WiseSayingService();


    public WiseSayingController(Scanner sc) {
        this.sc = sc;
    }

    public void requestCreate() {
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();
        System.out.print("저자 : ");
        String author = sc.nextLine().trim();
        int id = service.create(content, author);
        System.out.println("%d번 명언이 등록되었습니다.".formatted(id));
    }

    public void requestShowList() {
        List<WiseSaying> list = service.getList();
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (WiseSaying wiseSaying : list) {
            System.out.println(wiseSaying.getId() + " / " + wiseSaying.getAuthor() + " / " + wiseSaying.getContent());
        }
    }

    public void requestUpdate(int id) {
        WiseSaying ws = service.findById(id);
        if (ws == null) {
            System.out.println("해당 명언은 존재 하지않습니다.");
            return;
        }
        System.out.println("명언(기존) : %s".formatted(ws.getContent()));
        System.out.print("명언 : ");
        String content = sc.nextLine().trim();
        System.out.println("저자(기존) : %s".formatted(ws.getAuthor()));
        System.out.print("저자 : ");
        String author = sc.nextLine().trim();
        service.update(ws, content, author);
        System.out.println("%d번 명언이 수정되었습니다.".formatted(id));
    }

    public void requestDelete(int id) {
        boolean isDeleted = service.deleteById(id);
        if (isDeleted) System.out.println("%d번 명언이 삭제되었습니다".formatted(id));
        else System.out.println("해당 명언은 존재하지 않습니다");
    }

    public void requestBuild() {
        service.buildJsonArray();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
