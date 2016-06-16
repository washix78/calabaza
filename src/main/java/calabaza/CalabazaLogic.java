package calabaza;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import com.ibm.icu.text.Transliterator;

public class CalabazaLogic {

    final int MIN_TAG_COUNT = 1;
    final int MAX_TAG_COUNT = 3;
    final String JS_ROOT_OBJECT = "Mini";

    List<String> tagList;
    List<String> imageList;
    Map<String, List<Integer>> tagIndexListMap;

    public CalabazaLogic(List<String> tagList, List<String> imageList, List<List<Integer>> imageIndexListList) {
        this.tagList = new ArrayList<>(tagList);
        this.imageList = new ArrayList<>(imageList);

        this.tagIndexListMap = new HashMap<>();
        for (String image : imageList) {
            this.tagIndexListMap.put(image, new ArrayList<Integer>());
        }
        for (int tagI = 0, tagCount = tagList.size(); tagI != tagCount; tagI++) {
            List<Integer> imageIndexList = imageIndexListList.get(tagI);
            for (int imageI : imageIndexList) {
                String image = imageList.get(imageI);
                this.tagIndexListMap.get(image).add(tagI);
            }
        }
    }
    
    public List<String> getTagList() {
        List<String> list = new ArrayList<>();
        for (String tag : tagList) {
            if (tag != null) {
                list.add(tag);
            }
        }
        return list;
    }
    
    public List<String> getImageList() {
        return new ArrayList<>(imageList);
    }
    
    public List<String> getImageTagList(String image) {
        if (image == null || !imageList.contains(image)) {
            throw new RuntimeException("NULLか存在しない画像です。");
        }
        List<String> imageTagList = new ArrayList<>();
        for (int tagI : tagIndexListMap.get(image)) {
            String tag = tagList.get(tagI);
            if (tag != null) {
                imageTagList.add(tag);
            }
        }
        return imageTagList;
    }
    
    // [ [ imageI, imageI ], [ imageI, imageI ] ]
    public List<List<Integer>> getImageIndexListList() {
        List<String> tagList = getTagList();

        List<List<Integer>> imageIndexListList = new ArrayList<>();
        for (int i = 0, tagCount = tagList.size(); i != tagCount; i++) {
            imageIndexListList.add(new ArrayList<Integer>());
        }
        
        for (String image : imageList) {
            int imageI = imageList.indexOf(image);
            List<String> imageTagList = getImageTagList(image);
            for (String imageTag : imageTagList) {
                int tagI = tagList.indexOf(imageTag);
                imageIndexListList.get(tagI).add(imageI);
            }
        }
        return imageIndexListList;
    }
    
    // { tagList : [ "tag", "tag" ], encMap : { tag : "enc", tag : "enc" } }
    public Map<String, Object> getTagDataMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("tagList", getTagList());
        map.put("encMap", getEncodedTagMap());
        return map;
    }
    
    public Map<String, String> getEncodedTagMap() {
        Map<String, String> map = new HashMap<>();
        for (String tag : getTagList()) {
            map.put(tag, StringEscapeUtils.escapeHtml4(tag));
        }
        return map;
    }
    
    // { image : [ "tag", "tag" ], image : [ "tag", "tag" ] }
    public Map<String, List<String>> getImageDataMap(List<String> imageList) {
        Map<String, List<String>> map = new HashMap<>();
        for (String image : imageList) {
            map.put(image, getImageTagList(image));
        }
        return map;
    }
    
    // { tag : "tag", enc : "enc" }
    public Map<String, String> addTag(String newTag) {
        String tag = trimTag(newTag);
        if (tagList.contains(tag)) {
            throw new RuntimeException("存在するタグです。");
        }
        tagList.add(tag);
        
        Map<String, String> map = new HashMap<>();
        map.put("tag", tag);
        map.put("enc", StringEscapeUtils.escapeHtml4(tag));
        return map;
    }

    // { tag : "tag", enc : "enc" }
    public Map<String, String> updateTag(String oldTag, String newTag) {
        if (oldTag == null || !tagList.contains(oldTag)) {
            throw new RuntimeException("存在しないタグです。");
        }
        String tag = trimTag(newTag);
        if (tagList.contains(tag)) {
            throw new RuntimeException("存在するタグです。");
        }
        
        int tagI = tagList.indexOf(oldTag);
        tagList.set(tagI, tag);
        
        Map<String, String> map = new HashMap<>();
        map.put("tag", tag);
        map.put("enc", StringEscapeUtils.escapeHtml4(tag));
        return map;
    }
    
    public boolean removeTag(String tag) {
        if (tag == null) {
            return false;
        }
        int tagI = tagList.indexOf(tag);
        if (tagI == -1) {
            return false;
        }
        tagList.set(tagI, null);
        return true;
    }
    
    // { image : "image", tagList : [ "tag", "tag" ] }
    public Map<String, Object> updateImageTagList(String image, List<String> newTagList) {
        if (!imageList.contains(image)) {
            throw new RuntimeException();
        }
        if (newTagList.size() < MIN_TAG_COUNT || MAX_TAG_COUNT < newTagList.size()) {
            throw new RuntimeException("タグの数が不正です。");
        }
        for (String tag : newTagList) {
            if (tag == null || !tagList.contains(tag)) {
                throw new RuntimeException("存在しないタグです。");
            }
        }
        
        List<Integer> list = new ArrayList<>();
        for (String tag : newTagList) {
            list.add(tagList.indexOf(tag));
        }
        tagIndexListMap.put(image, list);
        
        Map<String, Object> map = new HashMap<>();
        map.put("image", image);
        map.put("tagList", getImageTagList(image));
        return map;
    }
    
    public List<String> getEscapedTagList() {
        List<String> escapedTagList = new ArrayList<>();
        for (String tag : getTagList()) {
            escapedTagList.add(StringEscapeUtils.escapeHtml4(tag));
        }
        return escapedTagList;
    }
    
    public String getJs(String jsName) throws IOException {
        String js = null;
        switch (jsName) {
        case "tagls.js":
            List<String> tagList = getEscapedTagList();
            js = String.format("%s.%s = %s;", JS_ROOT_OBJECT, "tagList", CalabazaUtils.convertToJson(tagList));
            break;
        case "imagels.js":
            List<String> imageList = getImageList();
            js = String.format("%s.%s = %s;", JS_ROOT_OBJECT, "imageList", CalabazaUtils.convertToJson(imageList));
            break;
        case "imageilsls.js":
            List<List<Integer>> imageIndexListList = getImageIndexListList();
            js = String.format("%s.%s = %s;", JS_ROOT_OBJECT, "imageIndexListList", CalabazaUtils.convertToJson(imageIndexListList));
            break;
        default:
            js = CalabazaUtils.getTextFromFile(Paths.get(jsName));
            break;
        }
        return js;
    }
    
    public List<String> getHasTagImageList(String tag) {
        if (tag == null || !tagList.contains(tag)) {
            throw new RuntimeException("NULLか存在しないタグです。");
        }

        List<String> imageList = new ArrayList<>();
        for (String image : this.imageList) {
            List<String> imageTagList = getImageTagList(image);
            if (imageTagList.contains(tag)) {
                imageList.add(image);
            }
        }
        return imageList;
    }
    
    public List<String> getHasNoTagImageList() {
        List<String> imageList = new ArrayList<>();
        for (String image : this.imageList) {
            List<String> imageTagList = getImageTagList(image);
            if (imageTagList.size() == 0) {
                imageList.add(image);
            }
        }
        return imageList;
    }
    
    public boolean hasTag(String tag) {
        if (tag == null) {
            return false;
        } else {
            return tagList.contains(tag);
        }
    }
    
    private String trimTag(String tag) {
        if (tag == null) {
            throw new RuntimeException("タグがNULLです。");
        }
        tag = tag.replaceAll("(\\s|　)+", " ").trim();
        tag =  Transliterator.getInstance("Halfwidth-Fullwidth").transliterate(tag);
        if (!tag.matches(".+")) {
            throw new RuntimeException("タグの文字長が0です。");
        }
        return tag;
    }
    
}
