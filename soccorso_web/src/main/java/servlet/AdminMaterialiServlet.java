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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && "ADMIN".equals(session.getAttribute("ruolo"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String tipoModifica = null;
        if ("/materiali/modifica-form".equals(request.getServletPath())) {
            tipoModifica = normalizza(request.getParameter("tipo"));
            if (tipoModifica.isBlank()) {
                redirectErrore(request, response, "Tipo materiale mancante");
                return;
            }
        }

        mostraPagina(request, response, tipoModifica);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        switch (request.getServletPath()) {
            case "/materiali/inserisci" -> inserisci(request, response);
            case "/materiali/modifica" -> modifica(request, response);
            case "/materiali/elimina" -> elimina(request, response);
            default -> redirectErrore(request, response, "Azione non valida");
        }
    }

    private void mostraPagina(HttpServletRequest request,
                              HttpServletResponse response,
                              String tipoModifica)
            throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceMateriale dao = new DaoInterfaceMaterialeImpl(em);
            List<Materiale> materiali = dao.findAll();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("materiali", materiali);
            data.put("nome", request.getSession(false).getAttribute("nome"));
            data.put("ruolo", "ADMIN");

            if (tipoModifica != null) {
                data.put("tipoModifica", tipoModifica);
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

    private void inserisci(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tipo = normalizza(request.getParameter("tipo"));
        String descrizione = normalizza(request.getParameter("descrizione"));

        if (tipo.isBlank() || descrizione.isBlank()) {
            redirectErrore(request, response, "Compila tipo e descrizione");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceMateriale dao = new DaoInterfaceMaterialeImpl(em);
            if (dao.findByTipo(tipo) != null) {
                redirectErrore(request, response, "Esiste già un materiale con questo tipo");
                return;
            }

            Materiale materiale = new Materiale();
            materiale.setTipo(tipo);
            materiale.setDescrizione(descrizione);
            dao.save(materiale);
            response.sendRedirect(request.getContextPath() + "/materiali?ok=1");
        } finally {
            em.close();
        }
    }

    private void modifica(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tipo = normalizza(request.getParameter("tipo"));
        String descrizione = normalizza(request.getParameter("descrizione"));

        if (tipo.isBlank() || descrizione.isBlank()) {
            redirectErrore(request, response, "Compila tipo e descrizione");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceMateriale dao = new DaoInterfaceMaterialeImpl(em);
            Materiale materiale = dao.findByTipo(tipo);
            if (materiale == null) {
                redirectErrore(request, response, "Materiale non trovato");
                return;
            }

            materiale.setDescrizione(descrizione);
            dao.update(materiale);
            response.sendRedirect(request.getContextPath() + "/materiali?ok=1");
        } finally {
            em.close();
        }
    }

    private void elimina(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tipo = normalizza(request.getParameter("tipo"));
        if (tipo.isBlank()) {
            redirectErrore(request, response, "Tipo materiale mancante");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceMateriale dao = new DaoInterfaceMaterialeImpl(em);
            try {
                boolean eliminato = dao.delete(tipo);
                if (!eliminato) {
                    redirectErrore(request, response, "Materiale non trovato");
                    return;
                }
                response.sendRedirect(request.getContextPath() + "/materiali?ok=1");
            } catch (IllegalStateException e) {
                redirectErrore(request, response, e.getMessage());
            }
        } finally {
            em.close();
        }
    }

    private void redirectErrore(HttpServletRequest request,
                                HttpServletResponse response,
                                String messaggio) throws IOException {
        response.sendRedirect(
                request.getContextPath() + "/materiali?errore="
                + URLEncoder.encode(messaggio, StandardCharsets.UTF_8)
        );
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
