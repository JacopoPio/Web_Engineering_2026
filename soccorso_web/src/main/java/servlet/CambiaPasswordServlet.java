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

@WebServlet(
        name = "CambiaPasswordServlet",
        urlPatterns = {"/cambia-password"}
)
public class CambiaPasswordServlet extends HttpServlet {

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

        if (!sessioneValida(session)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

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

        String successo =
                normalizza(
                        request.getParameter("successo")
                );

        String errore =
                normalizza(
                        request.getParameter("errore")
                );

        if (!successo.isBlank()) {
            data.put("successo", successo);
        }

        if (!errore.isBlank()) {
            data.put("errore", errore);
        }

        renderTemplate(
                response,
                "cambia-password.ftl",
                data
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (!sessioneValida(session)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        /*
         * Le password non vengono sottoposte a trim:
         * eventuali spazi fanno parte della password.
         */
        String passwordAttuale =
                valorePassword(
                        request.getParameter("passwordAttuale")
                );

        String nuovaPassword =
                valorePassword(
                        request.getParameter("nuovaPassword")
                );

        String confermaPassword =
                valorePassword(
                        request.getParameter("confermaPassword")
                );

        if (passwordAttuale.isEmpty()
                || nuovaPassword.isEmpty()
                || confermaPassword.isEmpty()) {

            redirectErrore(
                    request,
                    response,
                    "campi_obbligatori"
            );
            return;
        }

        if (!nuovaPassword.equals(confermaPassword)) {

            redirectErrore(
                    request,
                    response,
                    "password_non_coincidenti"
            );
            return;
        }

        if (!passwordValida(nuovaPassword)) {

            redirectErrore(
                    request,
                    response,
                    "password_debole"
            );
            return;
        }

        String email =
                normalizza(
                        String.valueOf(
                                session.getAttribute("email")
                        )
                ).toLowerCase();

        String ruolo =
                normalizza(
                        String.valueOf(
                                session.getAttribute("ruolo")
                        )
                ).toUpperCase();

        if (email.isBlank()) {

            session.invalidate();

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        EntityManager em =
                JPAUtil.getEntityManager();

        try {

            boolean aggiornata;

            if ("ADMIN".equals(ruolo)) {

                aggiornata =
                        cambiaPasswordAmministratore(
                                em,
                                email,
                                passwordAttuale,
                                nuovaPassword
                        );

            } else if ("OPERATORE".equals(ruolo)) {

                aggiornata =
                        cambiaPasswordOperatore(
                                em,
                                email,
                                passwordAttuale,
                                nuovaPassword
                        );

            } else {

                response.sendRedirect(
                        request.getContextPath() + "/login"
                );
                return;
            }

            if (!aggiornata) {

                redirectErrore(
                        request,
                        response,
                        "password_attuale_errata"
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/cambia-password"
                            + "?successo=password_modificata"
            );

        } catch (PasswordUgualeException e) {

            redirectErrore(
                    request,
                    response,
                    "password_uguale"
            );

        } catch (RuntimeException e) {

            e.printStackTrace();

            redirectErrore(
                    request,
                    response,
                    "aggiornamento_fallito"
            );

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private boolean cambiaPasswordAmministratore(
            EntityManager em,
            String email,
            String passwordAttuale,
            String nuovaPassword
    ) {

        DaoInterfaceAmministratore dao =
                new DaoInterfaceAmministratoreImpl(em);

        Amministratore amministratore =
                dao.findByEmail(email);

        if (amministratore == null
                || !amministratore.isAttivo()) {

            return false;
        }

        String passwordHash =
                amministratore.getPassword();

        if (!PasswordConverter.checkPassword(
                passwordAttuale,
                passwordHash
        )) {
            return false;
        }

        /*
         * Impedisce di riutilizzare la stessa password.
         */
        if (PasswordConverter.checkPassword(
                nuovaPassword,
                passwordHash
        )) {
            throw new PasswordUgualeException();
        }

        /*
         * PasswordConverter cifrerà automaticamente
         * la nuova password durante il salvataggio.
         */
        amministratore.setPassword(
                nuovaPassword
        );

        dao.update(amministratore);

        return true;
    }

    private boolean cambiaPasswordOperatore(
            EntityManager em,
            String email,
            String passwordAttuale,
            String nuovaPassword
    ) {

        DaoInterfaceOperatore dao =
                new DaoInterfaceOperatoreImpl(em);

        Operatore operatore =
                dao.findByEmail(email);

        if (operatore == null
                || !operatore.isAttivo()) {

            return false;
        }

        String passwordHash =
                operatore.getPassword();

        if (!PasswordConverter.checkPassword(
                passwordAttuale,
                passwordHash
        )) {
            return false;
        }

        if (PasswordConverter.checkPassword(
                nuovaPassword,
                passwordHash
        )) {
            throw new PasswordUgualeException();
        }

        operatore.setPassword(
                nuovaPassword
        );

        dao.update(operatore);

        return true;
    }

    private boolean passwordValida(
            String password
    ) {

        if (password.length() < 8
                || password.length() > 72) {

            return false;
        }

        boolean maiuscola = false;
        boolean minuscola = false;
        boolean numero = false;
        boolean simbolo = false;

        for (char carattere : password.toCharArray()) {

            if (Character.isUpperCase(carattere)) {
                maiuscola = true;

            } else if (Character.isLowerCase(carattere)) {
                minuscola = true;

            } else if (Character.isDigit(carattere)) {
                numero = true;

            } else {
                simbolo = true;
            }
        }

        return maiuscola
                && minuscola
                && numero
                && simbolo;
    }

    private boolean sessioneValida(
            HttpSession session
    ) {

        if (session == null) {
            return false;
        }

        Object ruolo =
                session.getAttribute("ruolo");

        Object email =
                session.getAttribute("email");

        if (ruolo == null || email == null) {
            return false;
        }

        return "ADMIN".equals(ruolo)
                || "OPERATORE".equals(ruolo);
    }

    private void redirectErrore(
            HttpServletRequest request,
            HttpServletResponse response,
            String errore
    ) throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/cambia-password"
                        + "?errore="
                        + errore
        );
    }

    private String valorePassword(
            String valore
    ) {

        return valore == null
                ? ""
                : valore;
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

    private static class PasswordUgualeException
            extends RuntimeException {
    }
}