package servlet;

import dao.DaoInterfaceMissione;
import dao.dao_impl.DaoInterfaceMissioneImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta_configuration.resources.JPAUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Missione;
import model.StatoRichiesta;

@WebServlet(name = "AdminMissioneServlet", urlPatterns = {"/admin/missioni"})
public class AdminMissioneServlet extends HttpServlet {

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

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("ruolo"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceMissione dao = new DaoInterfaceMissioneImpl(em);
            List<Missione> missioni = dao.findAll().stream()
                    .filter(m -> m.getRichiesta() != null)
                    .filter(m -> StatoRichiesta.IN_CORSO.equalsIgnoreCase(m.getRichiesta().getStato()))
                    .toList();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("missioni", missioni);

            String successo = normalizza(request.getParameter("successo"));
            String errore = normalizza(request.getParameter("errore"));
            if (!successo.isBlank()) {
                data.put("successo", successo);
            }
            if (!errore.isBlank()) {
                data.put("errore", errore);
            }

            renderTemplate(response, "gestione-missioni.ftl", data);
        } finally {
            em.close();
        }
    }

    private String normalizza(String valore) {
        return valore == null ? "" : valore.trim();
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
