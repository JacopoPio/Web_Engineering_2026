package servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RichiestaInviataServlet", urlPatterns = {"/richiesta-inviata"})
public class RichiestaInviataServlet extends HttpServlet {

    private Configuration cfg;

    @Override
    public void init() throws ServletException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(),
                "/templates"
        );
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());
        response.setContentType("text/html;charset=UTF-8");
        try {
            Template template = cfg.getTemplate("richiesta-inviata.ftl");
            template.process(data, response.getWriter());
        } catch (Exception e) {
            throw new ServletException("Errore nel template richiesta-inviata.ftl", e);
        }
    }
}
