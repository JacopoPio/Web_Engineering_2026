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
import model.Richiesta;
import model.StatoRichiesta;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
                TemplateExceptionHandler.RETHROW_HANDLER
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (!isAdmin(session)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        String vista =
                normalizza(
                        request.getParameter("vista")
                );

        boolean mostraArchivio =
                "archivio".equalsIgnoreCase(vista);

        EntityManager em =
                JPAUtil.getEntityManager();

        try {
            DaoInterfaceRichiesta dao =
                    new DaoInterfaceRichiestaImpl(em);

            List<Richiesta> richieste =
                    dao.findAll()
                            .stream()

                            /*
                             * Evita richieste con stato nullo.
                             */
                            .filter(richiesta ->
                                    richiesta.getStato() != null
                            )

                            /*
                             * Nell'archivio vengono mostrate
                             * solo le richieste archiviate.
                             *
                             * Nella pagina normale vengono mostrate
                             * solo quelle non archiviate.
                             */
                            .filter(richiesta ->
                                    richiesta.isArchiviata()
                                            == mostraArchivio
                            )

                            /*
                             * Le richieste da confermare non devono
                             * essere visibili nella gestione admin.
                             */
                            .filter(richiesta ->
                                    !StatoRichiesta.DA_CONFERMARE
                                            .equalsIgnoreCase(
                                                    richiesta.getStato()
                                            )
                            )

                            .toList();

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
                    richieste
            );

            data.put(
                    "mostraArchivio",
                    mostraArchivio
            );

            String errore =
                    normalizza(
                            request.getParameter("errore")
                    );

            String successo =
                    normalizza(
                            request.getParameter("successo")
                    );

            if (!errore.isBlank()) {
                data.put("errore", errore);
            }

            if (!successo.isBlank()) {
                data.put("successo", successo);
            }

            renderTemplate(
                    response,
                    "admin-richieste.ftl",
                    data
            );

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (!isAdmin(session)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String azione =
                normalizza(
                        request.getParameter("azione")
                ).toLowerCase();

        String email =
                normalizza(
                        request.getParameter("email")
                ).toLowerCase();

        if (email.isBlank()) {
            redirectErrore(
                    request,
                    response,
                    "richiesta_non_valida",
                    false
            );
            return;
        }

        EntityManager em =
                JPAUtil.getEntityManager();

        try {
            DaoInterfaceRichiesta dao =
                    new DaoInterfaceRichiestaImpl(em);

            Richiesta richiesta =
                    dao.findByEmail(email);

            if (richiesta == null) {
                redirectErrore(
                        request,
                        response,
                        "richiesta_non_trovata",
                        false
                );
                return;
            }

            switch (azione) {

                case "archivia":

                    archiviaRichiesta(
                            request,
                            response,
                            richiesta,
                            dao
                    );

                    break;

                case "ripristina":

                    ripristinaRichiesta(
                            request,
                            response,
                            richiesta,
                            dao
                    );

                    break;

                default:

                    redirectErrore(
                            request,
                            response,
                            "azione_non_valida",
                            false
                    );

                    break;
            }

        } catch (RuntimeException e) {

            e.printStackTrace();

            boolean tornaArchivio =
                    "ripristina".equals(azione);

            redirectErrore(
                    request,
                    response,
                    "operazione_fallita",
                    tornaArchivio
            );

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private void archiviaRichiesta(
            HttpServletRequest request,
            HttpServletResponse response,
            Richiesta richiesta,
            DaoInterfaceRichiesta dao
    ) throws IOException {

        /*
         * Possono essere archiviate soltanto
         * le richieste chiuse.
         */
        if (!StatoRichiesta.CHIUSA.equalsIgnoreCase(
                richiesta.getStato()
        )) {

            redirectErrore(
                    request,
                    response,
                    "richiesta_non_chiusa",
                    false
            );

            return;
        }

        if (richiesta.isArchiviata()) {

            redirectErrore(
                    request,
                    response,
                    "richiesta_gia_archiviata",
                    false
            );

            return;
        }

        richiesta.setArchiviata(true);

        dao.update(richiesta);

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/richieste"
                        + "?successo=archiviata"
        );
    }

    private void ripristinaRichiesta(
            HttpServletRequest request,
            HttpServletResponse response,
            Richiesta richiesta,
            DaoInterfaceRichiesta dao
    ) throws IOException {

        if (!richiesta.isArchiviata()) {

            redirectErrore(
                    request,
                    response,
                    "richiesta_non_archiviata",
                    true
            );

            return;
        }

        richiesta.setArchiviata(false);

        dao.update(richiesta);

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/richieste"
                        + "?vista=archivio"
                        + "&successo=ripristinata"
        );
    }

    private boolean isAdmin(
            HttpSession session
    ) {

        return session != null
                && "ADMIN".equals(
                        session.getAttribute("ruolo")
                );
    }

    private void redirectErrore(
            HttpServletRequest request,
            HttpServletResponse response,
            String errore,
            boolean tornaArchivio
    ) throws IOException {

        String url =
                request.getContextPath()
                        + "/admin/richieste";

        if (tornaArchivio) {
            url += "?vista=archivio&errore=" + errore;
        } else {
            url += "?errore=" + errore;
        }

        response.sendRedirect(url);
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
                    cfg.getTemplate(templateName);

            template.process(
                    data,
                    response.getWriter()
            );

        } catch (Exception e) {

            throw new ServletException(
                    "Errore nel template "
                            + templateName,
                    e
            );
        }
    }
}