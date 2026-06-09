/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.DaoInterfaceRichiesta;
import dao.dao_impl.DaoInterfaceRichiestaImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta_configuration.resources.JPAUtil;
import model.Richiesta;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author alesp
 */
@WebServlet(name = "AdminRichiesteServlet", urlPatterns = {"/admin/richieste"})
public class AdminRichiesteServlet extends HttpServlet {

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("ruolo") == null
                || !"ADMIN".equals(session.getAttribute("ruolo"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        EntityManager entityManager = JPAUtil.getEntityManager();

        try {
            DaoInterfaceRichiesta richiestaDao =
                    new DaoInterfaceRichiestaImpl(entityManager);

            List<Richiesta> richieste = richiestaDao.findAll();

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("pageTitle", "Gestione richieste");
            data.put("richieste", richieste);

            response.setContentType("text/html;charset=UTF-8");

            Template template = cfg.getTemplate("admin-richieste.ftl");
            template.process(data, response.getWriter());

        } catch (Exception e) {
            throw new ServletException("Errore durante il caricamento delle richieste", e);

        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}