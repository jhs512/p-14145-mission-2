package csh.repository;

import csh.entity.WiseSaying;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WiseSayingRepository {
    private static WiseSayingRepository instance;
    private WiseSayingRepository() {}
    public static WiseSayingRepository getInstance() {
        if(instance == null) {
            instance = new WiseSayingRepository();
        }
        return instance;
    }

    private static final Map<Integer, WiseSaying> map = new TreeMap<>(Comparator.reverseOrder());

    public void save(WiseSaying wiseSaying) {
        map.put(wiseSaying.getId(), wiseSaying);
    }

    public Map<Integer, WiseSaying> findAll() {
        return map;
    }

    public WiseSaying delete(int id) {
        return map.remove(id);
    }
}
