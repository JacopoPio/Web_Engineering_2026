package servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private Configuration cfg;

    @Override
    public void init() throws ServletException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);

        cfg.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(),
                "/templates"
        );

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    }

    private void renderTemplate(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            Template template = cfg.getTemplate(templateName);
            template.process(data, response.getWriter());
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Object> data = new HashMap<>();

        data.put("titolo", "HOME-Soccorso Web");
        data.put("messaggio", "Benvenuto nel portale di soccorso");

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("nome") != null) {
            data.put("nome", session.getAttribute("nome"));
        } else {
            data.put("nome", "Ospite");
        }

        int a = 1 + new Random().nextInt(9);
        int b = 1 + new Random().nextInt(9);

        data.put("captchaA", a);
        data.put("captchaB", b);
        data.put("captchaRisposta", a + b);

        renderTemplate(response, "home.ftl", data);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet Home";
    }
}