/*package servlet;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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

    private static final String STATO_RICHIESTA_ATTIVA = "ATTIVA";
    private static final String STATO_RICHIESTA_IN_CORSO = "IN_CORSO";
    private static final String STATO_MISSIONE_IN_CORSO = "IN_CORSO";

    private Configuration cfg;

    @Override
    public void init() throws ServletException {

        cfg = new Configuration(
                Configuration.VERSION_2_3_32
        );

        cfg.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(),
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

        EntityManager em = JPAUtil.getEntityManager();

        try {
            Richiesta richiesta = trovaPerId(
                    em,
                    Richiesta.class,
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

            if (!richiestaAttiva(richiesta)) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/richieste"
                                + "?errore=richiesta_non_attiva"
                );
                return;
            }

            if (missioneGiaPresente(em, richiesta)) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/richieste"
                                + "?errore=missione_esistente"
                );
                return;
            }

            List<Operatore> operatoriDisponibili =
                    trovaOperatoriDisponibili(em);

            List<Mezzo> mezziDisponibili =
                    trovaMezziDisponibili(em);

            List<Materiale> materialiDisponibili =
                    trovaMaterialiDisponibili(em);

            Map<String, Object> data = new HashMap<>();

            data.put(
                    "contextPath",
                    request.getContextPath()
            );

            data.put(
                    "richiestaId",
                    richiestaId
            );

            data.put(
                    "richiestaStato",
                    String.valueOf(richiesta.getStato())
            );

        
            data.put(
                    "emailSegnalante",
                    richiestaId
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
                    "mezziDisponibili",
                    mezziDisponibili
            );

            data.put(
                    "materialiDisponibili",
                    materialiDisponibili
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

        if (obiettivo.length() > 500
                || posizione.length() > 255) {

            redirectErrore(
                    request,
                    response,
                    richiestaId,
                    "campi"
            );
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        Missione missioneCreata = null;
        Operatore caposquadra = null;
        List<Operatore> membri = new ArrayList<>();

        try {
            tx.begin();

         
            Richiesta richiesta = trovaPerIdConLock(
                    em,
                    Richiesta.class,
                    richiestaId
            );

            if (richiesta == null) {
                throw new ErroreMissione(
                        "richiesta_non_trovata"
                );
            }

            if (!richiestaAttiva(richiesta)) {
                throw new ErroreMissione(
                        "richiesta_non_attiva"
                );
            }

            if (missioneGiaPresente(em, richiesta)) {
                throw new ErroreMissione(
                        "missione_esistente"
                );
            }

            caposquadra = trovaPerIdConLock(
                    em,
                    Operatore.class,
                    emailCaposquadra
            );

            if (caposquadra == null
                    || !caposquadra.isAttivo()
                    || !operatoreDisponibile(
                            em,
                            caposquadra
                    )) {

                throw new ErroreMissione(
                        "operatore_non_disponibile"
                );
            }

            
            Set<String> emailOperatoriInseriti =
                    new LinkedHashSet<>();

            String[] operatoriParam =
                    request.getParameterValues("operatori");

            if (operatoriParam != null) {

                for (String valore : operatoriParam) {

                    String email = normalizza(
                            valore
                    ).toLowerCase();

                    if (email.isBlank()) {
                        continue;
                    }

                  
                    if (email.equals(emailCaposquadra)) {
                        continue;
                    }

                    if (!emailOperatoriInseriti.add(email)) {
                        continue;
                    }

                    Operatore operatore =
                            trovaPerIdConLock(
                                    em,
                                    Operatore.class,
                                    email
                            );

                    if (operatore == null
                            || !operatore.isAttivo()
                            || !operatoreDisponibile(
                                    em,
                                    operatore
                            )) {

                        throw new ErroreMissione(
                                "operatore_non_disponibile"
                        );
                    }

                    membri.add(operatore);
                }
            }

            List<Mezzo> mezzi =
                    caricaMezziSelezionati(
                            em,
                            request
                    );

            List<Materiale> materiali =
                    caricaMaterialiSelezionati(
                            em,
                            request
                    );

            Missione missione = new Missione();

            missione.setRichiesta(richiesta);
            missione.setObiettivo(obiettivo);
            missione.setPosizione(posizione);
            missione.setDataInizio(LocalDateTime.now());
            missione.setStato(
                    STATO_MISSIONE_IN_CORSO
            );

            missione.setCaposquadra(caposquadra);
            missione.setOperatori(membri);
            missione.setMezzi(mezzi);
            missione.setMateriali(materiali);

            em.persist(missione);

          
            richiesta.setStato(
                    STATO_RICHIESTA_IN_CORSO
            );

            em.flush();
            tx.commit();

            missioneCreata = missione;

        } catch (ErroreMissione e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            redirectErrore(
                    request,
                    response,
                    richiestaId,
                    e.getCodice()
            );
            return;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw new ServletException(
                    "Errore durante la creazione della missione",
                    e
            );

        } finally {
            em.close();
        }

     
        if (missioneCreata != null
                && caposquadra != null) {

            inviaNotificaMissione(
                    caposquadra,
                    missioneCreata,
                    "CAPOSQUADRA"
            );

            for (Operatore membro : membri) {

                inviaNotificaMissione(
                        membro,
                        missioneCreata,
                        "MEMBRO"
                );
            }
        }

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/missioni"
                        + "?successo=creata"
        );
    }

    private List<Operatore> trovaOperatoriDisponibili(
            EntityManager em
    ) {

        List<Operatore> operatoriAttivi =
                em.createQuery(
                        "SELECT o "
                                + "FROM Operatore o "
                                + "WHERE o.attivo = true",
                        Operatore.class
                ).getResultList();

        List<Operatore> disponibili =
                new ArrayList<>();

        for (Operatore operatore : operatoriAttivi) {

            if (operatoreDisponibile(
                    em,
                    operatore
            )) {
                disponibili.add(operatore);
            }
        }

        return disponibili;
    }

    private List<Mezzo> trovaMezziDisponibili(
            EntityManager em
    ) {

        List<Mezzo> tutti =
                em.createQuery(
                        "SELECT m FROM Mezzo m",
                        Mezzo.class
                ).getResultList();

        List<Mezzo> disponibili =
                new ArrayList<>();

        for (Mezzo mezzo : tutti) {

            if (mezzoDisponibile(em, mezzo)) {
                disponibili.add(mezzo);
            }
        }

        return disponibili;
    }

    private List<Materiale> trovaMaterialiDisponibili(
            EntityManager em
    ) {

        List<Materiale> tutti =
                em.createQuery(
                        "SELECT m FROM Materiale m",
                        Materiale.class
                ).getResultList();

        List<Materiale> disponibili =
                new ArrayList<>();

        for (Materiale materiale : tutti) {

            if (materialeDisponibile(
                    em,
                    materiale
            )) {
                disponibili.add(materiale);
            }
        }

        return disponibili;
    }

    private boolean operatoreDisponibile(
            EntityManager em,
            Operatore operatore
    ) {

        Long numeroMissioni =
                em.createQuery(
                        "SELECT COUNT(m) "
                                + "FROM Missione m "
                                + "WHERE m.stato = :stato "
                                + "AND ("
                                + "m.caposquadra = :operatore "
                                + "OR :operatore MEMBER OF m.operatori"
                                + ")",
                        Long.class
                )
                .setParameter(
                        "stato",
                        STATO_MISSIONE_IN_CORSO
                )
                .setParameter(
                        "operatore",
                        operatore
                )
                .getSingleResult();

        return numeroMissioni == 0;
    }

    private boolean mezzoDisponibile(
            EntityManager em,
            Mezzo mezzo
    ) {

        Long numeroMissioni =
                em.createQuery(
                        "SELECT COUNT(m) "
                                + "FROM Missione m "
                                + "WHERE m.stato = :stato "
                                + "AND :mezzo MEMBER OF m.mezzi",
                        Long.class
                )
                .setParameter(
                        "stato",
                        STATO_MISSIONE_IN_CORSO
                )
                .setParameter(
                        "mezzo",
                        mezzo
                )
                .getSingleResult();

        return numeroMissioni == 0;
    }

    private boolean materialeDisponibile(
            EntityManager em,
            Materiale materiale
    ) {

        Long numeroMissioni =
                em.createQuery(
                        "SELECT COUNT(m) "
                                + "FROM Missione m "
                                + "WHERE m.stato = :stato "
                                + "AND :materiale MEMBER OF m.materiali",
                        Long.class
                )
                .setParameter(
                        "stato",
                        STATO_MISSIONE_IN_CORSO
                )
                .setParameter(
                        "materiale",
                        materiale
                )
                .getSingleResult();

        return numeroMissioni == 0;
    }

    private List<Mezzo> caricaMezziSelezionati(
            EntityManager em,
            HttpServletRequest request
    ) {

        List<Mezzo> mezzi = new ArrayList<>();
        Set<String> idInseriti = new LinkedHashSet<>();

        String[] valori =
                request.getParameterValues("mezzi");

        if (valori == null) {
            return mezzi;
        }

        for (String valore : valori) {

            String id = normalizza(valore);

            if (id.isBlank()
                    || !idInseriti.add(id)) {
                continue;
            }

            Mezzo mezzo = trovaPerIdConLock(
                    em,
                    Mezzo.class,
                    id
            );

            if (mezzo == null
                    || !mezzoDisponibile(
                            em,
                            mezzo
                    )) {

                throw new ErroreMissione(
                        "mezzo_non_disponibile"
                );
            }

            mezzi.add(mezzo);
        }

        return mezzi;
    }

    private List<Materiale> caricaMaterialiSelezionati(
            EntityManager em,
            HttpServletRequest request
    ) {

        List<Materiale> materiali =
                new ArrayList<>();

        Set<String> idInseriti =
                new LinkedHashSet<>();

        String[] valori =
                request.getParameterValues("materiali");

        if (valori == null) {
            return materiali;
        }

        for (String valore : valori) {

            String id = normalizza(valore);

            if (id.isBlank()
                    || !idInseriti.add(id)) {
                continue;
            }

            Materiale materiale =
                    trovaPerIdConLock(
                            em,
                            Materiale.class,
                            id
                    );

            if (materiale == null
                    || !materialeDisponibile(
                            em,
                            materiale
                    )) {

                throw new ErroreMissione(
                        "materiale_non_disponibile"
                );
            }

            materiali.add(materiale);
        }

        return materiali;
    }

    private boolean missioneGiaPresente(
            EntityManager em,
            Richiesta richiesta
    ) {

        Long numeroMissioni =
                em.createQuery(
                        "SELECT COUNT(m) "
                                + "FROM Missione m "
                                + "WHERE m.richiesta = :richiesta",
                        Long.class
                )
                .setParameter(
                        "richiesta",
                        richiesta
                )
                .getSingleResult();

        return numeroMissioni > 0;
    }

    private boolean richiestaAttiva(
            Richiesta richiesta
    ) {

        if (richiesta.getStato() == null) {
            return false;
        }

        return STATO_RICHIESTA_ATTIVA.equalsIgnoreCase(
                String.valueOf(
                        richiesta.getStato()
                )
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

    private Object convertiIdentificativo(
            EntityManager em,
            Class<?> entityClass,
            String valore
    ) {

        try {
            Class<?> tipoId =
                    em.getMetamodel()
                            .entity(entityClass)
                            .getIdType()
                            .getJavaType();

            if (tipoId == String.class) {
                return valore;
            }

            if (tipoId == Long.class
                    || tipoId == long.class) {
                return Long.valueOf(valore);
            }

            if (tipoId == Integer.class
                    || tipoId == int.class) {
                return Integer.valueOf(valore);
            }

            if (tipoId == Short.class
                    || tipoId == short.class) {
                return Short.valueOf(valore);
            }

            throw new IllegalArgumentException(
                    "Tipo di chiave primaria non supportato: "
                            + tipoId.getName()
            );

        } catch (RuntimeException e) {
            throw new ErroreMissione(
                    "risorsa_non_valida"
            );
        }
    }

    private <T> T trovaPerId(
            EntityManager em,
            Class<T> entityClass,
            String valore
    ) {

        Object id = convertiIdentificativo(
                em,
                entityClass,
                valore
        );

        return em.find(entityClass, id);
    }

    private <T> T trovaPerIdConLock(
            EntityManager em,
            Class<T> entityClass,
            String valore
    ) {

        Object id = convertiIdentificativo(
                em,
                entityClass,
                valore
        );

        return em.find(
                entityClass,
                id,
                LockModeType.PESSIMISTIC_WRITE
        );
    }

 
    private void inviaNotificaMissione(
            Operatore operatore,
            Missione missione,
            String ruolo
    ) {

        System.out.println(
                "===================================="
        );

        System.out.println(
                "NOTIFICA NUOVA MISSIONE"
        );

        System.out.println(
                "Destinatario: "
                        + operatore.getEmail()
        );

        System.out.println(
                "Ruolo: " + ruolo
        );

        System.out.println(
                "Missione: " + missione.getId()
        );

        System.out.println(
                "Obiettivo: "
                        + missione.getObiettivo()
        );

        System.out.println(
                "Posizione: "
                        + missione.getPosizione()
        );

        System.out.println(
                "Data inizio: "
                        + missione.getDataInizio()
        );

        System.out.println(
                "===================================="
        );
    }

    private void redirectErrore(
            HttpServletRequest request,
            HttpServletResponse response,
            String richiestaId,
            String errore
    ) throws IOException {

        String idCodificato = URLEncoder.encode(
                richiestaId == null ? "" : richiestaId,
                StandardCharsets.UTF_8
        );

        String erroreCodificato = URLEncoder.encode(
                errore,
                StandardCharsets.UTF_8
        );

        response.sendRedirect(
                request.getContextPath()
                        + "/admin/missioni/nuova"
                        + "?richiestaId="
                        + idCodificato
                        + "&errore="
                        + erroreCodificato
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
                    "Errore durante il caricamento del template "
                            + templateName,
                    e
            );
        }
    }

    private static class ErroreMissione
            extends RuntimeException {

        private final String codice;

        ErroreMissione(String codice) {
            super(codice);
            this.codice = codice;
        }

        String getCodice() {
            return codice;
        }
    }
}*/