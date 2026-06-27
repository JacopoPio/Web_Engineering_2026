package servlet;

import dao.DaoInterfaceMateriale;
import dao.DaoInterfaceMezzo;
import dao.DaoInterfaceMissione;
import dao.DaoInterfaceOperatore;
import dao.DaoInterfaceRichiesta;
import dao.DaoInterfaceSquadra; // NB: da creare se non esiste già

import dao.dao_impl.DaoInterfaceMaterialeImpl;
import dao.dao_impl.DaoInterfaceMezzoImpl;
import dao.dao_impl.DaoInterfaceMissioneImpl;
import dao.dao_impl.DaoInterfaceRichiestaImpl;
import dao.dao_impl.DaoInterfaceOperatoreImpl;
import dao.dao_impl.DaoInterfaceSquadraImpl; // NB: da creare se non esiste già

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

import model.Materiale;
import model.Mezzo;
import model.Missione;
import model.Operatore;
import model.Richiesta;
import model.Squadra;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet(
        name = "AdminCreaMissioneServlet",
        urlPatterns = {"/admin/missioni/nuova"}
)
public class AdminCreaMissioneServlet extends HttpServlet {

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

        cfg.setTemplateExceptionHandler(
                TemplateExceptionHandler.HTML_DEBUG_HANDLER
        );
    }

    private boolean isAdmin(
            HttpServletRequest request
    ) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return false;
        }

        String ruolo =
                (String) session.getAttribute("ruolo");

        return "ADMIN".equals(ruolo);
    }

    /*
     * Mostra il form nuova-missione.ftl.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        String richiestaId = normalizza(
                request.getParameter("richiestaId")
        );

        if (richiestaId.isBlank()) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/richieste"
                            + "?errore=richiesta_non_valida"
            );
            return;
        }

        EntityManager em =
                JPAUtil.getEntityManager();

        try {

            DaoInterfaceRichiesta daoRichiesta =
                    new DaoInterfaceRichiestaImpl(em);

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(em);

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(em);

            DaoInterfaceMezzo daoMezzo =
                    new DaoInterfaceMezzoImpl(em);

            DaoInterfaceMateriale daoMateriale =
                    new DaoInterfaceMaterialeImpl(em);

            Richiesta richiesta =
                    daoRichiesta.findByEmail(richiestaId);

            if (richiesta == null) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/richieste"
                                + "?errore=richiesta_non_trovata"
                );
                return;
            }

            if (!"attiva".equalsIgnoreCase(
                    String.valueOf(
                            richiesta.getStato()
                    )
            )) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/richieste"
                                + "?errore=richiesta_non_attiva"
                );
                return;
            }

            if (daoMissione.existsByRichiesta(richiesta)) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/richieste"
                                + "?errore=missione_esistente"
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
                    "richiestaId",
                    richiestaId
            );

            data.put(
                    "emailSegnalante",
                    richiestaId
            );

            data.put(
                    "richiestaStato",
                    richiesta.getStato()
            );

            data.put(
                    "richiestaDescrizione",
                    richiesta.getDescrizione()
            );

            data.put(
                    "richiestaPosizione",
                    richiesta.getIndirizzo()
            );

            data.put(
                    "operatoriDisponibili",
                    daoOperatore.findDisponibili()
            );

            data.put(
                    "mezziDisponibili",
                    daoMezzo.findDisponibili()
            );

            data.put(
                    "materialiDisponibili",
                    daoMateriale.findDisponibili()
            );

            String errore = normalizza(
                    request.getParameter("errore")
            );

            if (!errore.isBlank()) {
                data.put("errore", errore);
            }

            renderTemplate(
                    response,
                    "nuova-missione.ftl",
                    data
            );

        } finally {
            em.close();
        }
    }

    /*
     * Crea la missione e la squadra usando solamente i DAO.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String richiestaId = normalizza(
                request.getParameter("richiestaId")
        );

        String obiettivo = normalizza(
                request.getParameter("obiettivo")
        );

        String posizione = normalizza(
                request.getParameter("posizione")
        );

        String emailCaposquadra = normalizza(
                request.getParameter("caposquadra")
        ).toLowerCase();

        if (richiestaId.isBlank()
                || obiettivo.isBlank()
                || posizione.isBlank()) {

            redirectErrore(
                    request,
                    response,
                    richiestaId,
                    "campi"
            );
            return;
        }

        if (emailCaposquadra.isBlank()) {

            redirectErrore(
                    request,
                    response,
                    richiestaId,
                    "caposquadra"
            );
            return;
        }

        EntityManager em =
                JPAUtil.getEntityManager();

        try {

            DaoInterfaceRichiesta daoRichiesta =
                    new DaoInterfaceRichiestaImpl(em);

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(em);

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(em);

            DaoInterfaceMezzo daoMezzo =
                    new DaoInterfaceMezzoImpl(em);

            DaoInterfaceMateriale daoMateriale =
                    new DaoInterfaceMaterialeImpl(em);

            DaoInterfaceSquadra daoSquadra =
                    new DaoInterfaceSquadraImpl(em);

            Richiesta richiesta =
                    daoRichiesta.findByEmail(richiestaId);

            if (richiesta == null) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "richiesta_non_trovata"
                );
                return;
            }

            if (!"ATTIVA".equalsIgnoreCase(
                    String.valueOf(
                            richiesta.getStato()
                    )
            )) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "richiesta_non_attiva"
                );
                return;
            }

            if (daoMissione.existsByRichiesta(richiesta)) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "missione_esistente"
                );
                return;
            }

            /*
             * Caposquadra.
             */
            Operatore caposquadra =
                    daoOperatore.findByEmail(
                            emailCaposquadra
                    );

            if (caposquadra == null
                    || !caposquadra.isAttivo()
                    || !daoOperatore.isCaposquadra(
                            emailCaposquadra
                    )) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "operatore_non_disponibile"
                );
                return;
            }

            /*
             * Altri operatori (non includono ancora il caposquadra).
             */
            List<Operatore> altriOperatori =
                    costruisciListaOperatori(
                            request,
                            emailCaposquadra,
                            daoOperatore
                    );

            if (altriOperatori == null) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "operatore_non_disponibile"
                );
                return;
            }

            // Squadra.operatori è una lista unica: ci metto dentro
            // sia il caposquadra che gli altri operatori.
            List<Operatore> tuttiOperatori =
                    new ArrayList<>();

            tuttiOperatori.add(caposquadra);
            tuttiOperatori.addAll(altriOperatori);

            /*
             * Mezzi.
             */
            List<Mezzo> mezzi =
                    costruisciListaMezzi(
                            request,
                            daoMezzo
                    );

            if (mezzi == null) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "mezzo_non_disponibile"
                );
                return;
            }

            /*
             * Materiali.
             */
            List<Materiale> materiali =
                    costruisciListaMateriali(
                            request,
                            daoMateriale
                    );

            if (materiali == null) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "materiale_non_disponibile"
                );
                return;
            }

            // Creo la Squadra (nome generato automaticamente,
            // dato che il form non raccoglie ancora questo dato).
            Squadra squadra = new Squadra();
            squadra.setNome("Squadra - " + obiettivo);
            squadra.setOperatori(tuttiOperatori);

            Missione missione =
                    new Missione();

            missione.setRichiesta(richiesta);

            missione.setDescrizione(
                    obiettivo + " — " + posizione
            );

            missione.setMezzi(mezzi);
            missione.setMateriali(materiali);

            missione.setSquadra(squadra);
            squadra.setMissione(missione);

            // Missione è il lato owner della relazione con Squadra
            // (ha la @JoinColumn "missione"), quindi salvo Squadra
            // prima, per avere già il suo id pronto per il join.
            daoSquadra.save(squadra);
            daoMissione.save(missione);

            daoRichiesta.updateStato(
                    richiestaId,
                    "IN_CORSO"
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/missioni"
                            + "?successo=creata"
            );

        } finally {
            em.close();
        }
    }

    private List<Operatore> costruisciListaOperatori(
            HttpServletRequest request,
            String emailCaposquadra,
            DaoInterfaceOperatore daoOperatore
    ) {

        List<Operatore> risultato =
                new ArrayList<>();

        Set<String> emailInserite =
                new LinkedHashSet<>();

        String[] valori =
                request.getParameterValues(
                        "operatori"
                );

        if (valori == null) {
            return risultato;
        }

        for (String valore : valori) {

            String email =
                    normalizza(valore)
                            .toLowerCase();

            if (email.isBlank()) {
                continue;
            }

            if (email.equals(emailCaposquadra)) {
                continue;
            }

            if (!emailInserite.add(email)) {
                continue;
            }

            Operatore operatore =
                    daoOperatore.findByEmail(email);

            if (operatore == null
                    || !operatore.isAttivo()
                    || !daoOperatore.isDisponibile(
                            email
                    )) {

                return null;
            }

            risultato.add(operatore);
        }

        return risultato;
    }

    private List<Mezzo> costruisciListaMezzi(
            HttpServletRequest request,
            DaoInterfaceMezzo daoMezzo
    ) {

        List<Mezzo> risultato =
                new ArrayList<>();

        Set<String> targheInserite =
                new LinkedHashSet<>();

        String[] valori =
                request.getParameterValues("mezzi");

        if (valori == null) {
            return risultato;
        }

        for (String valore : valori) {

            String targa =
                    normalizza(valore);

            if (targa.isBlank()) {
                continue;
            }

            if (!targheInserite.add(targa)) {
                continue;
            }

            Mezzo mezzo =
                    daoMezzo.findByTarga(targa);

            if (mezzo == null
                    || !daoMezzo.isDisponibile(targa)) {

                return null;
            }

            risultato.add(mezzo);
        }

        return risultato;
    }

    private List<Materiale> costruisciListaMateriali(
            HttpServletRequest request,
            DaoInterfaceMateriale daoMateriale
    ) {

        List<Materiale> risultato =
                new ArrayList<>();

        Set<Long> idInseriti =
                new LinkedHashSet<>();

        String[] valori =
                request.getParameterValues(
                        "materiali"
                );

        if (valori == null) {
            return risultato;
        }

        for (String valore : valori) {

            String idTesto =
                    normalizza(valore);

            if (idTesto.isBlank()) {
                continue;
            }

            Long id;

            try {
                id = Long.valueOf(idTesto);
            } catch (NumberFormatException e) {
                return null;
            }

            if (!idInseriti.add(id)) {
                continue;
            }

            Materiale materiale =
                    daoMateriale.findById(id);

            if (materiale == null
                    || !daoMateriale.isDisponibile(id)) {

                return null;
            }

            risultato.add(materiale);
        }

        return risultato;
    }

    private void redirectErrore(
            HttpServletRequest request,
            HttpServletResponse response,
            String richiestaId,
            String errore
    ) throws IOException {

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/missioni/nuova"
                        + "?richiestaId="
                        + richiestaId
                        + "&errore="
                        + errore
        );
    }

    private String normalizza(String valore) {

        if (valore == null) {
            return "";
        }

        return valore.trim();
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
                    "Errore nel caricamento del template "
                            + templateName,
                    e
            );
        }
    }
}