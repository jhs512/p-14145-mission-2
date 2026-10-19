// 사용자 입력을 받고 ,  그것이 옳은 인자인지 판단하여 WiseSayingController 에게 넘김
// 스캐너와 출력 사용 가능

package com.back;

import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.system.controller.SystemController;

import java.util.Scanner;

public class App  {
    private final Scanner sc = new Scanner(System.in);

    SystemController systemController = new SystemController();
    WiseSayingController wiseSayingController = new WiseSayingController(sc);

    public void run() {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.println("명령) ");
            String prompt = sc.nextLine().trim();

            // IntelliJ 터미널에서 한글 씹힘 이슈로 영어 입력 받음
            switch (prompt) {
                case "enroll": // 등록
                    wiseSayingController.enroll();
                    break;
                case "show":
                    wiseSayingController.show();
                    break;
                case "delete":
                    wiseSayingController.delete();
                    break;
                case "edit":
                    wiseSayingController.edit();
                    break;

                case "exit":
                    systemController.exit();
                    return;
                default:
                    System.out.println("옳지 않은 INPUT. 다시 입력해주세요.");
            }

        }
    }
}
