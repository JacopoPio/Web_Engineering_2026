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

import model.Missione;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(
        name = "AdminMissioneServlet",
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

        EntityManager entityManager = null;

        try {

            entityManager =
                    JPAUtil.getEntityManager();

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(
                            entityManager
                    );

            List<Missione> missioniRecuperate =
                    daoMissione.findAll();

            /*
             * Lista senza eventuali elementi null.
             */
            List<Missione> missioni =
                    new ArrayList<>();

            if (missioniRecuperate != null) {

                for (Missione missione
                        : missioniRecuperate) {

                    if (missione != null && !missione.getRichiesta().getStato().equalsIgnoreCase("chiusa")) {
                        missioni.add(missione);
                    }
                }
            }

            System.out.println(
                    "ELEMENTI RESTITUITI DAL DAO: "
                    + (missioniRecuperate == null
                            ? 0
                            : missioniRecuperate.size())
            );

            System.out.println(
                    "MISSIONI VALIDE: "
                    + missioni.size()
            );

            /*
             * Ora il ciclo è sicuro, perché la lista
             * non contiene elementi null.
             */
            for (Missione missione : missioni) {

                System.out.println(
                        "Missione ID: "
                        + missione.getId()
                        + " - Descrizione: "
                        + missione.getDescrizione()
                );
            }

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
                    normalizza(
                            request.getParameter("successo")
                    );

            if ("creata".equals(successo)) {

                data.put(
                        "successo",
                        successo
                );

                data.put(
                        "messaggioSuccesso",
                        "Missione creata correttamente."
                );
            }

            String errore =
                    normalizza(
                            request.getParameter("errore")
                    );

            if (!errore.isBlank()) {

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

        } catch (RuntimeException e) {

            throw new ServletException(
                    "Errore durante il recupero "
                    + "delle missioni dal database",
                    e
            );

        } finally {

            if (entityManager != null
                    && entityManager.isOpen()) {

                entityManager.close();
            }
        }
    }

    private String normalizza(
            String valore
    ) {

        return valore == null
                ? ""
                : valore.trim();
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