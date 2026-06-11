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
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Abilita;
import model.Amministratore;
import model.Operatore;
import model.Patente;
import jakarta_configuration.resources.MailUtil;
import jakarta_configuration.resources.PasswordGenerator;

@WebServlet(name = "AdminCreaUtenteServlet", urlPatterns = {"/admin/nuovo-utente"})
public class AdminCreaUtenteServlet extends HttpServlet {

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

        emf = Persistence.createEntityManagerFactory("SoccorsoWebPU");
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

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());

        if (request.getParameter("errore") != null) {
            data.put("errore", request.getParameter("errore"));
        }

        if (request.getParameter("successo") != null) {
            data.put("successo", true);
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

        String ruolo = request.getParameter("ruolo");
        String email = request.getParameter("email");
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String dataNascita = request.getParameter("data_nascita");
        String cittaNascita = request.getParameter("citta_nascita");
        String cf = request.getParameter("cf");
        String indirizzo = request.getParameter("indirizzo");

        if (ruolo == null || ruolo.isBlank()
                || email == null || email.isBlank()
                || nome == null || nome.isBlank()
                || cognome == null || cognome.isBlank()) {

            response.sendRedirect(request.getContextPath() + "/admin/nuovo-utente?errore=campi");
            return;
        }

        /*
         * Password temporanea in chiaro.
         * Verrà inviata via mail/console.
         * Nel DB verrà salvata hashata dal PasswordConverter.
         */
        String passwordTemporanea = PasswordGenerator.generaPassword(12);

        EntityManager em = emf.createEntityManager();

        try {
            DaoInterfaceAmministratore daoAdmin =
                    new DaoInterfaceAmministratoreImpl(em);

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(em);

            DaoInterfacePatente daoPatente =
                    new DaoInterfaceImplPatente(em);

            DaoInterfaceAbilita daoAbilita =
                    new DaoInterfaceImplAbilita(em);

            List<Patente> listaPatenti =
                    costruisciListaPatenti(request, daoPatente);

            List<Abilita> listaAbilita =
                    costruisciListaAbilita(request, daoAbilita);

            if ("ADMIN".equals(ruolo)) {

                Amministratore esistente = daoAdmin.findByEmail(email);

                if (esistente != null) {
                    response.sendRedirect(request.getContextPath() + "/admin/nuovo-utente?errore=email");
                    return;
                }

                Amministratore admin = new Amministratore();

                admin.setEmail(email);
                admin.setNome(nome);
                admin.setCognome(cognome);

                if (dataNascita != null && !dataNascita.isBlank()) {
                    admin.setData_nascita(LocalDate.parse(dataNascita));
                }

                admin.setCitta_nascita(cittaNascita);
                admin.setCF(cf);
                admin.setIndirizzo(indirizzo);

                /*
                 * Qui NON mettere hash manuale.
                 * Il converter farà l'hash automaticamente.
                 */
                admin.setPassword(passwordTemporanea);

                admin.setPatenti(listaPatenti);
                admin.setAbilita(listaAbilita);

                daoAdmin.save(admin);

                MailUtil.inviaCredenziali(
                        email,
                        nome,
                        email,
                        passwordTemporanea,
                        "ADMIN"
                );

                response.sendRedirect(request.getContextPath() + "/admin/nuovo-utente?successo=1");
                return;
            }

            if ("OPERATORE".equals(ruolo)) {

                Operatore esistente = daoOperatore.findByEmail(email);

                if (esistente != null) {
                    response.sendRedirect(request.getContextPath() + "/admin/nuovo-utente?errore=email");
                    return;
                }

                Operatore operatore = new Operatore();

                operatore.setEmail(email);
                operatore.setNome(nome);
                operatore.setCognome(cognome);

                if (dataNascita != null && !dataNascita.isBlank()) {
                    operatore.setData_nascita(LocalDate.parse(dataNascita));
                }

                operatore.setCitta_nascita(cittaNascita);
                operatore.setCF(cf);
                operatore.setIndirizzo(indirizzo);

                /*
                 * Qui NON mettere hash manuale.
                 * Il converter farà l'hash automaticamente.
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

                response.sendRedirect(request.getContextPath() + "/admin/nuovo-utente?successo=1");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/nuovo-utente?errore=ruolo");

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

            if (patente != null) {
                lista.add(patente);
            }
        }

        return lista;
    }

    private List<Abilita> costruisciListaAbilita(HttpServletRequest request,
                                                  DaoInterfaceAbilita daoAbilita) {

        List<Abilita> lista = new ArrayList<>();

        String abilitaTesto = request.getParameter("abilita");

        if (abilitaTesto == null || abilitaTesto.isBlank()) {
            return lista;
        }

        String[] abilitaArray = abilitaTesto.split(",");

        for (String a : abilitaArray) {
            Abilita abilita = daoAbilita.findOrCreate(a);

            if (abilita != null) {
                lista.add(abilita);
            }
        }

        return lista;
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

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}