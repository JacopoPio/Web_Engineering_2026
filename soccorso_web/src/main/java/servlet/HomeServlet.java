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
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", ""})
public class HomeServlet extends HttpServlet {

    private Configuration cfg;
    private final SecureRandom random = new SecureRandom();

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

        int captchaA = random.nextInt(9) + 1;
        int captchaB = random.nextInt(9) + 1;

        HttpSession session = request.getSession(true);
        session.setAttribute("captchaRisultato", captchaA + captchaB);

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());
        data.put("titolo", "SoccorsoWeb");
        data.put("messaggio", "Portale per le richieste di soccorso");
        data.put("captchaA", captchaA);
        data.put("captchaB", captchaB);

        String errore = request.getParameter("errore");
        if (errore != null && !errore.isBlank()) {
            data.put("errore", errore.trim());
        }

        renderTemplate(response, "home.ftl", data);
    }

    private void renderTemplate(HttpServletResponse response,
                                String templateName,
                                Map<String, Object> data)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            Template template = cfg.getTemplate(templateName);
            template.process(data, response.getWriter());
        } catch (Exception e) {
            throw new ServletException("Errore nel template " + templateName, e);
        }
    }
}
