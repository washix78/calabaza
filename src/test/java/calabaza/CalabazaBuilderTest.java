package calabaza;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Test;

public class CalabazaBuilderTest {
    
    private static String imagesDir = "./webapp/mini/image";
    private static String destDir = "./src/test/resources/data";
    
    @AfterClass
    public static void afterClass() throws IOException {
        deleteFiles(Paths.get(destDir));
    }

    @Test
    public void test00Exec() {
        try {
            // no exist directory
            CalabazaBuilder builder = new CalabazaBuilder(imagesDir, destDir);
            builder.exec();

            String tagListJsonStr = getTextFromFile(Paths.get(destDir, "tagls.json"));
            String imageListJsonStr = getTextFromFile(Paths.get(destDir, "imagels.json"));
            String imageListListJsonStr = getTextFromFile(Paths.get(destDir, "imageilsls.json"));

            assertEquals("[]", tagListJsonStr);
            assertTrue(imageListJsonStr.startsWith("[\"image-"));
            assertEquals("[]", imageListListJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /*
     * utils
     */
    private String getTextFromFile(Path filePath) throws IOException {
        try (
            BufferedReader in = Files.newBufferedReader(filePath, Charset.forName("UTF-8"));
        ) {
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
    
    private static void deleteFiles(Path rootPath) throws IOException {
        if (Files.isDirectory(rootPath)) {
            try (
                DirectoryStream<Path> ds = Files.newDirectoryStream(rootPath);
            ) {
                for (Path path : ds) {
                    deleteFiles(path);
                }
                Files.deleteIfExists(rootPath);
            }
        } else {
            Files.delete(rootPath);
        }
    }

}
