package com.back;
import java.util.Scanner;

public class App {

    public void run() {

        System.out.println("== 명언 앱 ==");

        Scanner sc = AppContext.scanner;

        label:
        while (true) {
            System.out.print("명령) ");
            Cmd cmd = new Cmd(sc.nextLine().trim());

            switch (cmd.getCmd()) {
                case "종료":
                    break label;
                case "등록":
                    AppContext.wiseSayingController.actionWrite();
                    continue;
                case "목록":
                    AppContext.wiseSayingController.actionList(cmd);
                    continue;
                case "삭제":
                    AppContext.wiseSayingController.actionDelete(cmd);
                    continue;
                case "수정":
                    AppContext.wiseSayingController.actionModify(cmd);
                    break;
                case "빌드":
                    AppContext.wiseSayingController.actionBuild();
                    break;
            }
        }
    }
}
