package org.example;

import java.io.*;
import java.util.*;

public class Main {
    public static final String basePath = System.getProperty("user.dir") + "/db/wiseSaying/";

    static void writeFileForm(String filename, WiseSaying form) throws IOException {
        File folder = new File(basePath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(basePath + filename);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(basePath + filename);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(form.toJson());

        fw.close();
        pw.close();
    }

    public static int writeLastId(int id) throws IOException {
        if (id < 1) {
            System.out.println("잘못된 아이디가 입력되었습니다.");
            return -1;
        }

        File folder = new File(basePath);
        File file = new File(basePath + "lastId.txt");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(id);

        fw.close();
        pw.close();

        return id;
    }

    public static void delete(String filename) {
        File file = new File(basePath + filename);
        if (!file.exists()) return;
        file.delete();
    }


    private static WiseSaying readFileForm(File file) throws IOException{
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        // TODO: 언제 한번 text to json 하는거 제대로 만들어봐도 좋을듯?
        String line = "";
        WiseSaying form = new WiseSaying(-1);

        while ((line = br.readLine()) != null) {
            String str = line.trim();
            if (str.startsWith("\"")) {
                if (str.endsWith(",")) str = str.substring(0, str.length() - 1);

                String[] keyAndValue = str.split(":");
                if (keyAndValue.length != 2) {
                    System.out.printf("json파싱중에 예상치 못한 상황이 발생했다! line: %s%n", str);
                    continue;
                }
                String key = keyAndValue[0].replace("\"",  "").trim();
                String value = keyAndValue[1].replace("\"",  "").trim();

                switch (key) {
                    case "id" :
                        // TODO: 항상 parseInt 가 걱정되네 safe버전을 만들어놔야하는데
                        form.setId(Integer.parseInt(value));
                        break;
                    case "content":
                        form.setText(value);
                        break;
                    case "author":
                        form.setAuth(value);
                        break;
                    default: break;
                }
            }
        }


        fr.close();
        br.close();

        return form;
    }

    public static Map<Integer, WiseSaying> getSavedForms() throws IOException{
        File folder = new File(basePath);
        File[] files = folder.listFiles();
        Map<Integer, WiseSaying> map = new HashMap<>();

        if (files != null) {
            for (File file : files) {
                if (!file.isFile() || !file.getName().endsWith(".json")) {
                    continue;
                }
                if (file.getName().startsWith("data")) {
                    continue;
                }

                WiseSaying form = readFileForm(file);
                map.put(form.getId(), form);
            }// for
        }// files not null

        return map;
    }

    public static int getLastId() throws IOException{
        File file = new File(basePath + "lastId.txt");
        if (!file.exists()) {
            return 0;
        }

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine().trim();

        // TODO: id int 검사
        int lastId = Integer.parseInt(line);

        fr.close();
        br.close();

        return lastId;
    }

    static void main() {
        System.out.println("== 명언 앱 ==");

        Scanner sc = new Scanner(System.in);

        Map<Integer, WiseSaying> forms = new HashMap<>();
        int lastId = 0;

        try {
            forms = getSavedForms();
            lastId = getLastId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(true) {
            System.out.print("명령) ");
//            String cmd = sc.nextLine().trim();
            ParseCmd cmd =  new ParseCmd(sc.nextLine().trim());

            if (cmd.getCommand() == Command.EXIT) {
                break;
            } else if (cmd.getCommand() == Command.REGISTER) {
                System.out.print("명언 : ");
                String content = sc.nextLine();

                System.out.print("작가 : ");
                String auth = sc.nextLine();
                int id = lastId + 1;
                WiseSaying newForm = new WiseSaying(id);
                newForm.setText(content);
                newForm.setAuth(auth);

                try {
                    writeFileForm(id + ".json", newForm);
                    writeLastId(id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                forms.put(newForm.id, newForm);
                lastId = id;

                System.out.println(id + "번 명언이 등록되었습니다.");
            } else if (cmd.getCommand() == Command.LIST) {
                System.out.println("""
                        번호 / 작가 / 명언
                        ----------------------""");
                for (int id = lastId; id > 0; id--) {
                    WiseSaying form = forms.get(id);
                    if (form == null) continue;

                    String printLine = form.toString();
                    System.out.println(printLine);
                }
            } else if (cmd.getCommand() == Command.DELETE) {
                int id = Integer.parseInt(cmd.getParam("id"));
                if (!forms.containsKey(id)) {
                    System.out.println(id + "번 명언은 존재하지 않습니다.");
                    continue;
                }

                delete(id + ".json");
                forms.remove(id);
                System.out.println(id + "번 명언이 삭제되었습니다.");

            } else if (cmd.getCommand() == Command.UPDATE) {
                int id = Integer.parseInt(cmd.getParam("id"));
                if (!forms.containsKey(id)) {
                    System.out.println(id + "번 명언은 존재하지 않습니다.");
                    continue;
                }

                WiseSaying form = forms.get(id);
                WiseSaying newForm = new WiseSaying(form.id);

                System.out.println("명언(기존) : " + form.getText());
                System.out.print("명언 : ");
                newForm.setText(sc.nextLine().trim());

                System.out.println("작가(기존) : " + form.getAuth());
                System.out.print("작가 : ");
                newForm.setAuth(sc.nextLine().trim());

                try {
                    writeFileForm(id + ".json", newForm);
                    forms.remove(id);
                    forms.put(id, newForm);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }//while

    }
}
