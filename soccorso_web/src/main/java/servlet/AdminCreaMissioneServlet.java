package servlet;

import dao.DaoInterfaceMateriale;
import dao.DaoInterfaceMezzo;
import dao.DaoInterfaceMissione;
import dao.DaoInterfaceOperatore;
import dao.DaoInterfaceRichiesta;
import dao.dao_impl.DaoInterfaceMaterialeImpl;
import dao.dao_impl.DaoInterfaceMezzoImpl;
import dao.dao_impl.DaoInterfaceMissioneImpl;
import dao.dao_impl.DaoInterfaceOperatoreImpl;
import dao.dao_impl.DaoInterfaceRichiestaImpl;
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
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Materiale;
import model.Mezzo;
import model.Missione;
import model.Operatore;
import model.Richiesta;
import model.StatoRichiesta;

@WebServlet(
        name = "AdminCreaMissioneServlet",
        urlPatterns = {"/admin/missioni/nuova"}
)
public class AdminCreaMissioneServlet extends HttpServlet {

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

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        /*
         * Il parametro si chiama ancora richiestaId per compatibilità
         * con il template esistente, ma contiene l'email primaria.
         */
        String emailRichiesta = normalizza(request.getParameter("richiestaId"))
                .toLowerCase();

        if (emailRichiesta.isBlank()) {
            redirectLista(request, response, "errore=richiesta_non_valida");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceRichiesta daoRichiesta = new DaoInterfaceRichiestaImpl(em);
            DaoInterfaceMissione daoMissione = new DaoInterfaceMissioneImpl(em);
            DaoInterfaceOperatore daoOperatore = new DaoInterfaceOperatoreImpl(em);
            DaoInterfaceMezzo daoMezzo = new DaoInterfaceMezzoImpl(em);
            DaoInterfaceMateriale daoMateriale = new DaoInterfaceMaterialeImpl(em);

            Richiesta richiesta = daoRichiesta.findByEmail(emailRichiesta);
            if (richiesta == null) {
                redirectLista(request, response, "errore=richiesta_non_trovata");
                return;
            }
            if (!StatoRichiesta.ATTIVA.equalsIgnoreCase(richiesta.getStato())) {
                redirectLista(request, response, "errore=richiesta_non_attiva");
                return;
            }
            if (daoMissione.existsByRichiesta(richiesta)) {
                redirectLista(request, response, "errore=missione_esistente");
                return;
            }

            List<Operatore> disponibili = daoOperatore.findDisponibili();
            List<Operatore> capisquadra = new ArrayList<>();
            List<Operatore> altriOperatori = new ArrayList<>();

            for (Operatore operatore : disponibili) {
                if (operatore == null || !operatore.isAttivo()) {
                    continue;
                }
                if (daoOperatore.isCaposquadra(operatore.getEmail())) {
                    capisquadra.add(operatore);
                } else {
                    altriOperatori.add(operatore);
                }
            }

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("richiestaId", richiesta.getEmail_segnalante());
            data.put("emailSegnalante", richiesta.getEmail_segnalante());
            data.put("richiestaStato", richiesta.getStato());
            data.put("richiestaDescrizione", richiesta.getDescrizione());
            data.put("richiestaPosizione", richiesta.getIndirizzo());
            data.put("capisquadraDisponibili", capisquadra);
            data.put("operatoriDisponibili", altriOperatori);
            data.put("mezziDisponibili", daoMezzo.findDisponibili());
            data.put("materialiDisponibili", daoMateriale.findDisponibili());

            String errore = normalizza(request.getParameter("errore"));
            if (!errore.isBlank()) {
                data.put("errore", errore);
            }

            renderTemplate(response, "missione.ftl", data);
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String emailRichiesta = normalizza(request.getParameter("richiestaId"))
                .toLowerCase();
        String obiettivo = normalizza(request.getParameter("obiettivo"));
        String posizione = normalizza(request.getParameter("posizione"));
        String emailCaposquadra = normalizza(request.getParameter("caposquadra"))
                .toLowerCase();

        if (emailRichiesta.isBlank() || obiettivo.isBlank() || posizione.isBlank()) {
            redirectForm(request, response, emailRichiesta, "campi");
            return;
        }
        if (emailCaposquadra.isBlank()) {
            redirectForm(request, response, emailRichiesta, "caposquadra");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceRichiesta daoRichiesta = new DaoInterfaceRichiestaImpl(em);
            DaoInterfaceMissione daoMissione = new DaoInterfaceMissioneImpl(em);
            DaoInterfaceOperatore daoOperatore = new DaoInterfaceOperatoreImpl(em);
            DaoInterfaceMezzo daoMezzo = new DaoInterfaceMezzoImpl(em);
            DaoInterfaceMateriale daoMateriale = new DaoInterfaceMaterialeImpl(em);

            Richiesta richiesta = daoRichiesta.findByEmail(emailRichiesta);
            if (richiesta == null) {
                redirectLista(request, response, "errore=richiesta_non_trovata");
                return;
            }
            if (!StatoRichiesta.ATTIVA.equalsIgnoreCase(richiesta.getStato())) {
                redirectForm(request, response, emailRichiesta, "richiesta_non_attiva");
                return;
            }
            if (daoMissione.existsByRichiesta(richiesta)) {
                redirectForm(request, response, emailRichiesta, "missione_esistente");
                return;
            }

            Operatore caposquadra = daoOperatore.findByEmail(emailCaposquadra);
            if (caposquadra == null) {
                redirectForm(request, response, emailRichiesta, "caposquadra_non_trovato");
                return;
            }
            if (!caposquadra.isAttivo()) {
                redirectForm(request, response, emailRichiesta, "caposquadra_non_attivo");
                return;
            }
            if (!daoOperatore.isCaposquadra(emailCaposquadra)) {
                redirectForm(request, response, emailRichiesta, "operatore_non_caposquadra");
                return;
            }
            if (!daoOperatore.isDisponibile(emailCaposquadra)) {
                redirectForm(request, response, emailRichiesta, "caposquadra_occupato");
                return;
            }

            List<Operatore> operatori = new ArrayList<>();
            operatori.add(caposquadra);

            List<Operatore> altri = costruisciOperatori(
                    request,
                    emailCaposquadra,
                    daoOperatore
            );
            if (altri == null) {
                redirectForm(request, response, emailRichiesta, "operatore_non_disponibile");
                return;
            }
            operatori.addAll(altri);

            List<Mezzo> mezzi = costruisciMezzi(request, daoMezzo);
            if (mezzi == null) {
                redirectForm(request, response, emailRichiesta, "mezzo_non_disponibile");
                return;
            }

            List<Materiale> materiali = costruisciMateriali(request, daoMateriale);
            if (materiali == null) {
                redirectForm(request, response, emailRichiesta, "materiale_non_disponibile");
                return;
            }

            Missione missione = daoMissione.creaMissioneCompleta(
                    richiesta,
                    "Squadra - " + obiettivo,
                    obiettivo,
                    posizione,
                    operatori,
                    mezzi,
                    materiali
            );

            MailUtil.inviaNotificaRichiestaAccettata(
                    richiesta.getEmail_segnalante(),
                    richiesta.getNome_segnalante(),
                    missione.getDescrizione()
            );

            for (Operatore operatore : missione.getOperatori()) {
                MailUtil.inviaNotificaNuovaMissione(
                        operatore.getEmail(),
                        operatore.getNome(),
                        missione.getDescrizione(),
                        missione.getPosizione()
                );
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/missioni?successo=creata"
            );

        } catch (IllegalStateException | IllegalArgumentException e) {
            redirectForm(request, response, emailRichiesta, "creazione_fallita");
        } finally {
            em.close();
        }
    }

    private List<Operatore> costruisciOperatori(
            HttpServletRequest request,
            String emailCaposquadra,
            DaoInterfaceOperatore dao) {

        List<Operatore> risultato = new ArrayList<>();
        Set<String> emailInserite = new LinkedHashSet<>();
        String[] valori = request.getParameterValues("operatori");

        if (valori == null) {
            return risultato;
        }

        for (String valore : valori) {
            String email = normalizza(valore).toLowerCase();
            if (email.isBlank() || email.equals(emailCaposquadra)) {
                continue;
            }
            if (!emailInserite.add(email)) {
                continue;
            }

            Operatore operatore = dao.findByEmail(email);
            if (operatore == null
                    || !operatore.isAttivo()
                    || !dao.isDisponibile(email)) {
                return null;
            }
            risultato.add(operatore);
        }
        return risultato;
    }

    private List<Mezzo> costruisciMezzi(
            HttpServletRequest request,
            DaoInterfaceMezzo dao) {

        List<Mezzo> risultato = new ArrayList<>();
        Set<String> targhe = new LinkedHashSet<>();
        String[] valori = request.getParameterValues("mezzi");

        if (valori == null) {
            return risultato;
        }

        for (String valore : valori) {
            String targa = normalizza(valore).toUpperCase();
            if (targa.isBlank() || !targhe.add(targa)) {
                continue;
            }
            Mezzo mezzo = dao.findByTarga(targa);
            if (mezzo == null || !dao.isDisponibile(targa)) {
                return null;
            }
            risultato.add(mezzo);
        }
        return risultato;
    }

    private List<Materiale> costruisciMateriali(
            HttpServletRequest request,
            DaoInterfaceMateriale dao) {

        List<Materiale> risultato = new ArrayList<>();
        Set<Long> ids = new LinkedHashSet<>();
        String[] valori = request.getParameterValues("materiali");

        if (valori == null) {
            return risultato;
        }

        for (String valore : valori) {
            Long id;
            try {
                id = Long.valueOf(normalizza(valore));
            } catch (NumberFormatException e) {
                return null;
            }

            if (!ids.add(id)) {
                continue;
            }

            Materiale materiale = dao.findById(id);
            if (materiale == null || !dao.isDisponibile(id)) {
                return null;
            }
            risultato.add(materiale);
        }
        return risultato;
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && "ADMIN".equals(session.getAttribute("ruolo"));
    }

    private void redirectLista(
            HttpServletRequest request,
            HttpServletResponse response,
            String query) throws IOException {
        response.sendRedirect(
                request.getContextPath() + "/admin/richieste?" + query
        );
    }

    private void redirectForm(
            HttpServletRequest request,
            HttpServletResponse response,
            String email,
            String errore) throws IOException {

        if (email == null || email.isBlank()) {
            redirectLista(request, response, "errore=" + errore);
            return;
        }

        response.sendRedirect(
                request.getContextPath()
                + "/admin/missioni/nuova?richiestaId="
                + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&errore="
                + URLEncoder.encode(errore, StandardCharsets.UTF_8)
        );
    }

    private String normalizza(String valore) {
        return valore == null ? "" : valore.trim();
    }

    private void renderTemplate(
            HttpServletResponse response,
            String templateName,
            Map<String, Object> data)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try {
            Template template = cfg.getTemplate(templateName);
            template.process(data, response.getWriter());
        } catch (Exception e) {
            throw new ServletException(
                    "Errore nel template " + templateName,
                    e
            );
        }
    }
}
