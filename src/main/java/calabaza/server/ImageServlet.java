package calabaza.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import calabaza.CalabazaLogic;
import calabaza.CalabazaSingletonManager;
import calabaza.CalabazaUtils;

@WebServlet(name = "ImageServlet", urlPatterns = { "/api/image" })
public class ImageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private CalabazaLogic logic = CalabazaSingletonManager.getInstance().getLogic();

    @SuppressWarnings("unchecked")
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = null;
        try {
            String body = CalabazaUtils.getTextFromReader(request.getReader());
            Map<String, Object> paramMap = (Map<String, Object>) CalabazaUtils.convertFromJson(body);

            List<String> imageList = (List<String>) paramMap.get("imageList");
            content = new JSONObject(logic.getImageDataMap(imageList)).toString();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject jo = new JSONObject();
            jo.put("message", e.getLocalizedMessage());
            content = jo.toString();
            e.printStackTrace();
        }
        CalabazaUtils.outputToStream(response.getWriter(), content);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = null;
        try {
            String body = CalabazaUtils.getTextFromReader(request.getReader());
            Map<String, Object> paramMap = (Map<String, Object>) CalabazaUtils.convertFromJson(body);

            String image = (String) paramMap.get("image");
            List<String> newTagList = (ArrayList<String>) paramMap.get("tagList");
            content = new JSONObject(logic.updateImageTagList(image, newTagList)).toString();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject jo = new JSONObject();
            jo.put("message", e.getLocalizedMessage());
            content = jo.toString();
            e.printStackTrace();
        }
        CalabazaUtils.outputToStream(response.getWriter(), content);
    }

}
