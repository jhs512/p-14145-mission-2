package csh;

import java.util.HashMap;
import java.util.Map;

public class Rq {
    Map<String,String> map = new HashMap<>();

    public Rq(String command) {
        String[] commandBits = command.split("\\?");
        String actionName = commandBits.length > 0 ? commandBits[0].toLowerCase() : "";
        String params = commandBits.length > 1 ? commandBits[1] : "";
        map.put("actionName", actionName);
        if(params.isEmpty()) return;
        String[] paramBits = params.split("&");
        for(String param : paramBits) {
            String[] bits = param.split("=");
            String val = bits.length > 1 ? bits[1] : "";
            if(val.isEmpty()) continue;
            String key = bits[0].toLowerCase();
            map.put(key,val);
        }
    }

    public String getActionName() {
        return map.get("actionName");
    }

    public String getValue(String key, String defaultValue) {
        return map.getOrDefault(key,defaultValue);
    }

    public Integer getIntValue(String key, int defaultValue) {
        return Integer.parseInt(getValue(key, String.valueOf(defaultValue)));
    }
}
