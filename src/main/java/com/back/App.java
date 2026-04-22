package com.back;
//com.back.App
//역할 : 사용자 입력을 받고 그것이 WiseSayingController 에게 넘거야 하는지 판단해서 맞으면 넘김(넘김의 의미 : 메서드 호출(인자와 함께))
//이 단계에서는 스캐너 사용가능
//이 단계에서는 출력 사용가능

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.service.WiseSayingService;

//컨트롤러 => 서비스 => 리포지터리 => 파일DB
public class App {
    WiseSayingController controller;
    SystemController systemController;

    App() {
        systemController = new SystemController();
        controller = new WiseSayingController(systemController.get("folder.path"));

    }
    App(String stream) {
        systemController = new SystemController();
        controller = new WiseSayingController(stream,systemController.get("folder.path"));

    }


    public void run()
    {
        String command;
        String[] splited;
        String params;
        boolean online = true;

        System.out.println("== 명언 앱 ==");
        while(online) {
            System.out.print("명령) ");
            params = "";
            command = controller.getInput();
            System.out.println();
            if(command.contains("?"))
            {
                splited = command.split("\\?");
                command = splited[0];
                params = splited[1];
            }

            if(command.equals("종료"))online = false;
            else if(command.equals("등록"))
            {
                controller.regist();
            }
            else if(command.equals("목록"))
            {
                controller.printList(params);
            }
            else if(command.contains("삭제"))
            {
                controller.delete(params);
            }
            else if(command.contains("수정"))
            {
                controller.modify(params);
            }
            else if(command.contains("빌드"))
            {
                controller.build();
            }
        }
    }
}
