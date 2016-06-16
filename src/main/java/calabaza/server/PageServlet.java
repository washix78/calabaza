package calabaza.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import calabaza.CalabazaPage;
import calabaza.CalabazaUtils;

@WebServlet(name = "PageServlet", urlPatterns = { "/api/page" })
public class PageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (
            PrintWriter out = response.getWriter();
        ) {
            HttpSession session = request.getSession();
            Object pageO = session.getAttribute("page");
            CalabazaPage page = (pageO == null) ? new CalabazaPage() : (CalabazaPage) pageO;
            session.setAttribute("page", page);

            String body = CalabazaUtils.getTextFromReader(request.getReader());
            Map<String, Object> paramMap = (Map<String, Object>) CalabazaUtils.convertFromJson(body);

            Integer displayCount = Integer.parseInt((String) paramMap.get("displayCount"));
            List<String> displayTagList = (List<String>) paramMap.get("displayTagList");
            page.changeCondition(displayCount, displayTagList);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
