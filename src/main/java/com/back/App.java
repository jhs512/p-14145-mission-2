package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private final Scanner sc;
    private final WiseSayingController wiseSayingController;
    private final SystemController systemController;

    public App(Scanner sc) {
        this.sc = sc;

        WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
        WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);
        wiseSayingService.makeSampleData();

        this.wiseSayingController = new WiseSayingController(wiseSayingService, sc);
        this.systemController = new SystemController();
    }

    public void run() {

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine().trim();

            if (command.equals("종료")) {
                systemController.exit();
                break;
            }
            else if (command.equals("등록")) {
                wiseSayingController.doWrite();
            } else if (command.startsWith("목록")) {
                wiseSayingController.showList(command);
            } else if (command.startsWith("수정?")) {
                wiseSayingController.doModify(command);
            } else if (command.startsWith("삭제?")) {
                wiseSayingController.doRemove(command);
            } 
        }
    }
}
