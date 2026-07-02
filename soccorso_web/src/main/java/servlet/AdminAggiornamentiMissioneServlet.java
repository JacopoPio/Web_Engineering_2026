package servlet;

import dao.DaoInterfaceMissione;
import dao.dao_impl.DaoInterfaceMissioneImpl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import jakarta.persistence.EntityManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jakarta_configuration.resources.JPAUtil;

import model.Aggiornamento;
import model.Missione;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(
        name = "AdminAggiornamentiMissioneServlet",
        urlPatterns = {"/admin/missioni/aggiornamenti"}
)
public class AdminAggiornamentiMissioneServlet extends HttpServlet {

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

        Object ruolo =
                session.getAttribute("ruolo");

        return ruolo != null
                && "ADMIN".equalsIgnoreCase(
                        ruolo.toString()
                );
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

        String idParam =
                request.getParameter("id");

        if (idParam == null
                || idParam.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/missioni"
            );

            return;
        }

        int missioneId;

        try {

            missioneId =
                    Integer.parseInt(idParam.trim());

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/missioni"
            );

            return;
        }

        EntityManager entityManager = null;

        try {

            entityManager =
                    JPAUtil.getEntityManager();

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(
                            entityManager
                    );

            Missione missione =
                    daoMissione.findById(missioneId);

            if (missione == null) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/missioni"
                );

                return;
            }

            List<Aggiornamento> aggiornamentiRecuperati =
                    new ArrayList<>(
                            missione.getAggiornamenti()
                    );

            List<Aggiornamento> aggiornamenti =
                    new ArrayList<>();

            for (Aggiornamento aggiornamento
                    : aggiornamentiRecuperati) {

                if (aggiornamento != null) {
                    aggiornamenti.add(aggiornamento);
                }
            }

            /*
             * Ordino dal più recente al più vecchio.
             */
            aggiornamenti.sort(
                    Comparator.comparing(
                            Aggiornamento::getData_update,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "contextPath",
                    request.getContextPath()
            );

            data.put(
                    "missione",
                    missione
            );

            data.put(
                    "aggiornamenti",
                    aggiornamenti
            );

            renderTemplate(
                    response,
                    "aggiornamenti-missione.ftl",
                    data
            );

        } catch (RuntimeException e) {

            throw new ServletException(
                    "Errore durante il recupero "
                    + "degli aggiornamenti",
                    e
            );

        } finally {

            if (entityManager != null
                    && entityManager.isOpen()) {

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
                    cfg.getTemplate(
                            templateName
                    );

            template.process(
                    data,
                    response.getWriter()
            );

        } catch (TemplateException e) {

            throw new ServletException(
                    "Errore durante l'elaborazione "
                    + "del template "
                    + templateName,
                    e
            );
        }
    }
}