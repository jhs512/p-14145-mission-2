package com.back;

import com.back.domain.system.controller.SystemController;
import java.util.Scanner;

public class App {
    String userCommand;
    Scanner sc = new Scanner(System.in);

    public App(Scanner sc) {
        this.sc = sc;
    }

    private void readUserCommand() {  // 사용자 입력을 받는 함수
        userCommand = sc.nextLine();
    }

    public void run() {
        SystemController systemController = new SystemController(sc);

        System.out.println("== 명언 앱 ==");

        while(true) {
            System.out.print("명령) ");
            readUserCommand();

            systemController.getCommand(userCommand);

            if (userCommand.equals("종료")) return;
        }
    }
}
