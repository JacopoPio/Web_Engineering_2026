package servlet;

import dao.DaoInterfaceMateriale;
import dao.DaoInterfaceMezzo;
import dao.DaoInterfaceMissione;
import dao.DaoInterfaceOperatore;
import dao.DaoInterfaceRichiesta;
import dao.DaoInterfaceSquadra;

import dao.dao_impl.DaoInterfaceMaterialeImpl;
import dao.dao_impl.DaoInterfaceMezzoImpl;
import dao.dao_impl.DaoInterfaceMissioneImpl;
import dao.dao_impl.DaoInterfaceOperatoreImpl;
import dao.dao_impl.DaoInterfaceRichiestaImpl;
import dao.dao_impl.DaoInterfaceSquadraImpl;

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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

        return "ADMIN".equalsIgnoreCase(ruolo);
    }

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

        String richiestaId =
                normalizza(
                        request.getParameter(
                                "richiestaId"
                        )
                );

        if (richiestaId.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/richieste"
                            + "?errore=richiesta_non_valida"
            );

            return;
        }

        EntityManager entityManager =
                JPAUtil.getEntityManager();

        try {

            DaoInterfaceRichiesta daoRichiesta =
                    new DaoInterfaceRichiestaImpl(
                            entityManager
                    );

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(
                            entityManager
                    );

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(
                            entityManager
                    );

            DaoInterfaceMezzo daoMezzo =
                    new DaoInterfaceMezzoImpl(
                            entityManager
                    );

            DaoInterfaceMateriale daoMateriale =
                    new DaoInterfaceMaterialeImpl(
                            entityManager
                    );

            Richiesta richiesta =
                    daoRichiesta.findByEmail(
                            richiestaId
                    );

            if (richiesta == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/richieste"
                                + "?errore=richiesta_non_trovata"
                );

                return;
            }

            if (!"ATTIVA".equalsIgnoreCase(
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

            if (daoMissione.existsByRichiesta(
                    richiesta
            )) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/richieste"
                                + "?errore=missione_esistente"
                );

                return;
            }

            /*
             * Recupera tutti gli operatori disponibili.
             */
            List<Operatore> operatoriDisponibili =
                    daoOperatore.findDisponibili();

            /*
             * Tra gli operatori disponibili mantiene solamente
             * quelli abilitati come caposquadra.
             */
            List<Operatore> capisquadraDisponibili =
                    new ArrayList<>();

            for (Operatore operatore
                    : operatoriDisponibili) {

                if (daoOperatore.isCaposquadra(
                        operatore.getEmail()
                )) {

                    capisquadraDisponibili.add(
                            operatore
                    );
                }
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
                    operatoriDisponibili
            );

            data.put(
                    "capisquadraDisponibili",
                    capisquadraDisponibili
            );

            data.put(
                    "mezziDisponibili",
                    daoMezzo.findDisponibili()
            );

            data.put(
                    "materialiDisponibili",
                    daoMateriale.findDisponibili()
            );

            String errore =
                    normalizza(
                            request.getParameter(
                                    "errore"
                            )
                    );

            if (!errore.isBlank()) {

                data.put(
                        "errore",
                        errore
                );
            }

            renderTemplate(
                    response,
                    "missione.ftl",
                    data
            );

        } finally {

            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

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

        String richiestaId =
                normalizza(
                        request.getParameter(
                                "richiestaId"
                        )
                );

        String obiettivo =
                normalizza(
                        request.getParameter(
                                "obiettivo"
                        )
                );

        String posizione =
                normalizza(
                        request.getParameter(
                                "posizione"
                        )
                );

        /*
         * Non trasformare l'email in minuscolo.
         * Viene usato esattamente il valore ricevuto dal form.
         */
        String emailCaposquadra =
                normalizza(
                        request.getParameter(
                                "caposquadra"
                        )
                );

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

        EntityManager entityManager =
                JPAUtil.getEntityManager();

        try {

            DaoInterfaceRichiesta daoRichiesta =
                    new DaoInterfaceRichiestaImpl(
                            entityManager
                    );

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(
                            entityManager
                    );

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(
                            entityManager
                    );

            DaoInterfaceMezzo daoMezzo =
                    new DaoInterfaceMezzoImpl(
                            entityManager
                    );

            DaoInterfaceMateriale daoMateriale =
                    new DaoInterfaceMaterialeImpl(
                            entityManager
                    );

            DaoInterfaceSquadra daoSquadra =
                    new DaoInterfaceSquadraImpl(
                            entityManager
                    );

            Richiesta richiesta =
                    daoRichiesta.findByEmail(
                            richiestaId
                    );

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

            if (daoMissione.existsByRichiesta(
                    richiesta
            )) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "missione_esistente"
                );

                return;
            }

            /*
             * Recupera il caposquadra.
             */
            Operatore caposquadra =
                    daoOperatore.findByEmail(
                            emailCaposquadra
                    );

            if (caposquadra == null) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "caposquadra_non_trovato"
                );

                return;
            }

            if (!caposquadra.isAttivo()) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "caposquadra_non_attivo"
                );

                return;
            }

            if (!daoOperatore.isDisponibile(
                    emailCaposquadra
            )) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "caposquadra_occupato"
                );

                return;
            }

            if (!daoOperatore.isCaposquadra(
                    emailCaposquadra
            )) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "operatore_non_caposquadra"
                );

                return;
            }

            /*
             * Recupera gli altri operatori.
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

            List<Operatore> tuttiOperatori =
                    new ArrayList<>();

            tuttiOperatori.add(
                    caposquadra
            );

            tuttiOperatori.addAll(
                    altriOperatori
            );

            /*
             * Recupera i mezzi.
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
             * Recupera i materiali.
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

            /*
             * Crea la squadra.
             *
             * IMPORTANTE: non collegarla ancora alla missione.
             * La missione non esiste ancora nel database
             * (è "transient"), quindi se la squadra la
             * referenziasse già, Hibernate lancerebbe
             * TransientPropertyValueException al momento
             * del salvataggio.
             */
            Squadra squadra =
                    new Squadra();

            squadra.setNome(
                    "Squadra - " + obiettivo
            );

            squadra.setOperatori(
                    tuttiOperatori
            );

            /*
             * IMPORTANTE: entityManager.merge() non modifica
             * l'oggetto passato, ma restituisce una NUOVA
             * istanza gestita con l'id generato già impostato.
             * Bisogna quindi ricatturare il valore di ritorno,
             * altrimenti la variabile "squadra" continuerebbe
             * a puntare a un oggetto con id=0, che Hibernate
             * considera ancora "non salvato".
             */
            squadra = daoSquadra.save(
                    squadra
            );

            /*
             * Ora che la squadra ha un id valido, possiamo
             * assegnarla agli operatori e salvare la modifica.
             */
            for (Operatore operatore
                    : tuttiOperatori) {

                operatore.setSquadra(
                        squadra
                );

                daoOperatore.update(
                        operatore
                );
            }

            /*
             * Crea la missione e collegala alla squadra,
             * che a questo punto è già persistita
             * e ha un id valido.
             */
            Missione missione =
                    new Missione();

            missione.setRichiesta(
                    richiesta
            );

            missione.setDescrizione(
                    obiettivo + " - " + posizione
            );

            missione.setMezzi(
                    mezzi
            );

            missione.setMateriali(
                    materiali
            );

            missione.setSquadra(
                    squadra
            );

            /*
             * Stesso discorso fatto per la squadra:
             * cattura il valore di ritorno di save().
             */
            missione = daoMissione.save(
                    missione
            );

            /*
             * Aggiorna anche il lato inverso in memoria.
             * Non scrive nulla nel database (il campo
             * Squadra.missione è mappedBy), serve solo
             * per la consistenza dell'oggetto squadra.
             */
            squadra.setMissione(
                    missione
            );

            /*
             * La richiesta passa allo stato IN_CORSO.
             */
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

            if (entityManager.isOpen()) {
                entityManager.close();
            }
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
                    normalizza(valore);

            if (email.isBlank()) {
                continue;
            }

            /*
             * Evita di inserire nuovamente il caposquadra.
             */
            if (email.equalsIgnoreCase(
                    emailCaposquadra
            )) {
                continue;
            }

            /*
             * Usa il minuscolo solamente per controllare
             * eventuali duplicati.
             */
            String emailConfronto =
                    email.toLowerCase();

            if (!emailInserite.add(
                    emailConfronto
            )) {
                continue;
            }

            Operatore operatore =
                    daoOperatore.findByEmail(
                            email
                    );

            if (operatore == null
                    || !operatore.isAttivo()
                    || !daoOperatore.isDisponibile(
                            email
                    )) {

                return null;
            }

            risultato.add(
                    operatore
            );
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
                request.getParameterValues(
                        "mezzi"
                );

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
                    daoMezzo.findByTarga(
                            targa
                    );

            if (mezzo == null
                    || !daoMezzo.isDisponibile(
                            targa
                    )) {

                return null;
            }

            risultato.add(
                    mezzo
            );
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

                id = Long.valueOf(
                        idTesto
                );

            } catch (NumberFormatException e) {

                return null;
            }

            if (!idInseriti.add(id)) {
                continue;
            }

            Materiale materiale =
                    daoMateriale.findById(
                            id
                    );

            if (materiale == null
                    || !daoMateriale.isDisponibile(
                            id
                    )) {

                return null;
            }

            risultato.add(
                    materiale
            );
        }

        return risultato;
    }

    private void redirectErrore(
            HttpServletRequest request,
            HttpServletResponse response,
            String richiestaId,
            String errore
    ) throws IOException {

        String richiestaCodificata =
                URLEncoder.encode(
                        richiestaId,
                        StandardCharsets.UTF_8
                );

        String erroreCodificato =
                URLEncoder.encode(
                        errore,
                        StandardCharsets.UTF_8
                );

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/missioni/nuova"
                        + "?richiestaId="
                        + richiestaCodificata
                        + "&errore="
                        + erroreCodificato
        );
    }

    private String normalizza(
            String valore
    ) {

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
                    cfg.getTemplate(
                            templateName
                    );

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
