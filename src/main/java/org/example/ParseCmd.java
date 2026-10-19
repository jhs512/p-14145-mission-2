package org.example;

import java.util.HashMap;
import java.util.Map;

public class ParseCmd {
    String rawCmd;
    String prefix;
    String suffix;
    Map<String,String> param = new HashMap<>();

    public ParseCmd(String cmd) {
        this.rawCmd = cmd;

        String[] cmdParts = cmd.split("\\?");
        prefix = cmdParts[0].trim();
        suffix = cmdParts.length > 1 ? cmdParts[1].trim() : "";

        String[] query = suffix.split("&");
        for (String q : query) {
            String [] parts = q.split("=");
            if (parts.length > 1) {
                param.put(parts[0], parts[1]);
            }
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getRawCmd() {
        return rawCmd;
    }

    public Command getCommand() {
        switch (prefix) {
            case "종료":
                return Command.EXIT;
            case "등록":
                return Command.REGISTER;
            case "목록":
                return Command.LIST;
            case "삭제":
                return Command.DELETE;
            case "수정":
                return Command.UPDATE;
            default:
                return Command.ERROR;
        }
    }

    /**
     * 없는 키값일 경우 빈 문자열을 반환
     * @param {string} key
     * @return {string}
     */
    public String getParam(String key) {
        String value = "";
        try {
            value = param.getOrDefault(key, "");
        } catch (Exception e) {
            System.out.println(e);
        }
        return  value;
    }
}