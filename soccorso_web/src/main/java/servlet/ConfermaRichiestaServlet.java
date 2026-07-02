package servlet;

import dao.DaoInterfaceRichiesta;
import dao.dao_impl.DaoInterfaceRichiestaImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta_configuration.resources.JPAUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import model.Richiesta;
import model.StatoRichiesta;

@WebServlet(name = "ConfermaRichiestaServlet", urlPatterns = {"/conferma-richiesta"})
public class ConfermaRichiestaServlet extends HttpServlet {

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

        String token = request.getParameter("token");
        String esito = "token_non_valido";

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceRichiesta dao = new DaoInterfaceRichiestaImpl(em);
            Richiesta richiesta = dao.findByToken(token);

            if (richiesta != null) {
                if (!StatoRichiesta.DA_CONFERMARE.equalsIgnoreCase(richiesta.getStato())) {
                    esito = "gia_confermata";
                } else if (richiesta.getDataCreazione() == null
                        || richiesta.getDataCreazione().isBefore(LocalDateTime.now().minusHours(24))) {
                    esito = "token_scaduto";
                } else {
                    richiesta.setStato(StatoRichiesta.ATTIVA);
                    richiesta.setDataConferma(LocalDateTime.now());
                    richiesta.setTokenConferma(null);
                    dao.update(richiesta);
                    esito = "confermata";
                }
            }
        } finally {
            em.close();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());
        data.put("esito", esito);
        renderTemplate(response, "conferma-richiesta.ftl", data);
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
