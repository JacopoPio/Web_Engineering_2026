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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

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

    private void renderTemplate(HttpServletResponse response, String templateName, Map<String, Object> data)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try {
            Template template = cfg.getTemplate(templateName);
            template.process(data, response.getWriter());
        } catch (Exception e) {
            throw new ServletException("Errore durante il caricamento del template: " + templateName, e);
        }
    }

    private void mostraAccessoNegato(HttpServletRequest request,
                                     HttpServletResponse response,
                                     String messaggio)
            throws ServletException, IOException {

        Map<String, Object> data = new HashMap<>();

        data.put("contextPath", request.getContextPath());
        data.put("pageTitle", "Accesso negato");
        data.put("titolo", "Accesso negato");
        data.put("messaggio", messaggio);

        renderTemplate(response, "accesso-negato.ftl", data);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        /*
         * 1. Controllo se l'utente ha una sessione valida.
         * Se non ha fatto login, non può entrare nell'area admin.
         */
        if (session == null || session.getAttribute("ruolo") == null) {
            mostraAccessoNegato(
                    request,
                    response,
                    "Devi effettuare il login per accedere all'area amministratore."
            );
            return;
        }

        /*
         * 2. Controllo il ruolo.
         * Solo chi ha ruolo ADMIN può entrare.
         */
        String ruolo = session.getAttribute("ruolo").toString();

        if (!"ADMIN".equalsIgnoreCase(ruolo)) {
            mostraAccessoNegato(
                    request,
                    response,
                    "Non hai i permessi per accedere all'area amministratore."
            );
            return;
        }

        /*
         * 3. Recupero il nome dell'utente dalla sessione.
         */
        String nome = "Amministratore";

        Object nomeSessione = session.getAttribute("nome");

        if (nomeSessione != null) {
            nome = nomeSessione.toString();
        }

        /*
         * 4. Preparo i dati da passare al template admin.ftl.
         */
        Map<String, Object> data = new HashMap<>();

        data.put("contextPath", request.getContextPath());
        data.put("pageTitle", "Area Amministratore");
        data.put("titolo", "Area Amministratore");

        data.put("nome", nome);
        data.put("ruolo", ruolo);

        /*
         * Per ora sono valori fissi.
        */
        
        data.put("richiesteAttive", 0);
        data.put("richiesteInCorso", 0);
        data.put("richiesteChiuse", 0);
        data.put("operatoriDisponibili", 0);
        data.put("mezziDisponibili", 0);
        data.put("materialiDisponibili", 0);

        /*
         * 5. Mostro la dashboard admin.
         */
        renderTemplate(response, "admin.ftl", data);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet area amministratore";
    }
}