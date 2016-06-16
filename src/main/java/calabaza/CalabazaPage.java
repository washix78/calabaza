package calabaza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalabazaPage {

    private static final int DEFAULT_DISPLAY_COUNT = 20;

    private int displayCount;
    private int pageCount;
    private int currentNo;
    private List<String> displayTagList;
    private List<String> displayImageList;
    private int imageCount;

    public CalabazaPage() {
        this(DEFAULT_DISPLAY_COUNT);
    }

    public CalabazaPage(int displayCount) {
        CalabazaLogic logic = CalabazaSingletonManager.getInstance().getLogic();
        setDisplayCount(displayCount);
        this.displayImageList = logic.getImageList();
        this.imageCount = this.displayImageList.size();
        this.displayTagList = null;
        this.currentNo = 1;
        setPageCount(this.imageCount, this.displayCount);
    }

    public int getDisplayCount() {
        return displayCount;
    }

    public int getImageCount() {
        return imageCount;
    }

    public int getCurrentNo() {
        return currentNo;
    }

    public int getPageCount() {
        return pageCount;
    }

    public List<String> getDisplayTagList() {
        return displayTagList == null ? null : new ArrayList<String>(displayTagList);
    }

    public List<String> getDisplayImageList(boolean isAll) {
        if (isAll) {
            return new ArrayList<String>(displayImageList);
        } else {
            int startI = displayCount * (currentNo - 1);
            int end = displayCount * currentNo;
            if (end >= imageCount) {
                end = imageCount;
            }
            List<String> subList = displayImageList.subList(startI, end);
            return new ArrayList<String>(subList);
        }
    }

    public void changePageNo(int pageNo) {
        if (pageNo < 1) {
            currentNo = 1;
        } else if (pageNo > pageCount) {
            currentNo = pageCount;
        } else {
            currentNo = pageNo;
        }
    }

    public void changeCondition(int displayCount, List<String> newTagList) {
        CalabazaLogic logic = CalabazaSingletonManager.getInstance().getLogic();

        // tag
        if (newTagList == null) {
            // all
            this.displayTagList = null;
        } else {
            this.displayTagList = new ArrayList<>();
            if (newTagList.contains(null)) {
                this.displayTagList.add(null);
            }
            for (String displayTag : newTagList) {
                if (logic.hasTag(displayTag)) {
                    this.displayTagList.add(displayTag);
                }
            }
        }

        // image
        List<List<String>> listList = new ArrayList<>();
        if (newTagList == null) {
            this.displayImageList = logic.getImageList();
        } else {
            for (String tag : this.displayTagList) {
                if (tag == null) {
                    listList.add(logic.getHasNoTagImageList());
                } else {
                    listList.add(logic.getHasTagImageList(tag));
                }
            }
            this.displayImageList = mergeList(listList);
            Collections.sort(this.displayImageList, new ImageIDComparator());
        }

        this.imageCount = this.displayImageList.size();

        this.currentNo = 1;
        this.displayCount = (displayCount < 1) ? DEFAULT_DISPLAY_COUNT : displayCount;

        int pageCount = this.imageCount / this.displayCount;
        this.pageCount = (this.imageCount % this.displayCount != 0) ? pageCount + 1 : pageCount;
    }

    private List<String> mergeList(List<List<String>> listList) {
        List<String> mergedList = new ArrayList<>();
        for (List<String> list : listList) {
            for (String src : list) {
                if (!mergedList.contains(src)) {
                    mergedList.add(src);
                }
            }
        }
        return mergedList;
    }

    private void setDisplayCount(int displayCount) {
        this.displayCount = (displayCount < 1) ? DEFAULT_DISPLAY_COUNT : displayCount;
    }

    private void setPageCount(int imageCount, int displayCount) {
        int pageCount = imageCount / displayCount;
        this.pageCount = (imageCount % displayCount == 0) ? pageCount : pageCount + 1;
    }

}
