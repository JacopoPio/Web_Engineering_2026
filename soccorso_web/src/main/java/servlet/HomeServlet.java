package servlet;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet(
        name = "HomeServlet",
        urlPatterns = {"/home", ""}
)
public class HomeServlet extends HttpServlet {

    private static final int NUMERO_MASSIMO_CAPTCHA = 10;

    private Configuration cfg;
    private final SecureRandom random = new SecureRandom();

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

        cfg.setTemplateExceptionHandler(
                TemplateExceptionHandler.RETHROW_HANDLER
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        /*
         * Impedisce al browser di mostrare una vecchia pagina
         * contenente un CAPTCHA già utilizzato.
         */
        response.setHeader(
                "Cache-Control",
                "no-store, no-cache, must-revalidate, max-age=0"
        );

        response.setHeader(
                "Pragma",
                "no-cache"
        );

        response.setDateHeader(
                "Expires",
                0
        );

        int captchaA = random.nextInt(9) + 1;
        int captchaB = random.nextInt(9) + 1;

        int risultatoCaptcha =
                captchaA + captchaB;

        String captchaId =
                UUID.randomUUID().toString();

        HttpSession session =
                request.getSession(true);

        /*
         * Sincronizziamo l'accesso alla sessione perché lo stesso
         * utente potrebbe aprire la home in più schede.
         */
        synchronized (session) {

            Map<String, Integer> captchaSalvati =
                    recuperaCaptchaSalvati(session);

            /*
             * Evita di conservare un numero illimitato di CAPTCHA.
             * Viene eliminato il più vecchio.
             */
            if (captchaSalvati.size()
                    >= NUMERO_MASSIMO_CAPTCHA) {

                String primoCaptchaId =
                        captchaSalvati
                                .keySet()
                                .iterator()
                                .next();

                captchaSalvati.remove(
                        primoCaptchaId
                );
            }

            captchaSalvati.put(
                    captchaId,
                    risultatoCaptcha
            );

            session.setAttribute(
                    "captchaSalvati",
                    captchaSalvati
            );
        }

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "contextPath",
                request.getContextPath()
        );

        data.put(
                "titolo",
                "SoccorsoWeb"
        );

        data.put(
                "messaggio",
                "Portale per le richieste di soccorso"
        );

        data.put(
                "captchaA",
                captchaA
        );

        data.put(
                "captchaB",
                captchaB
        );

        data.put(
                "captchaId",
                captchaId
        );

        String errore =
                request.getParameter("errore");

        if (errore != null
                && !errore.isBlank()) {

            data.put(
                    "errore",
                    errore.trim()
            );
        }

        renderTemplate(
                response,
                "home.ftl",
                data
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Integer> recuperaCaptchaSalvati(
            HttpSession session
    ) {

        Object valore =
                session.getAttribute(
                        "captchaSalvati"
                );

        if (valore instanceof Map<?, ?>) {

            return (Map<String, Integer>) valore;
        }

        Map<String, Integer> captchaSalvati =
                new LinkedHashMap<>();

        session.setAttribute(
                "captchaSalvati",
                captchaSalvati
        );

        return captchaSalvati;
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