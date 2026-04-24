package com.back;

import java.util.*;

public class Cmd {

    private String cmd;
    private Map<String, String> args;


    public Cmd(String cmd) {
        String[] cmds = cmd.split("\\?");
        this.cmd = cmds[0];

        if (cmds.length == 1) return;

        this.args = new HashMap<>();
        String[] args = cmds[1].split("&");

        for (String arg : args) {
            String[] kv = arg.split("=");
            this.args.put(kv[0], kv[1]);
        }

    }

    public String getCmd() {
        return cmd;
    }

    public String getArg(String key) {
        if (args == null) return null;
        return args.get(key);
    }

}
