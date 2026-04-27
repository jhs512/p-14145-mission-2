package com.back;

import com.back.standard.util.TestUtil;

import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class AppTestRunner {
    public static String run(String input) {
        // 1. 표준 출력(System.out) 리다이렉션: 콘솔에 찍히는 내용을 메모리(ByteArray)로 가로챔
        ByteArrayOutputStream output = TestUtil.setOutToByteArray();

        // 2. 표준 입력(System.in) 시뮬레이션: 문자열 기반의 스캐너를 생성하여 입력값 주입
        // 입력을 마친 후 앱이 정상 종료되도록 마지막에 "종료" 커맨드 강제 포함
        Scanner sc = TestUtil.genScanner(input.trim() + "\n종료\n");

        // 3. 의존성 주입(DI) 및 앱 실행: 생성된 스캐너를 주입하여 애플리케이션 로직 수행
        new App(sc).run();

        // 4. 데이터 캡처: 스트림에 기록된 출력 내용을 문자열로 변환 및 정제(trim)
        String result = output.toString().trim();

        // 5. 시스템 상태 복구: 가로챘던 표준 출력 스트림을 다시 원래의 콘솔(System.out)로 복구
        TestUtil.clearSetOutToByteArray();

        return result;
    }
}
