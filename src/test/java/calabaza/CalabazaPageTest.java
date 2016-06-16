package calabaza;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import calabaza.CalabazaPage;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CalabazaPageTest {

    private static List<String> tagList;
    private static List<String> imageList;
    private static List<List<Integer>> indexListList;

    @BeforeClass
    public static void beforeClass() {
        tagList = Arrays.asList("赤", "青", "緑", "白", "黒");
        imageList = Arrays.asList("image-1.jpg", "image-2.png", "image-3.jpg", "image-4.png", "image-5.jpg",
                "image-6.png", "image-7.jpg", "image-8.png", "image-9.jpg", "image-10.png",
                "image-11.jpg", "image-12.png", "image-13.jpg", "image-14.png", "image-15.jpg",
                "image-16.png", "image-17.jpg", "image-18.png", "image-19.jpg", "image-20.png",
                "image-21.jpg", "image-22.png");
        indexListList = Arrays.asList(
                Arrays.asList(0, 2, 4, 6, 8),
                Arrays.asList(1, 3, 5, 7, 9),
                Arrays.asList(0, 10, 20),
                Arrays.asList(1, 11, 21),
                new ArrayList<Integer>());

        CalabazaSingletonManager.getInstance(tagList, imageList, indexListList);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test00CalabazaPage() throws NoSuchFieldException, IllegalAccessException {
        // displayCount;
        // pageCount;
        // currentNo;
        // displayTagList;
        // displayImageList;
        // imageCount;

        /*
         * irregular
         */
        CalabazaPage page1 = new CalabazaPage(0);
        assertEquals(20, getField(page1, "displayCount"));
        assertEquals(2, getField(page1, "pageCount"));
        assertEquals(1, getField(page1, "currentNo"));
        assertNull(getField(page1, "displayTagList"));
        assertArrayEquals(imageList.toArray(new String[0]),
                ((List<String>) getField(page1, "displayImageList")).toArray(new String[0]));
        assertEquals(22, getField(page1, "imageCount"));

        /*
         * regular
         */
        CalabazaPage page2 = new CalabazaPage();
        assertEquals(20, getField(page2, "displayCount"));
        assertEquals(2, getField(page2, "pageCount"));
        assertEquals(1, getField(page2, "currentNo"));
        assertNull(getField(page2, "displayTagList"));
        assertArrayEquals(imageList.toArray(new String[0]),
                ((List<String>) getField(page2, "displayImageList")).toArray(new String[0]));
        assertEquals(22, getField(page2, "imageCount"));

        CalabazaPage page3 = new CalabazaPage(10);
        assertEquals(10, getField(page3, "displayCount"));
        assertEquals(3, getField(page3, "pageCount"));
        assertEquals(1, getField(page3, "currentNo"));
        assertNull(getField(page3, "displayTagList"));
        assertArrayEquals(imageList.toArray(new String[0]),
                ((List<String>) getField(page3, "displayImageList")).toArray(new String[0]));
        assertEquals(22, getField(page3, "imageCount"));
    }

    @Test
    public void test01GetDisplayCount() {
        CalabazaPage page1 = new CalabazaPage();
        assertEquals(20, page1.getDisplayCount());

        CalabazaPage page2 = new CalabazaPage(10);
        assertEquals(10, page2.getDisplayCount());
    }

    @Test
    public void test02GetImageCount() {
        CalabazaPage page1 = new CalabazaPage();
        assertEquals(22, page1.getImageCount());

        CalabazaPage page2 = new CalabazaPage(10);
        assertEquals(22, page2.getImageCount());
    }

    @Test
    public void test03GetCurrentNo() {
        CalabazaPage page1 = new CalabazaPage();
        assertEquals(1, page1.getCurrentNo());
        // over
        page1.changePageNo(5);
        assertEquals(2, page1.getCurrentNo());
        // under
        page1.changePageNo(0);
        assertEquals(1, page1.getCurrentNo());
        // regular
        page1.changePageNo(2);
        assertEquals(2, page1.getCurrentNo());
        // change page condition
        page1.changeCondition(30, tagList);
        assertEquals(1, page1.getCurrentNo());

        CalabazaPage page2 = new CalabazaPage(10);
        assertEquals(1, page2.getCurrentNo());
        // over
        page2.changePageNo(5);
        assertEquals(3, page2.getCurrentNo());
        // under
        page2.changePageNo(0);
        assertEquals(1, page2.getCurrentNo());
        // regular
        page2.changePageNo(2);
        assertEquals(2, page2.getCurrentNo());
        // change page condition
        page2.changeCondition(30, tagList);
        assertEquals(1, page2.getCurrentNo());
    }

    @Test
    public void test04GetPageCount() {
        CalabazaPage page1 = new CalabazaPage();
        assertEquals(2, page1.getPageCount());
        // change page condition
        page1.changeCondition(30, null);
        assertEquals(1, page1.getPageCount());

        CalabazaPage page2 = new CalabazaPage(10);
        assertEquals(3, page2.getPageCount());
        // change page condition
        page2.changeCondition(20, null);
        assertEquals(2, page2.getPageCount());
    }

    @Test
    public void test05GetDisplayTagList() {
        CalabazaPage page1 = new CalabazaPage();
        assertNull(page1.getDisplayTagList());

        // change page condition
        List<String> list11 = new ArrayList<>();
        page1.changeCondition(20, list11);
        assertNotSame(page1.getDisplayTagList(), list11);
        assertArrayEquals(new String[] {}, page1.getDisplayTagList().toArray(new String[0]));

        List<String> list12 = Arrays.asList("金", "青", "銀", "赤", null);
        page1.changeCondition(20, list12);
        assertNotSame(page1.getDisplayTagList(), list12);
        assertArrayEquals(new String[] { "青", "赤" }, page1.getDisplayTagList().toArray(new String[0]));

        assertNotSame(page1.getDisplayTagList(), page1.getDisplayTagList());

        CalabazaPage page2 = new CalabazaPage(10);
        assertNull(page2.getDisplayTagList());

        // change page condition
        List<String> list21 = new ArrayList<>();
        page2.changeCondition(10, list21);
        assertNotSame(page2.getDisplayTagList(), list21);
        assertArrayEquals(new String[] {}, page2.getDisplayTagList().toArray(new String[0]));

        List<String> list22 = Arrays.asList("金", "青", "銀", "赤", null);
        page2.changeCondition(10, list22);
        assertNotSame(page2.getDisplayTagList(), page2.getDisplayTagList());
        assertArrayEquals(new String[] { "青", "赤", }, page2.getDisplayTagList().toArray(new String[0]));

        assertNotSame(page2.getDisplayTagList(), page2.getDisplayTagList());
    }

    @Test
    public void test06getDisplayImageList() {

        CalabazaPage page1 = new CalabazaPage();
        assertArrayEquals(imageList.toArray(new String[0]), page1.getDisplayImageList(true).toArray(new String[0]));

        assertArrayEquals(imageList.subList(0, 20).toArray(new String[0]),
                page1.getDisplayImageList(false).toArray(new String[0]));
        // change page no
        page1.changePageNo(2);
        assertArrayEquals(imageList.subList(20, 22).toArray(new String[0]),
                page1.getDisplayImageList(false).toArray(new String[0]));

        CalabazaPage page2 = new CalabazaPage(10);
        assertArrayEquals(imageList.toArray(new String[0]), page2.getDisplayImageList(true).toArray(new String[0]));

        assertArrayEquals(imageList.subList(0, 10).toArray(new String[0]),
                page2.getDisplayImageList(false).toArray(new String[0]));
        // change page no
        page2.changePageNo(2);
        assertArrayEquals(imageList.subList(10, 20).toArray(new String[0]),
                page2.getDisplayImageList(false).toArray(new String[0]));
    }

    private Object getField(Object instance, String name) throws NoSuchFieldException, IllegalAccessException {
        Field f = instance.getClass().getDeclaredField(name);
        f.setAccessible(true);
        return f.get(instance);
    }

}
