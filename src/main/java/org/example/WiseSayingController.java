package org.example;

//고객의 명령을 입력받고 적절한 응답을 표현 / 명언에 관한 응대

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class WiseSayingController {
    public void generatePrint(int count){
        System.out.println(count + "번 명언이 등록되었습니다.");
    }

    public void viewPrint(ArrayList<WiseSaying> arrList,String input, String key, int page){
        System.out.println("----------------------");
        System.out.println("검색타입 : " + input);
        System.out.println("검색어 : " + key);
        System.out.println("----------------------");
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        ArrayList<WiseSaying> wsal2 = new ArrayList<>();

        if(input.equals("content")){
            for (int i = arrList.size() - 1; i >= 0; i--) {
                WiseSaying ws = arrList.get(i);
                if(ws.wise.contains(key)){
                    wsal2.add(ws);
                }
            }
        }else{
            for (int i = arrList.size() - 1; i >= 0; i--) {
                WiseSaying ws = arrList.get(i);
                if(ws.writer.contains(key)){
                    wsal2.add(ws);
                }
            }
        }

        int idx = wsal2.size() - page * 5 ;
        int idx2 = wsal2.size() - (page-1) * 5 ;

        if(idx < 0){
            idx = 0;
        }
        if(idx2 < 0){
            idx2 += 5;
        }

        for (int i = idx2 -1 ; i >= idx; i--) {
            WiseSaying ws = wsal2.get(i);
            System.out.println(ws.number + " / " + ws.writer + " / " + ws.wise);
        }

        System.out.println("----------------------");
        System.out.print("페이지 : ");
        for(int i = 1; i <= wsal2.size()/5+1; i++){
            if(i == page){
                System.out.print("["+i+"]");
            } else {
                System.out.printf("%d", i);
            }
            if(i != wsal2.size()/5+1) {
                System.out.print(" / ");
            }
        }
        System.out.println("");

    }

    public void viewPrint2(ArrayList<WiseSaying> arrList , int page){
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        int idx = arrList.size() - page * 5 ;
        int idx2 = arrList.size() - (page-1) * 5 ;

        if(idx < 0){
            idx = 0;
        }

        for (int i = idx2-1; i >= idx; i--) {
            WiseSaying ws = arrList.get(i);
            System.out.println(ws.number + " / " + ws.writer + " / " + ws.wise);

        }

        System.out.println("----------------------");
        System.out.print("페이지 : ");
        for(int i = 1; i <= arrList.size()/5+1; i++){
            if(i == page){
                System.out.print("["+i+"]");
            } else {
                System.out.printf("%d", i);
            }
            if(i != arrList.size()/5+1) {
                System.out.print(" / ");
            }
        }
        System.out.println("");
    }
    public void deleteWiseSaying(int md5, int f){
        if(f ==1){
            System.out.println(md5 + "번 명언은 존재하지 않습니다.");
        }else{
            System.out.println(md5 + "번 명언이 삭제되었습니다.");
        }
    }
    public void build(ArrayList<WiseSaying> arrList){
        try{
            File file1 = new File("src/main/resources/db/wiseSaying/data.json");

            file1.getParentFile().mkdirs(); //클래스 저장

            FileWriter fw1 = new FileWriter(file1);
            BufferedWriter writer = new BufferedWriter(fw1);
            StringBuilder sb = new StringBuilder("["+"\n");
            for (int i = 0; i < arrList.size(); i++) {
                sb.append(arrList.get(i).printString());
                if (i < arrList.size() - 1) sb.append(",\n");
            }
            sb.append("\n"+"]");
            writer.write(sb.toString());

            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
    public void scanWiseSaying1(String md5){
        System.out.println("명언(기존) : " + md5);
    }
    public void scanWiseSaying2(String md5){
        System.out.println("작가(기존) : " + md5);
    }
}
