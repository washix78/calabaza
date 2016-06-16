package calabaza;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;

public class CalabazaBuilder {

    private final String TAG_LIST_JSON_FILE = "tagls.json";
    private final String IMAGE_LIST_JSON_FILE = "imagels.json";
    private final String IMAGE_INDEX_LIST_LIST_JSON_FILE = "imageilsls.json";

    private final String IMAGE_FILE_NAME_PATTERN = "^image\\-([0-9]+\\.)+(bmp|jpg|png)$";
    
    private String imagesDir;
    private String destDir;
    
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("set images-dir, dest dir");
        }
        
        String imagesDir = args[0];
        String destDir = args[1];
        new CalabazaBuilder(imagesDir, destDir).exec();
    }

    public CalabazaBuilder(String imagesDir, String destDir) {
        this.imagesDir = imagesDir;
        this.destDir = destDir;
    }

    public void exec() throws IOException {
        System.out.println("start");

        List<String> imageList = getFileNameList(Paths.get(imagesDir));

        // sort
        Collections.sort(imageList, new ImageIDComparator());

        JSONArray tagListJa = new JSONArray();
        JSONArray imageListJa = new JSONArray(imageList);
        JSONArray imageIndexListListJa = new JSONArray();
        
        Files.createDirectories(Paths.get(destDir));

        CalabazaUtils.outputToFile(Paths.get(destDir, TAG_LIST_JSON_FILE), tagListJa.toString());
        CalabazaUtils.outputToFile(Paths.get(destDir, IMAGE_LIST_JSON_FILE), imageListJa.toString());
        CalabazaUtils.outputToFile(Paths.get(destDir, IMAGE_INDEX_LIST_LIST_JSON_FILE), imageIndexListListJa.toString());

        System.out.println("end");
    }

    private List<String> getFileNameList(Path dirPath) throws IOException {
        try (
            DirectoryStream<Path> dirIn = Files.newDirectoryStream(dirPath);
        ) {
            List<String> fileNameList = new ArrayList<String>();
            for (Path filePath : dirIn) {
                String fileName = filePath.getFileName().toString();
                if (fileName.matches(IMAGE_FILE_NAME_PATTERN)) {
                    fileNameList.add(fileName);
                }
            }
            return fileNameList;
        }
    }

}
