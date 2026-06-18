package servlet;

import dao.DaoInterfaceAbilita;
import dao.DaoInterfaceAmministratore;
import dao.DaoInterfaceOperatore;
import dao.DaoInterfacePatente;
import dao.dao_impl.DaoInterfaceImplAbilita;
import dao.dao_impl.DaoInterfaceAmministratoreImpl;
import dao.dao_impl.DaoInterfaceOperatoreImpl;
import dao.dao_impl.DaoInterfaceImplPatente;
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
import jakarta_configuration.resources.MailUtil;
import jakarta_configuration.resources.PasswordGenerator;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Abilita;
import model.Amministratore;
import model.Operatore;
import model.Patente;

@WebServlet(name = "AdminCreaUtenteServlet", urlPatterns = {"/admin/nuovo-utente"})
public class AdminCreaUtenteServlet extends HttpServlet {

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

        HttpSession session = request.getSession(false);

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());

        if (session != null) {
            data.put("nomeAdmin", session.getAttribute("nome"));
            data.put("ruoloSessione", session.getAttribute("ruolo"));
        }

        if (request.getParameter("errore") != null) {
            data.put("errore", request.getParameter("errore"));
        }

        if (request.getParameter("successo") != null) {
            data.put("successo", true);
        }

        // --- INIZIO NUOVO CODICE ---
        // Recuperiamo le patenti dal DB per passarle al template
        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfacePatente daoPatente = new DaoInterfaceImplPatente(em);
            List<Patente> listaPatenti = daoPatente.findAll();
            data.put("listaPatenti", listaPatenti);
            DaoInterfaceAbilita daoAbilita = new DaoInterfaceImplAbilita(em);
            List<Abilita> abilita = daoAbilita.findAll();
            data.put("listaAbilita",abilita);
        } finally {
            em.close();
        }
        renderTemplate(response, "nuovo-utente.ftl", data);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String ruolo = normalizza(request.getParameter("ruolo")).toUpperCase();
        String email = normalizza(request.getParameter("email")).toLowerCase();
        String nome = normalizza(request.getParameter("nome"));
        String cognome = normalizza(request.getParameter("cognome"));
        String dataNascitaParam = normalizza(request.getParameter("data_nascita"));
        String cittaNascita = normalizza(request.getParameter("citta_nascita"));
        String cf = normalizza(request.getParameter("cf")).toUpperCase();
        String indirizzo = normalizza(request.getParameter("indirizzo"));

        if (ruolo.isBlank()
            || email.isBlank()
            || nome.isBlank()
            || cognome.isBlank()) {

                response.sendRedirect(request.getContextPath()
                + "/admin/nuovo-utente?errore=campi");
                return;
}

        if (cf.isBlank()) {
            response.sendRedirect(request.getContextPath()
            + "/admin/nuovo-utente?errore=cf_mancante");
            return;
        }

        if (!cf.matches("^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")) {
            response.sendRedirect(request.getContextPath()
            + "/admin/nuovo-utente?errore=cf_non_valido");
            return;
        }

        if (!"ADMIN".equals(ruolo) && !"OPERATORE".equals(ruolo)) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/nuovo-utente?errore=ruolo");
            return;
        }

        LocalDate dataNascita = null;

        if (!dataNascitaParam.isBlank()) {
            try {
                dataNascita = LocalDate.parse(dataNascitaParam);
            } catch (DateTimeParseException e) {
                response.sendRedirect(request.getContextPath()
                        + "/admin/nuovo-utente?errore=data");
                return;
            }
        }

        String passwordTemporanea = PasswordGenerator.generaPassword(12);

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceAmministratore daoAdmin =
                    new DaoInterfaceAmministratoreImpl(em);

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(em);

            DaoInterfacePatente daoPatente =
                    new DaoInterfaceImplPatente(em);

            DaoInterfaceAbilita daoAbilita =
                    new DaoInterfaceImplAbilita(em);

            Amministratore adminEsistente = daoAdmin.findByEmail(email);
            Operatore operatoreEsistente = daoOperatore.findByEmail(email);

            if (adminEsistente != null || operatoreEsistente != null) {
                response.sendRedirect(request.getContextPath()
                        + "/admin/nuovo-utente?errore=email");
                return;
            }

            List<Patente> listaPatenti =
                    costruisciListaPatenti(request, daoPatente);

            List<Abilita> listaAbilita =
                    costruisciListaAbilita(request, daoAbilita);

            if ("ADMIN".equals(ruolo)) {
                Amministratore admin = new Amministratore();

                admin.setEmail(email);
                admin.setNome(nome);
                admin.setCognome(cognome);
                admin.setData_nascita(dataNascita);
                admin.setCitta_nascita(cittaNascita);
                admin.setCF(cf);
                admin.setIndirizzo(indirizzo);

                /*
                 * Non fare hash manuale qui.
                 * Il PasswordConverter dell'entity farà l'hash al salvataggio.
                 */
                admin.setPassword(passwordTemporanea);

                admin.setPatenti(listaPatenti);
                admin.setAbilita(listaAbilita);

                daoAdmin.save(admin);
                
                System.out.println("PASSWORD TEMPORANEA GENERATA = " + passwordTemporanea);
                System.out.println("EMAIL DESTINATARIO = " + email);

                MailUtil.inviaCredenziali(
                        email,
                        nome,
                        email,
                        passwordTemporanea,
                        "ADMIN"
                );

                response.sendRedirect(request.getContextPath()
                        + "/admin/nuovo-utente?successo=1");
                return;
            }

            if ("OPERATORE".equals(ruolo)) {
                Operatore operatore = new Operatore();

                operatore.setEmail(email);
                operatore.setNome(nome);
                operatore.setCognome(cognome);
                operatore.setData_nascita(dataNascita);
                operatore.setCitta_nascita(cittaNascita);
                operatore.setCF(cf);
                operatore.setIndirizzo(indirizzo);

                /*
                 * Non fare hash manuale qui.
                 * Il PasswordConverter dell'entity farà l'hash al salvataggio.
                 */
                operatore.setPassword(passwordTemporanea);

                operatore.setPatenti(listaPatenti);
                operatore.setAbilita(listaAbilita);

                daoOperatore.save(operatore);

                MailUtil.inviaCredenziali(
                        email,
                        nome,
                        email,
                        passwordTemporanea,
                        "OPERATORE"
                );

                response.sendRedirect(request.getContextPath()
                        + "/admin/nuovo-utente?successo=1");
            }

        } finally {
            em.close();
        }
    }

    private List<Patente> costruisciListaPatenti(HttpServletRequest request,
                                                  DaoInterfacePatente daoPatente) {

        List<Patente> lista = new ArrayList<>();

        String[] patenti = request.getParameterValues("patenti");

        if (patenti == null) {
            return lista;
        }

        for (String p : patenti) {
            Patente patente = daoPatente.findOrCreate(p);

            if (patente != null && !lista.contains(patente)) {
                lista.add(patente);
            }
        }

        return lista;
    }

    private List<Abilita> costruisciListaAbilita(HttpServletRequest request,
                                                 DaoInterfaceAbilita daoAbilita) {

        List<Abilita> lista = new ArrayList<>();

        // Cambiato da getParameter a getParameterValues perché ora sono checkbox
        String[] abilitaArray = request.getParameterValues("abilita");

        if (abilitaArray == null) {
            return lista;
        }

        for (String a : abilitaArray) {
            // Usiamo il valore pulito se necessario, o lasciamo fare al DAO
            if (a != null && !a.isBlank()) {
                Abilita abilita = daoAbilita.findOrCreate(a.trim());

                if (abilita != null && !lista.contains(abilita)) {
                    lista.add(abilita);
                }
            }
        }

        return lista;
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