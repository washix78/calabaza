package calabaza.server;

import java.io.IOException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import calabaza.CalabazaLogic;
import calabaza.CalabazaSingletonManager;
import calabaza.CalabazaUtils;

@WebServlet(name = "TagServlet", urlPatterns = { "/api/tag" })
public class TagServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    private CalabazaLogic logic = CalabazaSingletonManager.getInstance().getLogic();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = null;
        try {
            content = new JSONObject(logic.getTagDataMap()).toString();
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = null;
        try {
            String body = CalabazaUtils.getTextFromReader(request.getReader());
            Map<String, String> paramMap = (Map<String, String>) CalabazaUtils.convertFromJson(body);

            String newTag = paramMap.get("tag");
            content = new JSONObject(logic.addTag(newTag)).toString();
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
            Map<String, String> paramMap = (Map<String, String>) CalabazaUtils.convertFromJson(body);

            String oldTag = (String) paramMap.get("old");
            String newTag = (String) paramMap.get("new");
            content = new JSONObject(logic.updateTag(oldTag, newTag)).toString();
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
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        try {
            String body = CalabazaUtils.getTextFromReader(request.getReader());
            Map<String, String> paramMap = (Map<String, String>) CalabazaUtils.convertFromJson(body);

            logic.removeTag((String) paramMap.get("tag"));
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
