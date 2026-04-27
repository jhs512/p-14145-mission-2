package org.example;
import java.util.ArrayList;

//명언 객체(번호/명언내용/작가)

public class WiseSaying {
    int number;
    String wise;
    String writer;

    public String printString() {
        return String.format("{\n\"id\":%d,\n\"content\":\"%s\",\n\"author\":\"%s\"\n}", number, wise, writer);
    }

    public static WiseSaying fromJson(String json) {
        WiseSaying ws = new WiseSaying();
        ws.number = Integer.parseInt(json.replaceAll(".*\"id\":(\\d+).*", "$1"));
        ws.wise = json.replaceAll(".*\"content\":\"([^\"]+)\".*", "$1");
        ws.writer = json.replaceAll(".*\"author\":\"([^\"]+)\".*", "$1");
        return ws;
    }
}
