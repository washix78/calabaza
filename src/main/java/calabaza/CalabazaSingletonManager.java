package calabaza;

import java.util.List;

public class CalabazaSingletonManager {

    private static CalabazaSingletonManager instance;

    public static CalabazaSingletonManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("初期化していません。");
        } else {
            return instance;
        }
    }

    public static CalabazaSingletonManager getInstance(List<String> tagList, List<String> imageList, List<List<Integer>> imageIndexListList) {
        if (instance == null) {
            instance = new CalabazaSingletonManager(tagList, imageList, imageIndexListList);
        }
        return instance;
    }

    private CalabazaLogic logic = null;

    private CalabazaSingletonManager(List<String> tagList, List<String> imageList, List<List<Integer>> imageIndexListList) {
        logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
    }

    public CalabazaLogic getLogic() {
        return logic;
    }

}
