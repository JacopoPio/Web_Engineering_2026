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

import model.Missione;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(
        name = "AdminMissioniServlet",
        urlPatterns = {"/admin/missioni"}
)
public class AdminMissioneServlet extends HttpServlet {

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
                (String) session.getAttribute("ruolo");

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

        EntityManager entityManager =
                JPAUtil.getEntityManager();

        try {

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(
                            entityManager
                    );

            List<Missione> missioni =
                    daoMissione.findAll();

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "contextPath",
                    request.getContextPath()
            );

            data.put(
                    "missioni",
                    missioni
            );

            String successo =
                    request.getParameter("successo");

            if (successo != null
                    && !successo.isBlank()) {

                data.put(
                        "successo",
                        successo
                );
            }

            String errore =
                    request.getParameter("errore");

            if (errore != null
                    && !errore.isBlank()) {

                data.put(
                        "errore",
                        errore
                );
            }

            renderTemplate(
                    response,
                    "gestione-missioni.ftl",
                    data
            );

        } finally {

            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
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