package com.back.domain.wiseSaying.controller;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.service.WiseSayingService;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

//com.back.domain.wiseSaying.controller.WiseSayingController.java
//역할 : 고객의 명령을 입력받고 적절한 응답을 표현
//이 단계에서는 스캐너 사용가능
//이 단계에서는 출력 사용가능
//역할 : 명언에 관련된 응대
public class WiseSayingController {
    WiseSayingService wservice;
    Scanner sc;

    public WiseSayingController(String filePath)
    {
        wservice = new WiseSayingService(filePath);
        this.sc = new Scanner(System.in);
    }
    public WiseSayingController(String stream, String filePath)
    {
        wservice = new WiseSayingService(filePath);
        this.sc = new Scanner(stream);
    }

    public void build(){
        if(wservice.build())
        {
            System.out.println("data.json 파일의 내용이 갱신되었습니다.");
        }
    }
    public String getInput()
    {
        return sc.nextLine().trim();
    }

    public void delete(String params)
    {
        System.out.println(wservice.delete(params));
    }
    public void regist()
    {
        StringBuilder sb = new StringBuilder();
        System.out.print("명언 : ");
        String context = sc.nextLine().trim();
        System.out.println();
        sb.append(context)
                        .append(",");

        System.out.print("작가 : ");
        String author = sc.nextLine().trim();
        System.out.println();
        sb.append(author);

        System.out.println(wservice.regist(sb.toString()));
    }

    public void modify(String params)
    {
        StringBuilder answer = new StringBuilder();
        List<String[]> keyValue = SystemController.parsing(params);
        int index = -1;
        for(String[] pair : keyValue)
        {
            if(pair[0].equals("id"))
            {
                index =  Integer.parseInt(pair[1]);
                break;
            }
        }
        WiseSaying ws = wservice.findById(index);

        if(ws == null)
        {
            System.out.println(index+"번 명언은 존재하지 않습니다.");
            return;
        }

        //content
        String afterContent = "";
        System.out.println("명언(기존) : "+ ws.getContent());
        System.out.print("명언 : ");
        afterContent = sc.nextLine().trim();

        //author
        String afterAuthor = "";
        System.out.println("작가(기존) : "+ ws.getAuthor());
        System.out.print("작가 : ");
        afterAuthor = sc.nextLine().trim();

        wservice.modify(ws.getIdx(), afterContent, afterAuthor);
    }

    public void printList(String params)
    {
        StringBuilder answer  = new StringBuilder();
        List<WiseSaying> wList = null;
        HashMap<String, String> mapping = new HashMap<>();
        int totalPages = wservice.getCntPages();
        int page = 1;
        if(!params.equals(""))
        {
            boolean checkType = false;
            List<String[]> keyValues = SystemController.parsing(params);
            StringBuilder searchTypeString = new StringBuilder();

            searchTypeString.append("------------------------\n");
            for(String[] kv : keyValues)
            {
                String key = kv[0];
                String value = kv[1];
                mapping.put(key,value);
                switch (key)
                {
                    case "keywordType":
                        searchTypeString.append("검색타입 : "+value+"\n");
                        checkType = true;
                        break;
                    case "keyword":
                        searchTypeString.append("검색어 : "+value+"\n");
                        checkType = true;
                        break;
                    case "page":
                        page = Integer.parseInt(value);
                }
            }
            searchTypeString.append("------------------------\n");
            if(checkType) answer.append(searchTypeString.toString());

            wList = wservice.getPage(page, mapping.get("keywordType"), mapping.get("keyword"));
        }
        else{
            page = wservice.possiblePage(page);
            wList = wservice.getPage(page);
        }

        for(int i=wList.size()-1; i>=0; i--)answer.append(wList.get(i));
        answer.append("------------------------\n")
                        .append("페이지 : ");
        for(int i = 1; i <= totalPages; i++)
        {
            if(i!=1)answer.append(" / ");
            if(i == page)
                answer.append("["+i+"]");
            else
                answer.append(i);
        }
        answer.append("\n");
        System.out.println("번호 / 작가 / 명언");
        System.out.println("------------------------");
        System.out.print(answer.toString());

    }
}
