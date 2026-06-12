package servlet;

import dao.DaoInterfaceMezzo;
import dao.dao_impl.DaoInterfaceMezzoImpl;
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
import model.Mezzo;

@WebServlet(
        name = "MezziServlet",
        urlPatterns = {
            "/mezzi",
            "/mezzi/inserisci",
            "/mezzi/modifica-form",
            "/mezzi/modifica",
            "/mezzi/elimina"
        }
)
public class AdminMezziServlet extends HttpServlet {

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

        if ("/mezzi/modifica-form".equals(path)) {
            String targa = request.getParameter("targa");

            if (targa == null || targa.isBlank()) {
                response.sendRedirect(request.getContextPath() + "/mezzi?errore=Targa mancante");
                return;
            }

            mostraPaginaMezzi(request, response, targa.trim().toUpperCase());
            return;
        }

        mostraPaginaMezzi(request, response, null);
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

        if ("/mezzi/inserisci".equals(path)) {
            inserisciMezzo(request, response);
            return;
        }

        if ("/mezzi/modifica".equals(path)) {
            modificaMezzo(request, response);
            return;
        }

        if ("/mezzi/elimina".equals(path)) {
            eliminaMezzo(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/mezzi?errore=Azione non valida");
    }

    private void mostraPaginaMezzi(HttpServletRequest request,
                                   HttpServletResponse response,
                                   String targaModifica)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMezzo daoMezzo = new DaoInterfaceMezzoImpl(em);

            List<Mezzo> mezzi = daoMezzo.findAll();

            HttpSession session = request.getSession(false);

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("mezzi", mezzi);

            if (session != null) {
                data.put("nome", session.getAttribute("nome"));
                data.put("ruolo", session.getAttribute("ruolo"));
            }

            if (targaModifica != null) {
                data.put("targaModifica", targaModifica);
            }

            if (request.getParameter("ok") != null) {
                data.put("ok", true);
            }

            if (request.getParameter("errore") != null) {
                data.put("errore", request.getParameter("errore"));
            }

            renderTemplate(response, "mezzi.ftl", data);

        } finally {
            em.close();
        }
    }

    private void inserisciMezzo(HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {

        String targa = request.getParameter("targa");
        String tipo = request.getParameter("tipo");

        if (targa == null || targa.isBlank()
                || tipo == null || tipo.isBlank()) {

            response.sendRedirect(request.getContextPath() + "/mezzi?errore=Compila targa e tipo");
            return;
        }

        targa = targa.trim().toUpperCase();
        tipo = tipo.trim();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMezzo daoMezzo = new DaoInterfaceMezzoImpl(em);

            Mezzo esistente = daoMezzo.findByTarga(targa);

            if (esistente != null) {
                response.sendRedirect(request.getContextPath() + "/mezzi?errore=Esiste gia  un mezzo con questa targa");
                return;
            }

            Mezzo mezzo = new Mezzo();
            mezzo.setTarga(targa);
            mezzo.setTipo(tipo);

            daoMezzo.save(mezzo);

            response.sendRedirect(request.getContextPath() + "/mezzi?ok=1");

        } finally {
            em.close();
        }
    }

    private void modificaMezzo(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {

        String targa = request.getParameter("targa");
        String tipo = request.getParameter("tipo");

        if (targa == null || targa.isBlank()
                || tipo == null || tipo.isBlank()) {

            response.sendRedirect(request.getContextPath() + "/mezzi?errore=Compila targa e tipo");
            return;
        }

        targa = targa.trim().toUpperCase();
        tipo = tipo.trim();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMezzo daoMezzo = new DaoInterfaceMezzoImpl(em);

            Mezzo mezzo = daoMezzo.findByTarga(targa);

            if (mezzo == null) {
                response.sendRedirect(request.getContextPath() + "/mezzi?errore=Mezzo non trovato");
                return;
            }

            mezzo.setTipo(tipo);

            daoMezzo.update(mezzo);

            response.sendRedirect(request.getContextPath() + "/mezzi?ok=1");

        } finally {
            em.close();
        }
    }

    private void eliminaMezzo(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {

        String targa = request.getParameter("targa");

        if (targa == null || targa.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/mezzi?errore=Targa mancante");
            return;
        }

        targa = targa.trim().toUpperCase();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMezzo daoMezzo = new DaoInterfaceMezzoImpl(em);

            boolean eliminato = daoMezzo.delete(targa);

            if (eliminato) {
                response.sendRedirect(request.getContextPath() + "/mezzi?ok=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/mezzi?errore=Mezzo non trovato");
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