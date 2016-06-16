package calabaza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class CalabazaMain {

    public static void main(String[] args) {
        try {
            Properties prop = getProp();
            Path tempDir = Files.createTempDirectory(Paths.get("."), "temp-").toRealPath();
            Path docDir = Paths.get(prop.getProperty("docDir")).toRealPath();

            Tomcat tomcat = new Tomcat();
            tomcat.setBaseDir(tempDir.toString());
            tomcat.setPort(Integer.parseInt(prop.getProperty("serverPort")));
            tomcat.addWebapp("/calabaza", docDir.toString());
            tomcat.start();

            waiting();

            tomcat.stop();
            deleteFiles(tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
        System.out.println("終了");
    }

    private static Properties getProp() throws IOException {
        Properties prop = new Properties();
        try (
            InputStream in = CalabazaMain.class.getClassLoader().getResourceAsStream("calabaza.properties");
        ) {
            prop.load(in);
        }
        return prop;
    }

    private static void waiting() throws IOException {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ) {
            boolean isContinue = true;
            while (isContinue) {
                System.out.print("アプリケーションを終了したい場合は、「Q」を入力してください。：");
                String line = in.readLine();
                if ("Q".equals(line)) {
                    isContinue = false;
                }
            }
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
