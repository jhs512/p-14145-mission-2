package org.example;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
//데이터의 조회/수정/삭제/생성을 담당

public class WiseSayingRepository {
    ArrayList<WiseSaying> wsal = new ArrayList<>();
    WiseSayingController wc1 = new WiseSayingController();
    SystemController sc1 = new SystemController();

    public void generateWiseSaying(int count, String s1, String s2) { //데이터 생성
        WiseSaying ws1 = new WiseSaying();

        ws1.number = count;
        ws1.wise = s1;
        ws1.writer = s2;

        wsal.add(ws1);
        wc1.generatePrint(ws1.number);

        try{
            // 만들 파일 이름 및 경로 지정
            File file1 = new File("src/main/resources/db/wiseSaying/" + ws1.number + ".json");
            File file2 = new File("src/main/resources/db/wiseSaying/lastId.txt");

            file1.getParentFile().mkdirs(); //클래스 저장
            file2.getParentFile().mkdirs(); //마지막 id 저장

            FileWriter fw1 = new FileWriter(file1);
            BufferedWriter writer = new BufferedWriter(fw1);
            writer.write(ws1.printString());

            FileWriter fw2 = new FileWriter(file2);
            BufferedWriter writer2 = new BufferedWriter(fw2);
            writer2.write(String.valueOf(ws1.number));

            writer.close();
            writer2.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteWiseSaying(int md4) { //데이터 삭제

        Path reader = null;
        try {
            reader = Paths.get("src/main/resources/db/wiseSaying/" + md4 + ".json");

            if ( Files.deleteIfExists(reader) == false) {
                wc1.deleteWiseSaying(md4,1);
            } else {
                //wsal.remove(md4-1);
                Files.deleteIfExists(reader);
                wc1.deleteWiseSaying(md4,2);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewWiseSaying(int input,String key, int page) { //데이터 조회 페이지 번호 있음
       if(input ==1){
           wc1.viewPrint(wsal,"content",key,page);
       }else{
           wc1.viewPrint(wsal,"author",key, page);
       }
    }

    public int loadLastId() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/db/wiseSaying/lastId.txt"));
            int id = Integer.parseInt(br.readLine());
            br.close();
            return id;
        } catch (Exception e) {
            return 0;
        }
    }

    private ArrayList<WiseSaying> loadAllFromFiles() throws IOException {
        ArrayList<WiseSaying> s1 = new ArrayList<>();
        File dir = new File("src/main/resources/db/wiseSaying/");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json") && !name.equals("data.json"));
        if (files != null) {
            Arrays.sort(files, Comparator.comparingInt(f -> Integer.parseInt(f.getName().replace(".json", ""))));
            for (File file : files) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                s1.add(WiseSaying.fromJson(sb.toString()));
                br.close();
            }
        }
        return s1;
    }

    public void viewWiseSaying2(int page) {
        try {
            wc1.viewPrint2(loadAllFromFiles(), page);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyWiseSaying(int md4) { //데이터 수정

        WiseSaying ws = wsal.get(md4 - 1);

        if (md4 < 1 || md4 > wsal.size()) {
            wc1.deleteWiseSaying(md4,1);
        } else {

            wc1.scanWiseSaying1(ws.wise);
            String str1 = sc1.scanS();
            ws.wise = str1;

            wc1.scanWiseSaying2(ws.writer);
            String str2 = sc1.scanH();
            ws.writer = str2;
        }

        try{
            // 만들 파일 이름 및 경로 지정
            File file1 = new File("src/main/resources/db/wiseSaying/" + ws.number + ".json");

            file1.createNewFile(); //클래스 저장

            FileWriter fw1 = new FileWriter(file1);
            BufferedWriter writer = new BufferedWriter(fw1);
            writer.write(ws.printString());

            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void buildWiseSaying() {
        try {
            wc1.build(loadAllFromFiles());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
