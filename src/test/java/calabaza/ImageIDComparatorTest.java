package calabaza;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class ImageIDComparatorTest {

    @Test
    public void test00() {
        List<String> list = Arrays.asList("image-1.jpg", "image-10.png", "image-20.jpg", "image-111.png", "image-5.jpg", "image-110.png",
                "image-21.jpg", "image-100.png", "image-11.jpg", "image-4.png", "image-3.jpg", "image-2.png");
        Collections.sort(list, new ImageIDComparator());

        String[] expect = { "image-1.jpg", "image-2.png", "image-3.jpg", "image-4.png", "image-5.jpg",
                "image-10.png", "image-11.jpg", "image-20.jpg", "image-21.jpg",
                "image-100.png", "image-110.png", "image-111.png" };
        assertArrayEquals(expect, list.toArray(new String[0]));
    }

}
