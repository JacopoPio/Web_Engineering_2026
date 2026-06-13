package servlet;

import dao.DaoInterfaceMateriale;
import dao.dao_impl.DaoInterfaceMaterialeImpl;
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

@WebServlet(
        name = "MaterialiServlet",
        urlPatterns = {
            "/materiali",
            "/materiali/inserisci",
            "/materiali/modifica-form",
            "/materiali/modifica",
            "/materiali/elimina"
        }
)
public class AdminMaterialiServlet extends HttpServlet {

    private Configuration cfg;

    @Override
    public void init() throws ServletException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);

        cfg.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(),
                "/templates"
        );

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        String ruolo = (String) session.getAttribute("ruolo");

        return "ADMIN".equals(ruolo);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String path = request.getServletPath();

        if ("/materiali/modifica-form".equals(path)) {
            String tipo = request.getParameter("tipo");

            if (tipo == null || tipo.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/materiali?errore=Nome materiale mancante");
                return;
            }

            mostraPaginaMateriali(request, response, tipo.trim());
            return;
        }

        mostraPaginaMateriali(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();

        if ("/materiali/inserisci".equals(path)) {
            inserisciMateriale(request, response);
            return;
        }

        if ("/materiali/modifica".equals(path)) {
            modificaMateriale(request, response);
            return;
        }

        if ("/materiali/elimina".equals(path)) {
            eliminaMateriale(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/materiali?errore=Azione non valida");
    }

    private void mostraPaginaMateriali(HttpServletRequest request,
                                       HttpServletResponse response,
                                       String nomeModifica)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMateriale daoMateriale =
                    new DaoInterfaceMaterialeImpl(em);

            List<Materiale> materiali = daoMateriale.findAll();

            HttpSession session = request.getSession(false);

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("materiali", materiali);

            if (session != null) {
                data.put("nome", session.getAttribute("nome"));
                data.put("ruolo", session.getAttribute("ruolo"));
            }

            if (nomeModifica != null) {
                data.put("nomeModifica", nomeModifica);
            }

            if (request.getParameter("ok") != null) {
                data.put("ok", true);
            }

            if (request.getParameter("errore") != null) {
                data.put("errore", request.getParameter("errore"));
            }

            renderTemplate(response, "admin-materiali.ftl", data);

        } finally {
            em.close();
        }
    }

    private void inserisciMateriale(HttpServletRequest request,
                                    HttpServletResponse response)
            throws IOException {

        String tipo = request.getParameter("tipo");
        String descrizione = request.getParameter("descrizione");

        if (tipo == null || tipo.isBlank()
                || descrizione == null || descrizione.isBlank()) {

            response.sendRedirect(request.getContextPath() + "/materiali?errore=Compila nome e descrizione");
            return;
        }

        tipo = tipo.trim();
        descrizione = descrizione.trim();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMateriale daoMateriale =
                    new DaoInterfaceMaterialeImpl(em);

            Materiale esistente = daoMateriale.findByTipo(tipo);

            if (esistente != null) {
                response.sendRedirect(request.getContextPath() + "/materiali?errore=Esiste già un materiale con questo nome");
                return;
            }

            Materiale materiale = new Materiale();
            materiale.setTipo(tipo);
            materiale.setDescrizione(descrizione);

            daoMateriale.save(materiale);

            response.sendRedirect(request.getContextPath() + "/materiali?ok=1");

        } finally {
            em.close();
        }
    }

    private void modificaMateriale(HttpServletRequest request,
                                   HttpServletResponse response)
            throws IOException {

        String tipo = request.getParameter("nome");
        String descrizione = request.getParameter("descrizione");

        if (tipo == null || tipo.isBlank()
                || descrizione == null || descrizione.isBlank()) {

            response.sendRedirect(request.getContextPath() + "/materiali?errore=Compila nome e descrizione");
            return;
        }

        tipo = tipo.trim();
        descrizione = descrizione.trim();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMateriale daoMateriale =
                    new DaoInterfaceMaterialeImpl(em);

            Materiale materiale = daoMateriale.findByTipo(tipo);

            if (materiale == null) {
                response.sendRedirect(request.getContextPath() + "/materiali?errore=Materiale non trovato");
                return;
            }

            materiale.setDescrizione(descrizione);

            daoMateriale.update(materiale);

            response.sendRedirect(request.getContextPath() + "/materiali?ok=1");

        } finally {
            em.close();
        }
    }

    private void eliminaMateriale(HttpServletRequest request,
                                  HttpServletResponse response)
            throws IOException {

        String tipo = request.getParameter("tipo");

        if (tipo == null || tipo.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/materiali?errore=Tipo materiale mancante");
            return;
        }

        tipo = tipo.trim();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMateriale daoMateriale =
                    new DaoInterfaceMaterialeImpl(em);

            boolean eliminato = daoMateriale.delete(tipo);

            if (eliminato) {
                response.sendRedirect(request.getContextPath() + "/materiali?ok=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/materiali?errore=Materiale non trovato");
            }

        } finally {
            em.close();
        }
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
            throw new ServletException("Errore nel caricamento del template " + templateName, e);
        }
    }
}