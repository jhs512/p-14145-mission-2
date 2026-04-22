package com.back.domain.wiseSaying.repository;

import com.back.domain.wiseSaying.entity.WiseSaying;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//com.back.domain.wiseSaying.repository.WiseSayingRepository.java
//역할 : 데이터의 조회/수정/삭제/생성을 담당
//스캐너 사용금지, 출력 금지
public class WiseSayingRepository {
    List<WiseSaying> mydb;
    public final static int PAGE_MAX_ELEMENT = 5;
    private final String folderPath;
    public WiseSayingRepository(String folderPath)
    {
        this.folderPath = folderPath;
        mydb = loadAll(folderPath);
        mydb.sort(Comparator.comparing(WiseSaying::getIdx));
    }
    public WiseSaying get(int id)
    {
        if(!contains(id)) return null;
        int index = indexOf(id);
        return mydb.get(index);
    }
    public List<WiseSaying> getAll()
    {
        return this.mydb;
    }

    public List<WiseSaying> contentFindByKeyword(String keyword) {
        List<WiseSaying> answer = new ArrayList<>();
        for(WiseSaying ws : mydb)
        {
            if(ws.getContent().contains(keyword))
                answer.add(ws);
        }
        return answer;
    }
    public void modify(int index, String content, String author)
    {
        WiseSaying ws;
        if(!contains(index))return;
        int idx = indexOf(index);
        ws = mydb.get(idx);
        ws.setContent(content);
        ws.setAuthor(author);
        update(ws);
    }

    private boolean update(WiseSaying ws)
    {
        //임시
        boolean answer = false;
        answer = deleteFile(ws);
        answer = save(ws);
        return answer;
    }


    public void insert(String content, String author)
    {
        WiseSaying ws = new WiseSaying(content,author);
        save(ws);
        mydb.add(ws);
    }

    public void insert(String content)
    {
        mydb.add(new WiseSaying(content, "작자미상" + WiseSaying.getNum()));
    }
    public String delete(int index)
    {
        String answer = index+"번 명언은 존재하지 않습니다.";
        if(!contains(index)) return answer;
        int idx = indexOf(index);
        WiseSaying ws = mydb.get(idx);
        deleteFile(ws);
        mydb.remove(idx);
        answer = index+"번 명언이 삭제되었습니다.";
        return answer;
    }
    public boolean deleteFile(WiseSaying ws) {
        try {
            return Files.deleteIfExists(Path.of( folderPath + ws.getIdx() + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean contains(int idx)
    {
        for(WiseSaying ws : mydb)
        {
            if(ws.getIdx() == idx) return true;
        }
        return false;
    }

    private int indexOf(int index)
    {
        for(int i=0; i<mydb.size(); i++)
        {
            if(mydb.get(i).getIdx() == index) return i;
        }
        return -1;
    }

    public List<WiseSaying> authorFindByKeyword(String param) {
        List<WiseSaying> answer = new ArrayList<>();
        for(WiseSaying ws : mydb)
        {
            if(ws.getAuthor().contains(param))
                answer.add(ws);
        }
        return answer;
    }


    public List<WiseSaying> getPage(int pageNum) {
        List<WiseSaying> answer = new ArrayList<>();
        int page = pageNum;
        int end = mydb.size() - (page - 1) * PAGE_MAX_ELEMENT;
        int start = Math.max(end - PAGE_MAX_ELEMENT, 0);
        for (int i = start; i < end; i++) {
            answer.add(mydb.get(i));
        }
        return answer;
    }

    public int getCntPages(){
        return (int)Math.ceil((double)mydb.size()/PAGE_MAX_ELEMENT);
    }




    //save
    public boolean save(String filename, String context)
    {
        boolean answer = false;
        try(FileOutputStream fos = new FileOutputStream(filename);){
            fos.write(context.getBytes());
            answer = true;
        }
        catch(IOException e)
        {
            throw new RuntimeException("repo: ioE", e);
        }
        return answer;
    }

    public boolean save(){
        boolean answer = false;
        WiseSaying ws;
        for(int i=0; i<mydb.size(); i++)
        {
            ws = mydb.get(i);
            save(folderPath + ws.getIdx()+".json",ws.makeJson());
        }
        saveLastIdx();
        return answer;
    }

    private boolean save(WiseSaying ws)
    {
        boolean answer = false;
        answer = save(folderPath + ws.getIdx()+".json", ws.makeJson());
        saveLastIdx();
        return answer;
    }

    private boolean saveLastIdx()
    {
        boolean answer = false;
        answer = save(folderPath + "lastIdx.json",(WiseSaying.getNum()-1)+"");
        return answer;
    }

    public boolean build()
    {
        String fileName = "data.json";
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for(int i=0; i<mydb.size(); i++)
        {
            WiseSaying ws = mydb.get(i);
            sb.append(ws.makeJson());
            if(i != mydb.size()-1)
                sb.append(",");
        }
        sb.append("]");
        return save(folderPath+fileName,sb.toString());
    }

    // Repository에서 파일 목록 읽기
    private List<WiseSaying> loadAll(String folderPath) {
        List<WiseSaying> list = new ArrayList<>();

        //검증 필요
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        try
        {
            for (File file : files) {
                String content = new String(Files.readAllBytes(file.toPath()));
                if(file.getName().contains("lastIdx"))
                {
                    WiseSaying.setNum(Integer.parseInt(content)-1);
                    continue;
                }
                else if(!file.getName().contains("data")) {
                    WiseSaying ws = WiseSaying.parseJson(content);
                    //System.out.println(ws);
                    list.add(ws);
                }
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException("ioE: repo", e);
        }

        int max_num = Math.max(list.getLast().getIdx()+1,WiseSaying.getNum());
        WiseSaying.setNum(max_num);
        return list;
    }
}
