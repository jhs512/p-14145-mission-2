package org.example;

//고객의 명령을 입력받고 적절한 응답을 표현 / 시스템에 관련된 응대

import java.util.Scanner;

public class SystemController {

    public String scanS(){
        Scanner sc = new Scanner(System.in);
        System.out.print("명언 : ");
        String md5 = sc.nextLine().trim();
        return md5;
    }

    public String scanH(){
        Scanner sc = new Scanner(System.in);
        System.out.print("작가 : ");
        String md5 = sc.nextLine().trim();
        return md5;
    }
}
