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

    /*
     * Devono corrispondere esattamente ai valori
     * ammessi dal vincolo chk_stato nel database.
     */
    private static final String STATO_ATTIVA = "attiva";
    private static final String STATO_IN_CORSO = "in corso";

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

            /*
             * Il database salva lo stato come "attiva".
             */
            if (!STATO_ATTIVA.equalsIgnoreCase(
                    String.valueOf(
                            richiesta.getStato()
                    ).trim()
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

            List<Operatore> operatoriDisponibili =
                    daoOperatore.findDisponibili();

            List<Operatore> capisquadraDisponibili =
                    new ArrayList<>();

            for (Operatore operatore
                    : operatoriDisponibili) {

                if (operatore.isAttivo()
                        && daoOperatore.isCaposquadra(
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
                            request.getParameter("errore")
                    );

            if (!errore.isBlank()) {
                data.put("errore", errore);
            }

            renderTemplate(
                    response,
                    "missione.ftl",
                    data
            );

        } catch (RuntimeException e) {

            throw new ServletException(
                    "Errore nel caricamento "
                    + "del form della missione",
                    e
            );

        } finally {

            if (entityManager != null
                    && entityManager.isOpen()) {

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
                        request.getParameter("richiestaId")
                );

        String obiettivo =
                normalizza(
                        request.getParameter("obiettivo")
                );

        String posizione =
                normalizza(
                        request.getParameter("posizione")
                );

        String emailCaposquadra =
                normalizza(
                        request.getParameter("caposquadra")
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

            if (!STATO_ATTIVA.equalsIgnoreCase(
                    String.valueOf(
                            richiesta.getStato()
                    ).trim()
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
             * 1. Crea e salva la squadra.
             */
            Squadra nuovaSquadra =
                    new Squadra();

            nuovaSquadra.setNome(
                    "Squadra - " + obiettivo
            );

            Squadra squadraSalvata =
                    daoSquadra.save(
                            nuovaSquadra
                    );

            if (squadraSalvata == null
                    || squadraSalvata.getId() == null
                    || squadraSalvata.getId() <= 0) {

                redirectErrore(
                        request,
                        response,
                        richiestaId,
                        "squadra_non_salvata"
                );

                return;
            }

            System.out.println(
                    "Squadra salvata con ID: "
                    + squadraSalvata.getId()
            );

            /*
             * 2. Assegna la nuova squadra
             * agli operatori.
             */
            for (Operatore operatore
                    : tuttiOperatori) {

                operatore.setSquadra(
                        squadraSalvata
                );

                daoOperatore.update(
                        operatore
                );
            }

            /*
             * 3. Crea e salva la missione.
             */
            Missione missione =
                    new Missione();

            missione.setRichiesta(
                    richiesta
            );

            missione.setDescrizione(
                    obiettivo
                    + " - "
                    + posizione
            );

            missione.setMezzi(
                    mezzi
            );

            missione.setMateriali(
                    materiali
            );

            missione.setSquadra(
                    squadraSalvata
            );

            Missione missioneSalvata =
                    daoMissione.save(
                            missione
                    );

            if (missioneSalvata == null
                    || missioneSalvata.getId() == null
                    || missioneSalvata.getId() <= 0) {

                throw new IllegalStateException(
                        "La missione non è stata salvata"
                );
            }

            /*
             * Aggiorna il lato inverso in memoria.
             */
            squadraSalvata.setMissione(
                    missioneSalvata
            );

            /*
             * 4. Il valore deve essere esattamente:
             *
             * "in corso"
             *
             * perché è quello ammesso dal vincolo
             * chk_stato della tabella richiesta.
             */
            daoRichiesta.updateStato(
                    richiestaId,
                    STATO_IN_CORSO
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/missioni"
                    + "?successo=creata"
            );

        } catch (RuntimeException e) {

            throw new ServletException(
                    "Errore durante la creazione "
                    + "della missione",
                    e
            );

        } finally {

            if (entityManager != null
                    && entityManager.isOpen()) {

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
             * Evita di aggiungere nuovamente
             * il caposquadra.
             */
            if (email.equalsIgnoreCase(
                    emailCaposquadra
            )) {
                continue;
            }

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
                    cfg.getTemplate(
                            templateName
                    );

            template.process(
                    data,
                    response.getWriter()
            );

        } catch (Exception e) {

            throw new ServletException(
                    "Errore nel caricamento "
                    + "del template "
                    + templateName,
                    e
            );
        }
    }
}