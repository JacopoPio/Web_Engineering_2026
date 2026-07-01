package servlet;

import dao.DaoInterfaceMateriale;
import dao.DaoInterfaceMezzo;
import dao.DaoInterfaceMissione;
import dao.DaoInterfaceOperatore;
import dao.dao_impl.DaoInterfaceMaterialeImpl;
import dao.dao_impl.DaoInterfaceMezzoImpl;
import dao.dao_impl.DaoInterfaceMissioneImpl;
import dao.dao_impl.DaoInterfaceOperatoreImpl;
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
import model.Materiale;
import model.Mezzo;
import model.Missione;
import model.Operatore;

@WebServlet(
        name = "StoricoMissioniServlet",
        urlPatterns = {
            "/storico/operatori",
            "/storico/mezzi",
            "/storico/materiali",
            "/storico/operatore",
            "/storico/mezzo",
            "/storico/materiale"
        }
)
public class StoricoMissioniServlet extends HttpServlet {

    private Configuration cfg;

    @Override
    public void init() throws ServletException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);

        cfg.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(),
                "/templates"
        );

        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        cfg.setURLEscapingCharset("UTF-8");
        cfg.setTemplateExceptionHandler(
                TemplateExceptionHandler.HTML_DEBUG_HANDLER
        );
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && "ADMIN".equals(session.getAttribute("ruolo"));
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();

        try {
            String path = request.getServletPath();

            /*
             * Pagine iniziali dello storico.
             * Mostrano gli elementi disponibili e permettono
             * di scegliere quello di cui visualizzare le missioni.
             */
            if ("/storico/operatori".equals(path)) {
                DaoInterfaceOperatore daoOperatore =
                        new DaoInterfaceOperatoreImpl(em);

                Map<String, Object> data = datiBase(request);
                data.put("titolo", "Storico missioni degli operatori");
                data.put("tipo", "operatore");
                data.put("operatori", daoOperatore.findAll());

                renderTemplate(response, "storico-elementi.ftl", data);
                return;
            }

            if ("/storico/mezzi".equals(path)) {
                DaoInterfaceMezzo daoMezzo =
                        new DaoInterfaceMezzoImpl(em);

                Map<String, Object> data = datiBase(request);
                data.put("titolo", "Storico missioni dei mezzi");
                data.put("tipo", "mezzo");
                data.put("mezzi", daoMezzo.findAll());

                renderTemplate(response, "storico-elementi.ftl", data);
                return;
            }

            if ("/storico/materiali".equals(path)) {
                DaoInterfaceMateriale daoMateriale =
                        new DaoInterfaceMaterialeImpl(em);

                Map<String, Object> data = datiBase(request);
                data.put("titolo", "Storico missioni dei materiali");
                data.put("tipo", "materiale");
                data.put("materiali", daoMateriale.findAll());

                renderTemplate(response, "storico-elementi.ftl", data);
                return;
            }

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(em);

            List<Missione> missioni;
            String titolo;
            String elemento;
            String ritorno;

            if ("/storico/mezzo".equals(path)) {
                String targa = request.getParameter("targa");

                if (targa == null || targa.isBlank()) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/storico/mezzi?errore=Targa mancante"
                    );
                    return;
                }

                DaoInterfaceMezzo daoMezzo =
                        new DaoInterfaceMezzoImpl(em);

                Mezzo mezzo = daoMezzo.findByTarga(targa);

                if (mezzo == null) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/storico/mezzi?errore=Mezzo non trovato"
                    );
                    return;
                }

                missioni = daoMissione.findByMezzo(mezzo.getTarga());
                titolo = "Storico missioni del mezzo";
                elemento = mezzo.getTarga() + " - " + mezzo.getTipo();
                ritorno = request.getContextPath() + "/storico/mezzi";

            } else if ("/storico/materiale".equals(path)) {
                String idParametro = request.getParameter("id");
                Long id;

                try {
                    id = Long.valueOf(idParametro);
                } catch (NumberFormatException e) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/storico/materiali?errore=Identificativo materiale non valido"
                    );
                    return;
                }

                DaoInterfaceMateriale daoMateriale =
                        new DaoInterfaceMaterialeImpl(em);

                Materiale materiale = daoMateriale.findById(id);

                if (materiale == null) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/storico/materiali?errore=Materiale non trovato"
                    );
                    return;
                }

                missioni = daoMissione.findByMateriale(id);
                titolo = "Storico missioni del materiale";
                elemento = materiale.getTipo();
                ritorno = request.getContextPath() + "/storico/materiali";

            } else if ("/storico/operatore".equals(path)) {
                String email = request.getParameter("email");

                if (email == null || email.isBlank()) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/storico/operatori?errore=Email operatore mancante"
                    );
                    return;
                }

                DaoInterfaceOperatore daoOperatore =
                        new DaoInterfaceOperatoreImpl(em);

                Operatore operatore = daoOperatore.findByEmail(email);

                if (operatore == null) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/storico/operatori?errore=Operatore non trovato"
                    );
                    return;
                }

                missioni = daoMissione.findByOperatore(email);
                titolo = "Storico missioni dell'operatore";
                elemento = operatore.getNome()
                        + " "
                        + operatore.getCognome()
                        + " - "
                        + operatore.getEmail();
                ritorno = request.getContextPath() + "/storico/operatori";

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Map<String, Object> data = datiBase(request);
            data.put("titolo", titolo);
            data.put("elemento", elemento);
            data.put("missioni", missioni);
            data.put("ritorno", ritorno);

            renderTemplate(response, "storico-missioni.ftl", data);

        } finally {
            em.close();
        }
    }

    private Map<String, Object> datiBase(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());

        HttpSession session = request.getSession(false);
        if (session != null) {
            data.put("nome", session.getAttribute("nome"));
            data.put("ruolo", session.getAttribute("ruolo"));
        }

        if (request.getParameter("errore") != null) {
            data.put("errore", request.getParameter("errore"));
        }

        return data;
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
            throw new ServletException(
                    "Errore nel caricamento del template " + templateName,
                    e
            );
        }
    }
}
