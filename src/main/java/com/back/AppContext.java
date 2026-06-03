package com.back;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.controller.WiseSayingController;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Scanner;

public class AppContext {
    public static Scanner scanner = new Scanner(System.in);
    public static WiseSayingRepository wiseSayingRepository = new WiseSayingRepository();
    public static WiseSayingService wiseSayingService = new WiseSayingService(wiseSayingRepository);
    public static WiseSayingController wiseSayingController = new WiseSayingController(scanner, wiseSayingService);
    public static SystemController systemController = new SystemController(scanner, wiseSayingController);

    public static void renew(Scanner sc) {
        scanner = sc;
        wiseSayingRepository = new WiseSayingRepository();
        wiseSayingService = new WiseSayingService(wiseSayingRepository);
        wiseSayingController = new WiseSayingController(scanner, wiseSayingService);
        systemController = new SystemController(scanner, wiseSayingController);

        initData();
    }

    public static void renew() {
        renew(new Scanner(System.in));
    }

    private static void initData() {
        if (wiseSayingService.readPage(1).isEmpty()) {
            int quoteTotalNo = 10;
            for(int i = 1; i <= quoteTotalNo; i++) {
                wiseSayingService.register("명언 " + i, "작자미상 " + i);
            }
        }
    }
}
