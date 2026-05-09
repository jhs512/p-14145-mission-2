package csh.service;

import csh.CommonUtil;
import csh.entity.WiseSaying;
import csh.repository.WiseSayingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WiseSayingService {
    private final WiseSayingRepository repo = WiseSayingRepository.getInstance();
    private final CommonUtil commonUtil = new CommonUtil();
    private int id = commonUtil.readLastId() + 1;

    public Integer create(String content, String author) {
        WiseSaying ws = new WiseSaying.Builder()
                .id(id++)
                .content(content)
                .author(author)
                .build();
        repo.save(ws);
        String fileName = "Id_%s.json".formatted(ws.getId());
        commonUtil.createWiseSayingFile(fileName, ws.toJsonString(),ws.getId());
        return ws.getId();
    }

    public List<WiseSaying> getList() {
        return new ArrayList<>(repo.findAll().values());
    }

    public boolean deleteById(int id) {
        return repo.delete(id) != null;
    }

    public WiseSaying findById(int id) {
        Map<Integer,WiseSaying> map = repo.findAll();
        return map.get(id);
    }

    public void update(WiseSaying ws, String content, String author) {
        ws.setContent(content);
        ws.setAuthor(author);
        repo.save(ws);
        String fileName = "Id_%s.json".formatted(ws.getId());
        commonUtil.createFile(fileName, ws.toJsonString());
    }

    public void buildJsonArray() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        List<WiseSaying> list = getList();
        for (WiseSaying ws : list) {
            sb.append(ws.toJsonString());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        commonUtil.createFile("data.json", sb.toString());
    }

    public void setId(int id) {
        this.id = id;
    }
}
