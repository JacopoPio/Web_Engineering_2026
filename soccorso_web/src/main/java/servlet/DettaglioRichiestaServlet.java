/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Richiesta;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author alesp
 */
@WebServlet(name = "DettagliRichiestaServlet", urlPatterns = {"/admin/dettagli-richiesta"})
public class DettaglioRichiestaServlet extends HttpServlet {

    private Configuration cfg;
    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);

        cfg.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(),
                "/templates"
        );

        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);

        emf = Persistence.createEntityManagerFactory("Soccorso");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("ruolo"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String email = request.getParameter("email");

        if (email == null || email.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/admin/richieste?errore=1");
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            Richiesta richiesta = em.find(Richiesta.class, email);

            if (richiesta == null) {
                response.sendRedirect(request.getContextPath() + "/admin/richieste?errore=1");
                return;
            }

            Map<String, Object> data = new HashMap<>();

            data.put("contextPath", request.getContextPath());
            data.put("nome", session.getAttribute("nome"));
            data.put("ruolo", session.getAttribute("ruolo"));
            data.put("richiesta", richiesta);

            renderTemplate(response, "admin-dettaglio-richiesta.ftl", data);

        } finally {
            em.close();
        }
    }

    private void renderTemplate(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            Template template = cfg.getTemplate(templateName);
            template.process(data, response.getWriter());
        } catch (Exception e) {
            throw new ServletException("Errore nel caricamento del template " + templateName, e);
        }
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}