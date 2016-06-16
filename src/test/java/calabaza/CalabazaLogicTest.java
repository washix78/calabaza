package calabaza;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CalabazaLogicTest {

    private static List<String> tagList;
    private static List<String> imageList;
    private static List<List<Integer>> imageIndexListList;
    
    @BeforeClass
    public static void beforeClass() {
        Integer[] a = null;
        a = new Integer[] { 1, 2 };
        Arrays.asList(a);
        tagList = Arrays.asList("タグ１", "タグ２", "タグ３", "タグ４", "タグ５");
        imageList = Arrays.asList("image-1.jpg", "image-2.png", "image-3.jpg", "image-4.png", "image-5.jpg");
        imageIndexListList = Arrays.asList(
                Arrays.asList(new Integer[] { 0, 1 }),
                Arrays.asList(new Integer[] { 2, 3 }),
                Arrays.asList(new Integer[] { 4 }),
                Arrays.asList(new Integer[] {}),
                Arrays.asList(new Integer[] { 1, 2, 3 }));
    }
    
    @Test
    public void test00CalabazaLogic() {
        // no data
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
        
        // tag list
        assertNotSame(tagList, logic.tagList);
        assertArrayEquals(tagList.toArray(), logic.tagList.toArray());

        // image list
        assertNotSame(imageList, logic.imageList);
        assertArrayEquals(imageList.toArray(), logic.imageList.toArray());

        // tag index list map
        assertEquals(5, logic.tagIndexListMap.size());
        assertArrayEquals(new Integer[]{0}, logic.tagIndexListMap.get("image-1.jpg").toArray());
        assertArrayEquals(new Integer[]{0,4}, logic.tagIndexListMap.get("image-2.png").toArray());
        assertArrayEquals(new Integer[]{1,4}, logic.tagIndexListMap.get("image-3.jpg").toArray());
        assertArrayEquals(new Integer[]{1,4}, logic.tagIndexListMap.get("image-4.png").toArray());
        assertArrayEquals(new Integer[]{2}, logic.tagIndexListMap.get("image-5.jpg").toArray());
    }

    @Test
    public void test01GetTagList() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);

        assertArrayEquals(new String[]{"タグ１","タグ２","タグ３","タグ４","タグ５"},logic.getTagList().toArray());

        // after tag removing
        logic.removeTag("タグ３");
        assertArrayEquals(new String[]{"タグ１","タグ２", "タグ４","タグ５"},logic.getTagList().toArray());
    }
    
    @Test
    public void test02GetImageList() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
        
        assertArrayEquals(new String[]{"image-1.jpg","image-2.png","image-3.jpg","image-4.png","image-5.jpg"}, logic.getImageList().toArray());
    }
    
    @Test
    public void test03GetImageTagList() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
        
        /*
         * illegal
         */

        // null image
        try {
            logic.getImageTagList(null);
            fail();
        } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }

        // not exist image
        try {
            logic.getImageTagList("image-6.png");
            fail();
        } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }
        
        /*
         * legal
         */
        
        assertArrayEquals(new String[]{"タグ１","タグ５"}, logic.getImageTagList("image-2.png").toArray());
        
        // after tag adding, image tag list updating
        logic.addTag("タグ６");
        logic.updateImageTagList("image-2.png", Arrays.asList("タグ１","タグ５","タグ６"));
        assertArrayEquals(new String[]{"タグ１","タグ５","タグ６"}, logic.getImageTagList("image-2.png").toArray());
        
        // after tag removing
        logic.removeTag("タグ１");
        logic.removeTag("タグ６");
        assertArrayEquals(new String[]{"タグ５"}, logic.getImageTagList("image-2.png").toArray());
    }
    
    @Test
    public void test04GetImageIndexListList() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
        
        List<List<Integer>> listList1 = logic.getImageIndexListList();
        assertArrayEquals(new Integer[]{0,1}, listList1.get(0).toArray());
        assertArrayEquals(new Integer[]{2,3}, listList1.get(1).toArray());
        assertArrayEquals(new Integer[]{4}, listList1.get(2).toArray());
        assertArrayEquals(new Integer[]{}, listList1.get(3).toArray());
        assertArrayEquals(new Integer[]{1,2,3}, listList1.get(4).toArray());
        
        // after tag adding
        logic.addTag("タグ６");
        List<List<Integer>> listList2 = logic.getImageIndexListList();
        assertArrayEquals(new Integer[]{0,1}, listList2.get(0).toArray());
        assertArrayEquals(new Integer[]{2,3}, listList2.get(1).toArray());
        assertArrayEquals(new Integer[]{4}, listList2.get(2).toArray());
        assertArrayEquals(new Integer[]{}, listList2.get(3).toArray());
        assertArrayEquals(new Integer[]{1,2,3}, listList2.get(4).toArray());
        assertArrayEquals(new Integer[]{}, listList2.get(5).toArray());
        
        // after image tag list updating
        logic.updateImageTagList("image-3.jpg", Arrays.asList("タグ６"));
        List<List<Integer>> listList3 = logic.getImageIndexListList();
        assertArrayEquals(new Integer[]{0,1}, listList3.get(0).toArray());
        assertArrayEquals(new Integer[]{3}, listList3.get(1).toArray());
        assertArrayEquals(new Integer[]{4}, listList3.get(2).toArray());
        assertArrayEquals(new Integer[]{}, listList3.get(3).toArray());
        assertArrayEquals(new Integer[]{1,3}, listList3.get(4).toArray());
        assertArrayEquals(new Integer[]{2}, listList3.get(5).toArray());

        // after tag removing
        logic.removeTag("タグ３");
        logic.removeTag("タグ６");
        List<List<Integer>> listList4 = logic.getImageIndexListList();
        assertArrayEquals(new Integer[]{0,1}, listList4.get(0).toArray());
        assertArrayEquals(new Integer[]{3}, listList4.get(1).toArray());
        assertArrayEquals(new Integer[]{}, listList4.get(2).toArray());
        assertArrayEquals(new Integer[]{1,3}, listList4.get(3).toArray());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void test05GetTagDataMap() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);

        Map<String, Object> tagDataMap = logic.getTagDataMap();
        assertEquals(2, tagDataMap.size());

        assertArrayEquals(logic.getTagList().toArray(), ((List<String>) tagDataMap.get("tagList")).toArray());
        assertEquals(logic.getEscapedTagList().size(), ((Map<String, String>)tagDataMap.get("encMap")).size());
    }
    
    @Test
    public void test06GetEncodedTagMap() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
        
        assertEquals(5, logic.getEncodedTagMap().size());
        
        // after tag adding
        logic.addTag("タグ６");
        assertEquals(6, logic.getEncodedTagMap().size());
        
        // after tag removing
        logic.removeTag("タグ１");
        logic.removeTag("タグ２");
        assertEquals(4, logic.getEncodedTagMap().size());
    }
    
    @Test
    public void test07GetImageDataMap() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);

        /*
         * illegal
         */

        // null
        try {
            logic.getImageDataMap(null);
            fail();
        } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }
        
        // contain null
        try {
            logic.getImageDataMap(Arrays.asList("image-1.jpg", null, "image-2.png"));
            fail();
        } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }
        
        // contain not exist
        try {
            logic.getImageDataMap(Arrays.asList("image-1.jpg","image-X.jpg", "image-2.png"));
            fail();
        } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }
    }

    @Test
    public void test08AddTag() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);

        /*
         * illegal
         */

        // null
        try {
            logic.addTag(null);
            fail();
        } catch (Exception e) {}
        
        // 0 length
        try {
            logic.addTag("");
            fail();
        } catch (Exception e) {}

        // spaces
        try {
            logic.addTag(" 　 　");
            fail();
        } catch (Exception e) {}
        
        // exist
        try {
            logic.addTag("タグ１");
            fail();
        } catch (Exception e) {}
        try {
            logic.addTag(" タグ1　");
            fail();
        } catch (Exception e) {}
        
        /*
         * legal
         */
        assertEquals(" タグ６　", logic.addTag("タグ6").get("tag"));
        assertEquals("スペース　コブラ", logic.addTag(" 　スペース 　 　コブラ　").get("tag"));
    }
    
    @Test
    public void test09UpdateTag() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);

        /*
         * illegal
         */

        // null, new tag
        try {
            logic.updateTag(null, "タグ６");
            fail();
        } catch (Exception e) {}
        // not exist, new tag
        try {
            logic.updateTag("タグ１０", "タグ６");
            fail();
        } catch (Exception e) {}
        // exist, null
        try {
            logic.updateTag("タグ１", null);
            fail();
        } catch (Exception e) {}
        // exist, 0 length
        try {
            logic.updateTag("タグ１", "");
            fail();
        } catch (Exception e) {}
        // exist, spaces
        try {
            logic.updateTag("タグ１", " 　 　");
            fail();
        } catch (Exception e) {}
        // exist, exist
        try {
            logic.updateTag("タグ１", " 　 タグ3　");
            fail();
        } catch (Exception e) {}
        
        /*
         * legal
         */
        assertEquals("Ａ－Ｚ", logic.updateTag("タグ１", "A-Z").get("tag"));
    }
    
    @Test
    public void test10RemoveTag() {
        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
        
        // null
        assertFalse(logic.removeTag(null));
        // not exist
        assertFalse(logic.removeTag("NO"));
        // exist
        assertTrue(logic.removeTag("タグ５"));
    }

//    @Test
//    public void test11UpdateImageTagList() {
//        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
//        logic.updateImageTagList(image, newTagList);
//        
//        /*
//         * illegal
//         */
//        // image is null
//        // image not exist
//        // new tag list contain null
//        // new tag list contain not exist
//        // new tag list length over 3
//        // new tag list length unless 1
//    }
//    
//    @Test
//    public void test12GetEscapedTagList() {
//        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
//        logic.removeTag(tag);
//    }
//    
//    @Test
//    public void test13GetJs() {
//        /*
//         * illegal
//         */
//        // not supported
//        // not exist file
//        
//        // check tag list
//        // check image list
//        // check image index list list
//        
//    }
//    
//    @Test
//    public void test14GetHasTagImageList() {
//        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
//        logic.getHasTagImageList(tag);
//        /*
//         * illegal
//         */
//        // null
//        // not exist
//        
//        logic.removeTag(tag);
//    }
//    
//    @Test
//    public void test15GetHasNoTagImageList() {
//        CalabazaLogic logic = new CalabazaLogic(tagList, imageList, imageIndexListList);
//        logic.removeTag(tag);
//        logic.getHasNoTagImageList();
//    }
//    
//    @Test
//    public void test16HasTag() {
//        /*
//         * illegal
//         */
//        // null
//        // not exist
//    }
//    

}
