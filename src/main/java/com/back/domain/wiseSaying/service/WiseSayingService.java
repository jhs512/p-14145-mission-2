package com.back.domain.wiseSaying.service;

import com.back.domain.system.controller.SystemController;
import com.back.domain.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.repository.WiseSayingRepository;

import java.util.ArrayList;
import java.util.List;

//com.back.domain.wiseSaying.service.WiseSayingService.java
//역할 : 순수 비지니스 로직
//스캐너 사용금지, 출력 금지
public class WiseSayingService {
    WiseSayingRepository repo;


    public WiseSayingService(String filePath)
    {
        repo = new WiseSayingRepository(filePath);
    }
    public String regist(String query)
    {
        String[] splited = query.split(",");
        String answer = WiseSaying.getNum() +"번 명언이 등록되었습니다.";
        //0이 명언, 1이 작가
        repo.insert(splited[0], splited[1]);
        return answer;
    }

    public WiseSaying findById(int id)
    {
        WiseSaying answer = repo.get(id);
        return answer;
    }

    public String delete(String query)
    {
        String answer = "";
        List<String[]> keyValue = SystemController.parsing(query);

        //keywordType=content&keyword=과거
        switch(keyValue.getFirst()[0])
        {
            case "id":
                answer =  repo.delete(Integer.parseInt(keyValue.getFirst()[1]));
                break;
            case "keywordType":
                break;
            case "keyword":
                break;
        }
        return answer;
    }

    public List<WiseSaying> getPage(int pageNum, String keywordType, String keyword) {
        List<WiseSaying> answer;
        if(keywordType == null) keywordType = "";

        switch(keywordType)
        {
            case "content":
                answer = repo.contentFindByKeyword(keyword);
                break;
            case "author":
                answer = repo.authorFindByKeyword(keyword);
                break;
            default:
                answer = getPage(pageNum);
        }
        answer = sepPage(answer,pageNum);
        return answer;
    }

    public List<WiseSaying> sepPage(List<WiseSaying> list, int pageNum)
    {
        List<WiseSaying> answer = new ArrayList<>();
        if(list.size() == 0)return list;
        int i = Math.max(list.size()-WiseSayingRepository.PAGE_MAX_ELEMENT*pageNum, 0);
        int end = (list.size()-1)/pageNum;
        for(;i<=end;i++)
        {
            answer.add(list.get(i));
        }
        return answer;
    }

    public List<WiseSaying> getPage(int page)
    {
        int pageNum = possiblePage(page);
        return repo.getPage(pageNum);
    }
    public int possiblePage(int page)
    {
        int pageNum = 1;
        if(page <= repo.getCntPages())
        {
            pageNum = page;
        }
        return pageNum;
    }
    public int getCntPages()
    {
        return repo.getCntPages();
    }
    public void modify(int id, String content, String author)
    {
        repo.modify(id,content,author);
    }

    public boolean build() {
        //임시
        return repo.build();
    }
}
