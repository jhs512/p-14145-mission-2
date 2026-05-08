package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {
    private final Scanner sc;
    private final SystemController systemController;
    private final WiseSayingController wiseSayingController;

    public App() {
        this(new Scanner(System.in));
    }

    public App(Scanner sc) {
        this.sc = sc;
        this.systemController = new SystemController();
        this.wiseSayingController = new WiseSayingController(sc);
    }

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");

            String cmd;
            try {
                cmd = sc.nextLine().trim();
            } catch (NoSuchElementException e) {
                // 입력이 끝났을 때 안전하게 종료
                break;
            }

            if (cmd.equals("종료")) {
                systemController.exit();
                break;
            } else if (cmd.equals("등록")) {
                wiseSayingController.write();
            } else if (cmd.equals("목록")) {
                wiseSayingController.list();
            } else if (cmd.startsWith("삭제?id=")) {
                int id = Integer.parseInt(cmd.split("=")[1]);
                wiseSayingController.delete(id);
            } else if (cmd.startsWith("수정?id=")) {
                int id = Integer.parseInt(cmd.split("=")[1]);
                wiseSayingController.modify(id);
            } else if (cmd.equals("빌드")) {
                wiseSayingController.build();
            }
        }
    }
}
