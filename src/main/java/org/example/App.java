package org.example;

//사용자 입력을 받고 WiseSayingController 에게 넘거야 하는지 판단해서 맞으면 넘김

import java.io.File;
import java.util.Scanner;

public class App {
    public void run() {
        Scanner sc = new Scanner(System.in);
        System.out.println("== 명언 앱 ==");

        WiseSayingRepository ac =  new WiseSayingRepository();
        int count = ac.loadLastId();

        while(true) {
            System.out.print("명령)");
            String md = sc.nextLine().trim();

            if (md.equals("종료")) {
                return;
            }

            if (md.equals("등록")) {
                System.out.print("명언 : ");
                String md2 = sc.nextLine().trim();
                System.out.print("작가 : ");
                String md3 = sc.nextLine().trim();
                count++;
               ac.generateWiseSaying(count, md2 , md3);

            } else if (md.contains("목록")) {
                int page = 1;

                if(md.contains("keyword")){
                    int start = md.indexOf("keyword=") + "keyword=".length();
                    int end = md.indexOf("&", start);
                    String str = (end == -1) ? md.substring(start) : md.substring(start, end);

                    if(md.contains("content")){
                        if(md.contains(("page"))){
                            int start2 = md.indexOf("page=") + "page=".length();
                            int end2 = md.indexOf("&", start2);

                            page = Integer.parseInt(end2 == -1 ? md.substring(start2) : md.substring(start2, end2));

                            ac.viewWiseSaying(1,str , page);
                        }else{
                            page = 1;
                            ac.viewWiseSaying(1,str , page);
                        }
                    }else{
                        if(md.contains(("page"))){
                            int start2 = md.indexOf("page=") + "page=".length();
                            int end2 = md.indexOf("&", start2);

                            page = Integer.parseInt(end2 == -1 ? md.substring(start2) : md.substring(start2, end2));

                            ac.viewWiseSaying(2,str , page);
                        }else{
                            page = 1;
                            ac.viewWiseSaying(2, str, page);
                        }
                    }
                }else{
                    if(md.contains(("page"))){
                        int start2 = md.indexOf("page=") + "page=".length();
                        int end2 = md.indexOf("&", start2);

                        page = Integer.parseInt(end2 == -1 ? md.substring(start2) : md.substring(start2, end2));

                        ac.viewWiseSaying2(page);
                    }else{
                        page = 1;
                        ac.viewWiseSaying2(page);
                    }
                }

            } else if (md.contains("삭제")) {
                int md4 = Integer.parseInt(md.substring(6).trim());
                ac.deleteWiseSaying(md4);

            } else if (md.contains("수정")) {
                int md4 = Integer.parseInt(md.substring(6).trim());
                ac.modifyWiseSaying(md4);

            } else if (md.contains("빌드")) {
                ac.buildWiseSaying();

            }else {
                System.out.println("잘못된접근");
            }
        }



    }

}
