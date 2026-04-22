package com.back.domain.wiseSaying.entity;

import java.util.Objects;

//com.back.domain.wiseSaying.entity.WiseSaying.java
//역할 : 명언 객체(번호/명언내용/작가)
//이 클래스는 컨트롤러, 서비스, 리포지터리 모두에서 사용가능
public class WiseSaying {
    private int idx;
    private String content;
    private String  author;
    private static int num = 1;

    public WiseSaying(int idx, String content, String author)
    {
        this.idx = idx;
        this.content = content;
        this.author = author;
    }

    public WiseSaying(int idx, String content)
    {
        this.idx = idx;
        this.content = content;
        this.author = "작자미상";
    }
    public WiseSaying(String content, String author)
    {
        this.idx = num++;
        this.content = content;
        this.author = author;
    }
    public WiseSaying(String content)
    {
        this.idx = num++;
        this.content = content;
        this.author = "작자미상";
    }
    public WiseSaying()
    {
        this.idx = num;
        this.content = "명언 "+num;
        this.author = "작자미상 "+num;
        num++;
    }
    public static WiseSaying parseJson(String json)
    {
        WiseSaying ws;
        json = json.replaceAll("[{}\"]", ""); // { } " 제거
        String[] pairs = json.split(",");

        int id = 0;
        String content = "";
        String author = "";

        for (String pair : pairs) {
            String[] kv = pair.split(":");
            String key = kv[0].trim();
            String value = kv[1].trim();

            switch (key) {
                case "id" -> id = Integer.parseInt(value);
                case "content" -> content = value;
                case "author" -> author = value;
            }
        }
        ws = new WiseSaying(id,content,author);
        return ws;
    }

    public static int getNum(){return num;}

    public static void setNum(int i) {
        WiseSaying.num = i;
    }

    public int getIdx() {
        return idx;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String makeJson(){

        return "{id:"+idx+","
            +"content:"+content+","
            +"author:"+author+"}";
    }
    @Override
    public String toString() {
        return idx +
                " / " + author +
                " / " + content + "\n";
    }

    @Override
    public boolean equals(Object o)
    {
        if(this==o)return true;
        if(o == null || getClass() != o.getClass()) return false;
        WiseSaying dummy = (WiseSaying) o;
        return idx == ((WiseSaying) o).getIdx();
    }
    @Override
    public int hashCode() {
        return Objects.hash(idx); // equals에서 쓴 필드를 그대로 사용
    }
}
