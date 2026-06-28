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
import jakarta.servlet.http.HttpSession;

import jakarta_configuration.resources.JPAUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "AdminRichiesteServlet",
        urlPatterns = {"/admin/richieste"}
)
public class AdminRichiesteServlet extends HttpServlet {

    private Configuration cfg;

    @Override
    public void init() throws ServletException {

        cfg = new Configuration(
                Configuration.VERSION_2_3_32
        );

        cfg.setClassLoaderForTemplateLoading(
                Thread.currentThread()
                        .getContextClassLoader(),
                "/templates"
        );

        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        cfg.setURLEscapingCharset("UTF-8");

        cfg.setTemplateExceptionHandler(
                TemplateExceptionHandler.HTML_DEBUG_HANDLER
        );
    }

    private boolean isAdmin(
            HttpServletRequest request
    ) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        String ruolo =
                String.valueOf(
                        session.getAttribute("ruolo")
                );

        return "ADMIN".equalsIgnoreCase(ruolo);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        if (!isAdmin(request)) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        HttpSession session =
                request.getSession(false);

        EntityManager entityManager =
                JPAUtil.getEntityManager();

        try {

            DaoInterfaceRichiesta daoRichiesta =
                    new DaoInterfaceRichiestaImpl(
                            entityManager
                    );

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "contextPath",
                    request.getContextPath()
            );

            data.put(
                    "nome",
                    session.getAttribute("nome")
            );

            data.put(
                    "ruolo",
                    session.getAttribute("ruolo")
            );

            data.put(
                    "richieste",
                    daoRichiesta.findAll()
            );

            /*
             * Recupera i parametri presenti nell'URL
             * dopo i redirect delle altre servlet.
             */
            String ok =
                    normalizza(
                            request.getParameter("ok")
                    );

            String errore =
                    normalizza(
                            request.getParameter("errore")
                    );

            String successo =
                    normalizza(
                            request.getParameter("successo")
                    );

            if (!ok.isBlank()) {
                data.put("ok", ok);
            }

            if (!errore.isBlank()) {
                data.put("errore", errore);
            }

            if (!successo.isBlank()) {
                data.put("successo", successo);
            }

            /*
             * Il nome è al plurale:
             * admin-richieste.ftl
             */
            renderTemplate(
                    response,
                    "admin-richieste.ftl",
                    data
            );

        } finally {

            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    private String normalizza(String valore) {

        if (valore == null) {
            return "";
        }

        return valore.trim();
    }

    private void renderTemplate(
            HttpServletResponse response,
            String templateName,
            Map<String, Object> data
    ) throws ServletException, IOException {

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        try {

            Template template =
                    cfg.getTemplate(templateName);

            template.process(
                    data,
                    response.getWriter()
            );

        } catch (Exception e) {

            throw new ServletException(
                    "Errore nel caricamento del template "
                    + templateName,
                    e
            );
        }
    }
}