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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.Amministratore;
import model.Operatore;

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
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
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
            nega(request, response);
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            if ("ADMIN".equals(ruolo)) {
                DaoInterfaceAmministratore dao = new DaoInterfaceAmministratoreImpl(em);
                Amministratore admin = dao.findByEmail(username);

                if (admin == null || !admin.isAttivo()
                        || !PasswordConverter.checkPassword(password, admin.getPassword())) {
                    nega(request, response);
                    return;
                }

                creaSessione(request, admin.getEmail(), admin.getNome(), "ADMIN");
                response.sendRedirect(request.getContextPath() + "/admin");
                return;
            }

            if ("OPERATORE".equals(ruolo)) {
                DaoInterfaceOperatore dao = new DaoInterfaceOperatoreImpl(em);
                Operatore operatore = dao.findByEmail(username);

                if (operatore == null || !operatore.isAttivo()
                        || !PasswordConverter.checkPassword(password, operatore.getPassword())) {
                    nega(request, response);
                    return;
                }

                creaSessione(request, operatore.getEmail(), operatore.getNome(), "OPERATORE");
                response.sendRedirect(request.getContextPath() + "/operatori");
                return;
            }

            nega(request, response);

        } finally {
            em.close();
        }
    }

    private void creaSessione(HttpServletRequest request,
                              String email,
                              String nome,
                              String ruolo) {
        HttpSession vecchia = request.getSession(false);
        if (vecchia != null) {
            vecchia.invalidate();
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("email", email);
        session.setAttribute("nome", nome);
        session.setAttribute("ruolo", ruolo);
        session.setMaxInactiveInterval(30 * 60);
    }

    private void nega(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login?errore=1");
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
