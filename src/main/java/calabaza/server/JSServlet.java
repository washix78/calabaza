package calabaza.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import calabaza.CalabazaSingletonManager;

@WebServlet(name = "JSServlet", urlPatterns = { "/js/*" })
public class JSServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private List<String> DATA_JS_NAMES = Arrays.asList("tagls.js", "imagels.js", "imageilsls.js");

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/javascript; charset=UTF-8");

        try (
            PrintWriter out = response.getWriter();
        ) {
            String jsName = request.getPathInfo().replaceFirst("/", "");
            if (DATA_JS_NAMES.indexOf(jsName) == -1) {
                jsName = request.getServletContext().getRealPath("/js/" + jsName);
            }
            String js = CalabazaSingletonManager.getInstance().getLogic().getJs(jsName);
            out.write(js);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
