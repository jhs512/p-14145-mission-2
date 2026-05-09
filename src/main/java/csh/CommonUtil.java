package csh;

import csh.entity.WiseSaying;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CommonUtil {
    public static final String DIR_PATH = "db/wiseSaying";

    public void createDir() {
        File dir = new File(DIR_PATH);
        dir.mkdirs();
    }

    public boolean isExistDir() {
        File dir = new File(DIR_PATH);
        return dir.exists();
    }

    public void createFile(String fileName, String content) {
        try {
            File file = new File(DIR_PATH + "/" + fileName);
            OutputStream out = new FileOutputStream(file);
            out.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException fe) {
            System.out.println(fe.getMessage());
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }
    }

    private void writeLastIdFile(Integer id) {
        createFile("lastId.txt", String.valueOf(id));
    }

    public void createWiseSayingFile(String fileName, String content, int id) {
        createFile(fileName, content);
        writeLastIdFile(id);
    }

    public int readLastId() {
        File file = new File(DIR_PATH + "/lastId.txt");
        int lastId = 0;
        if (!file.exists()) {
            return lastId;
        }
        try {
            InputStream is = new FileInputStream(file);
            byte[] x = is.readAllBytes();
            lastId = Integer.parseInt(new String(x, StandardCharsets.UTF_8));
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }
        return lastId;
    }

    public WiseSaying readFile(int id) {
        File file = new File(DIR_PATH + "/Id_%d.json".formatted(id));
        if (!file.exists()) {
            return null;
        }
        String jsonString = "";
        try {
            InputStream is = new FileInputStream(file);
            byte[] x = is.readAllBytes();
            jsonString = new String(x, StandardCharsets.UTF_8);
        } catch (IOException ie) {
            System.out.println(ie.getMessage());
        }
        jsonString = jsonString.replace("{","").replace("}","").replace("\"","");
        Map<String,String> map = new HashMap<>();
        String[] params = jsonString.split(",");
        for(String param : params) {
            String[] bits = param.split(":");
            map.put(bits[0], bits[1]);
        }
        WiseSaying ws = new WiseSaying
                .Builder()
                .id(Integer.parseInt(map.get("id")))
                .content(map.get("content"))
                .author(map.get("author"))
                .build();
        return ws;
    }

}
