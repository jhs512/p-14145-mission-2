package com.back;

import com.back.domain.constant.GuideMsg;
import com.back.domain.system.controller.SystemController;

import java.util.Scanner;

public class App {
    private final Scanner sc;
    private final SystemController systemController;

    public App() {
        this.sc = AppContext.scanner;
        systemController = AppContext.systemController;
    }

    public void run() {
        System.out.print(GuideMsg.INTRO.getValue());
        systemController.run();
    }
}
