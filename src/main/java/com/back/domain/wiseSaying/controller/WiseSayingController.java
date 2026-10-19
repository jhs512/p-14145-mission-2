// 고객의 명령을 입력받고 적절한 응답을 표현 - 명언에 대한 응대
// 스캐너, 출력 사용 가능
// 배열, json 파일을 조작하지 않음

package com.back.domain.wiseSaying.controller;

import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.Scanner;

public class WiseSayingController {
    private final Scanner sc;
    private final WiseSayingService wiseSayingService;

    // 생성자
    public WiseSayingController(Scanner sc) {
        this.sc = sc;
        this.wiseSayingService = new WiseSayingService();
    }

    public void enroll(){

        System.out.print("명언) : ");
        String content = sc.nextLine().trim();

        System.out.print("작가) : ");
        String author = sc.nextLine().trim();

        WiseSaying saying = wiseSayingService.enroll(content, author);
        System.out.println(saying.getId() + "번 명언이 등록되었습니다.");
    }

    public void delete(){
        System.out.println("삭제할 명언의 번호를 입력해주세요. ");
        int delete_number = Integer.parseInt(sc.nextLine().trim());

        // 추후 WiseSayingService.delete() 호출
        boolean success = wiseSayingService.deleteById(delete_number);

        if (success) {
            System.out.println(delete_number + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(delete_number + "번 명언은 존재하지 않습니다.");
        }
    }

    public void edit(){
        System.out.println("수정할 명언의 번호를 알려주세요. ");
        int edit_number = Integer.parseInt(sc.nextLine().trim());

        // 수정하려는 명언이 존재하는지 확인
        WiseSaying saying =  wiseSayingService.findById(edit_number);

        if (saying == null) {
            System.out.println("존재하지 않는 명언입니다.");
            return;
        }

        System.out.println("명언(기존): " + saying.getContent());
        System.out.print("명언(수정) : ");
        String newContent = sc.nextLine().trim();

        System.out.println("작가(기존): " + saying.getAuthor());
        System.out.print("작가(수정) : ");
        String newAuthor = sc.nextLine().trim();

        // Service에 수정 요청
        boolean success = wiseSayingService.editById(edit_number, newContent, newAuthor);

        if (success) {
            System.out.println(edit_number + "번 명언이 수정되었습니다.");
        } else {
            System.out.println("명언 수정에 실패했습니다.");
        }
    }
    public void show(){
        WiseSaying[] sayings = wiseSayingService.getAllList();

        System.out.println("번호 / 작가 / 명언");
        System.out.println("-----------------------");

        // Repository에서 이미 최신순으로 반환
        for (WiseSaying saying : sayings) {
            System.out.println(saying);
        }

    }
    }
