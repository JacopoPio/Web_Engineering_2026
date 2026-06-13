package servlet;

import dao.DaoInterfaceAmministratore;
import dao.DaoInterfaceOperatore;
import dao.dao_impl.DaoInterfaceAmministratoreImpl;
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
import jakarta_configuration.resources.PasswordConverter;
import model.Amministratore;
import model.Operatore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

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

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());

        if (request.getParameter("errore") != null) {
            data.put("errore", true);
        }

        renderTemplate(response, "login.ftl", data);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String ruolo = normalizza(request.getParameter("ruolo")).toUpperCase();
        String username = normalizza(request.getParameter("username")).toLowerCase();
        String password = normalizza(request.getParameter("password"));

        if (ruolo.isBlank() || username.isBlank() || password.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login?errore=1");
            return;
        }

        /*
         * Admin fisso di emergenza.
         * Serve solo durante lo sviluppo.
         */
        if ("ADMIN".equals(ruolo)
                && "admin".equals(username)
                && "admin123".equals(password)) {

            HttpSession session = request.getSession();
            session.setAttribute("email", "admin");
            session.setAttribute("nome", "Amministratore");
            session.setAttribute("ruolo", "ADMIN");

            response.sendRedirect(request.getContextPath() + "/admin");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceAmministratore daoAdmin =
                    new DaoInterfaceAmministratoreImpl(em);

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(em);

            if ("ADMIN".equals(ruolo)) {
                Amministratore admin = daoAdmin.findByEmail(username);

                if (admin == null) {
                    response.sendRedirect(request.getContextPath() + "/login?errore=1");
                    return;
                }

                boolean passwordCorretta =
                        PasswordConverter.checkPassword(password, admin.getPassword());

                if (!passwordCorretta) {
                    response.sendRedirect(request.getContextPath() + "/login?errore=1");
                    return;
                }

                HttpSession session = request.getSession();
                session.setAttribute("email", admin.getEmail());
                session.setAttribute("nome", admin.getNome());
                session.setAttribute("ruolo", "ADMIN");

                response.sendRedirect(request.getContextPath() + "/admin");
                return;
            }

            if ("OPERATORE".equals(ruolo)) {
                Operatore operatore = daoOperatore.findByEmail(username);

                if (operatore == null) {
                    response.sendRedirect(request.getContextPath() + "/login?errore=1");
                    return;
                }

                boolean passwordCorretta =
                        PasswordConverter.checkPassword(password, operatore.getPassword());

                if (!passwordCorretta) {
                    response.sendRedirect(request.getContextPath() + "/login?errore=1");
                    return;
                }

                HttpSession session = request.getSession();
                session.setAttribute("email", operatore.getEmail());
                session.setAttribute("nome", operatore.getNome());
                session.setAttribute("ruolo", "OPERATORE");

                response.sendRedirect(request.getContextPath() + "/operatori");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/login?errore=1");

        } finally {
            em.close();
        }
    }

    private String normalizza(String valore) {
        if (valore == null) {
            return "";
        }

        return valore.trim();
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