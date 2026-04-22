package com.back;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.standard.util.TestUtil;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class AppTestRunner {
    public static String run(String str){
        String answer = "";
        str+="\n종료";
        Scanner scanner = TestUtil.genScanner(str);
        ByteArrayOutputStream bs = TestUtil.setOutToByteArray();
        new App(str).run();
        String out = bs.toString().trim();
        TestUtil.clearSetOutToByteArray();
        return out;

    }
    public static void settingInit(){
        WiseSaying.setNum(1);
    }
}
