package csh;

import csh.entity.WiseSaying;
import csh.repository.WiseSayingRepository;


public class InitApp {

    public static void init() {
        WiseSayingRepository repo = WiseSayingRepository.getInstance();
        CommonUtil commonUtil = new CommonUtil();
        if (commonUtil.isExistDir()) {
            commonUtil.createDir();
        }

        int id = commonUtil.readLastId();
        for(int i=1; i<=id; i++) {
            WiseSaying ws = commonUtil.readFile(i);
            if(ws == null) continue;
            repo.save(ws);
        }
    }
}
