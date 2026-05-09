package csh;

import csh.controller.WiseSayingController;

import java.util.Scanner;

public class App {
    private final Scanner sc = new Scanner(System.in);
    private final WiseSayingController controller = new WiseSayingController(sc);
    public void run() {
        InitApp.init();

        while(true) {
            System.out.print("명령) ");
            String command = sc.nextLine().trim();
            Rq rq = new Rq(command);
            String actionName = rq.getActionName();
            switch (actionName) {
                case "exit" -> {
                    System.out.println("프로그램 종료");
                    return;
                }
                case "create" -> controller.requestCreate();
                case "list" -> controller.requestShowList();
                case "edit" -> {
                    int id = rq.getIntValue("id",-1);
                    if (id == -1) {
                        System.out.println("해당 명언은 존재하지 않습니다");
                        break;
                    }
                    controller.requestUpdate(id);
                }
                case "delete" -> {
                    int id = rq.getIntValue("id",-1);
                    if (id == -1) {
                        System.out.println("해당 명언은 존재하지 않습니다");
                        break;
                    }
                    controller.requestDelete(id);
                }
                case "build" -> controller.requestBuild();
                default -> System.out.println("잘못된 입력입니다. 다시 입력하세요");
            }
        }
    }
}
