package calabaza.server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.json.JSONArray;

import calabaza.CalabazaLogic;
import calabaza.CalabazaSingletonManager;
import calabaza.CalabazaUtils;

@WebListener
public class CalabazaServletContextListener implements ServletContextListener {

    private String dataDir;

    @SuppressWarnings("unchecked")
    @Override
    public void contextInitialized(ServletContextEvent scEvent) {
        try (
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("calabaza.properties");
        ) {
            Properties prop = new Properties();
            prop.load(in);

            this.dataDir = prop.getProperty("dataDir");
            JSONArray tagListJa = new JSONArray(CalabazaUtils.getTextFromFile(Paths.get(dataDir, "tagls.json")));
            JSONArray imageListJa = new JSONArray(CalabazaUtils.getTextFromFile(Paths.get(dataDir, "imagels.json")));
            JSONArray imageIndexListListJa = new JSONArray(CalabazaUtils.getTextFromFile(Paths.get(dataDir, "imageilsls.json")));

            List<String> tagList = (List<String>) CalabazaUtils.convertFromJson(tagListJa);
            List<String> imageList = (List<String>) CalabazaUtils.convertFromJson(imageListJa);
            List<List<Integer>> imageIndexListList = (List<List<Integer>>) CalabazaUtils.convertFromJson(imageIndexListListJa);

            CalabazaSingletonManager.getInstance(tagList, imageList, imageIndexListList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent scEvent) {
        CalabazaLogic logic = CalabazaSingletonManager.getInstance().getLogic();

        JSONArray tagListJa = new JSONArray(logic.getTagList());
        JSONArray imageListJa = new JSONArray(logic.getImageList());
        JSONArray imageIndexListListJa = new JSONArray(logic.getImageIndexListList());

        try {
            CalabazaUtils.outputToFile(Paths.get(this.dataDir, "tagls.json"), tagListJa.toString());
            CalabazaUtils.outputToFile(Paths.get(this.dataDir, "imagels.json"), imageListJa.toString());
            CalabazaUtils.outputToFile(Paths.get(this.dataDir, "imageilsls.json"), imageIndexListListJa.toString());

            CalabazaUtils.outputToFile(Paths.get(this.dataDir, "tagls.js"), logic.getJs("tagls.js"));
            CalabazaUtils.outputToFile(Paths.get(this.dataDir, "imagels.js"), logic.getJs("imagels.js"));
            CalabazaUtils.outputToFile(Paths.get(this.dataDir, "imageilsls.js"), logic.getJs("imageilsls.js"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
