/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.DaoInterfaceAbilita;
import dao.DaoInterfaceAmministratore;
import dao.DaoInterfaceOperatore;
import dao.DaoInterfacePatente;
import dao.dao_impl.DaoInterfaceAmministratoreImpl;
import dao.dao_impl.DaoInterfaceImplAbilita;
import dao.dao_impl.DaoInterfaceImplPatente;
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
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Abilita;
import model.Amministratore;
import model.Operatore;
import model.Patente;

@WebServlet(
        name = "AdminGestioneUtentiServlet",
        urlPatterns = {"/admin/utenti"}
)
public class AdminGestioneUtentiServlet extends HttpServlet {

    private Configuration cfg;

    @Override
    public void init() throws ServletException {
        cfg = new Configuration(Configuration.VERSION_2_3_32);

        cfg.setClassLoaderForTemplateLoading(
                Thread.currentThread().getContextClassLoader(),
                "/templates"
        );

        cfg.setDefaultEncoding("UTF-8");
        cfg.setOutputEncoding("UTF-8");
        cfg.setURLEscapingCharset("UTF-8");
        cfg.setTemplateExceptionHandler(
                TemplateExceptionHandler.HTML_DEBUG_HANDLER
        );
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null
                && "ADMIN".equals(session.getAttribute("ruolo"));
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

        String azione =
                normalizza(request.getParameter("azione"));

        if ("modifica".equals(azione)) {
            mostraFormModifica(request, response);
            return;
        }

        mostraElenco(request, response);
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

        String azione =
                normalizza(request.getParameter("azione"));

        switch (azione) {
            case "modificaUtente":
                modificaUtente(request, response);
                break;

            case "disattiva":
                cambiaStatoUtente(
                        request,
                        response,
                        false
                );
                break;

            case "riattiva":
                cambiaStatoUtente(
                        request,
                        response,
                        true
                );
                break;

            default:
                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/utenti?errore=azione"
                );
        }
    }

    private void mostraElenco(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceAmministratore daoAdmin =
                    new DaoInterfaceAmministratoreImpl(em);

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(em);

            Map<String, Object> data =
                    creaDatiBase(request);

            data.put(
                    "amministratori",
                    daoAdmin.findAll()
            );

            data.put(
                    "operatori",
                    daoOperatore.findAll()
            );

            String errore =
                    request.getParameter("errore");

            String successo =
                    request.getParameter("successo");

            if (errore != null) {
                data.put("errore", errore);
            }

            if (successo != null) {
                data.put("successo", successo);
            }

            renderTemplate(
                    response,
                    "gestione-utenti.ftl",
                    data
            );

        } catch (RuntimeException e) {
            throw new ServletException(
                    "Errore nel caricamento degli utenti",
                    e
            );

        } finally {
            em.close();
        }
    }

    private void mostraFormModifica(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String ruolo =
                normalizza(request.getParameter("ruolo"))
                        .toUpperCase();

        String email =
                normalizza(request.getParameter("email"))
                        .toLowerCase();

        if (!ruoloValido(ruolo) || email.isBlank()) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/utenti?errore=parametri"
            );
            return;
        }

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

            Map<String, Object> data =
                    creaDatiBase(request);

            data.put("modalita", "modifica");
            data.put("titoloPagina", "Modifica utente");
            data.put("azioneForm", "modificaUtente");
            data.put(
                    "formAction",
                    request.getContextPath()
                    + "/admin/utenti"
            );

            data.put("ruoloUtente", ruolo);

            data.put(
                    "listaPatenti",
                    daoPatente.findAll()
            );

            data.put(
                    "listaAbilita",
                    daoAbilita.findAll()
            );

            String errore =
                    request.getParameter("errore");

            if (errore != null) {
                data.put("errore", errore);
            }

            if ("ADMIN".equals(ruolo)) {
                Amministratore amministratore =
                        daoAdmin.findByEmail(email);

                if (amministratore == null) {
                    redirectUtenteNonTrovato(
                            request,
                            response
                    );
                    return;
                }

                inserisciDatiAmministratore(
                        data,
                        amministratore
                );

            } else {
                Operatore operatore =
                        daoOperatore.findByEmail(email);

                if (operatore == null) {
                    redirectUtenteNonTrovato(
                            request,
                            response
                    );
                    return;
                }

                inserisciDatiOperatore(
                        data,
                        operatore
                );
            }

            renderTemplate(
                    response,
                    "nuovo-utente.ftl",
                    data
            );

        } catch (RuntimeException e) {
            throw new ServletException(
                    "Errore nel caricamento dell'utente",
                    e
            );

        } finally {
            em.close();
        }
    }

    private void modificaUtente(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String ruolo =
                normalizza(request.getParameter("ruolo"))
                        .toUpperCase();

        String email =
                normalizza(request.getParameter("email"))
                        .toLowerCase();

        String nome =
                normalizza(request.getParameter("nome"));

        String cognome =
                normalizza(request.getParameter("cognome"));

        String cf =
                normalizza(request.getParameter("cf"))
                        .toUpperCase();

        String dataNascitaParam =
                normalizza(
                        request.getParameter("data_nascita")
                );

        String cittaNascita =
                normalizza(
                        request.getParameter("citta_nascita")
                );

        String indirizzo =
                normalizza(
                        request.getParameter("indirizzo")
                );

        if (!ruoloValido(ruolo)
                || email.isBlank()
                || nome.isBlank()
                || cognome.isBlank()
                || cf.isBlank()) {

            redirectErroreModifica(
                    request,
                    response,
                    ruolo,
                    email,
                    "campi"
            );
            return;
        }

        if (!cf.matches(
                "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}"
                + "[A-Z][0-9]{3}[A-Z]$"
        )) {
            redirectErroreModifica(
                    request,
                    response,
                    ruolo,
                    email,
                    "cf_non_valido"
            );
            return;
        }

        LocalDate dataNascita =
                parseDataNascita(dataNascitaParam);

        if (!dataNascitaParam.isBlank()
                && dataNascita == null) {

            redirectErroreModifica(
                    request,
                    response,
                    ruolo,
                    email,
                    "data"
            );
            return;
        }

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

            List<Patente> patenti;

            List<Abilita> abilita;

            try {
                patenti = costruisciListaPatenti(
                        request,
                        daoPatente
                );

                abilita = costruisciListaAbilita(
                        request,
                        daoAbilita
                );

            } catch (IllegalArgumentException e) {
                redirectErroreModifica(
                        request,
                        response,
                        ruolo,
                        email,
                        "selezione_non_valida"
                );
                return;
            }

            if ("ADMIN".equals(ruolo)) {
                Amministratore amministratore =
                        daoAdmin.findByEmail(email);

                if (amministratore == null) {
                    redirectUtenteNonTrovato(
                            request,
                            response
                    );
                    return;
                }

                amministratore.setNome(nome);
                amministratore.setCognome(cognome);
                amministratore.setCF(cf);
                amministratore.setData_nascita(dataNascita);
                amministratore.setCitta_nascita(cittaNascita);
                amministratore.setIndirizzo(indirizzo);
                amministratore.setPatenti(patenti);
                amministratore.setAbilita(abilita);

                daoAdmin.update(amministratore);

            } else {
                Operatore operatore =
                        daoOperatore.findByEmail(email);

                if (operatore == null) {
                    redirectUtenteNonTrovato(
                            request,
                            response
                    );
                    return;
                }

                operatore.setNome(nome);
                operatore.setCognome(cognome);
                operatore.setCF(cf);
                operatore.setData_nascita(dataNascita);
                operatore.setCitta_nascita(cittaNascita);
                operatore.setIndirizzo(indirizzo);
                operatore.setPatenti(patenti);
                operatore.setAbilita(abilita);

                daoOperatore.update(operatore);
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/utenti"
                    + "?successo=modificato"
            );

        } catch (RuntimeException e) {
            throw new ServletException(
                    "Errore durante la modifica dell'utente",
                    e
            );

        } finally {
            em.close();
        }
    }

    private void cambiaStatoUtente(
            HttpServletRequest request,
            HttpServletResponse response,
            boolean nuovoStato
    ) throws ServletException, IOException {

        String ruolo =
                normalizza(request.getParameter("ruolo"))
                        .toUpperCase();

        String email =
                normalizza(request.getParameter("email"))
                        .toLowerCase();

        if (!ruoloValido(ruolo) || email.isBlank()) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/utenti?errore=parametri"
            );
            return;
        }

        HttpSession session =
                request.getSession(false);

        String emailSession = session == null
                ? null
                : (String) session.getAttribute("email");

        if (!nuovoStato
                && emailSession != null
                && email.equalsIgnoreCase(emailSession)) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/utenti"
                    + "?errore=autodisattivazione"
            );
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceAmministratore daoAdmin =
                    new DaoInterfaceAmministratoreImpl(em);

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(em);

            if ("ADMIN".equals(ruolo)) {
                Amministratore amministratore =
                        daoAdmin.findByEmail(email);

                if (amministratore == null) {
                    redirectUtenteNonTrovato(
                            request,
                            response
                    );
                    return;
                }

                if (!nuovoStato
                        && amministratore.isAttivo()) {

                    Long amministratoriAttivi =
                            em.createQuery(
                                    "SELECT COUNT(a) "
                                    + "FROM Amministratore a "
                                    + "WHERE a.attivo = true",
                                    Long.class
                            ).getSingleResult();

                    if (amministratoriAttivi <= 1) {
                        response.sendRedirect(
                                request.getContextPath()
                                + "/admin/utenti"
                                + "?errore=ultimo_admin"
                        );
                        return;
                    }
                }

                amministratore.setAttivo(nuovoStato);
                daoAdmin.update(amministratore);

            } else {
                Operatore operatore =
                        daoOperatore.findByEmail(email);

                if (operatore == null) {
                    redirectUtenteNonTrovato(
                            request,
                            response
                    );
                    return;
                }

                operatore.setAttivo(nuovoStato);
                daoOperatore.update(operatore);
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/utenti"
                    + "?successo="
                    + (nuovoStato
                        ? "riattivato"
                        : "disattivato")
            );

        } catch (RuntimeException e) {
            throw new ServletException(
                    "Errore durante il cambio di stato",
                    e
            );

        } finally {
            em.close();
        }
    }

    private void inserisciDatiAmministratore(
            Map<String, Object> data,
            Amministratore amministratore
    ) {

        data.put(
                "emailUtente",
                amministratore.getEmail()
        );

        data.put(
                "nomeUtente",
                amministratore.getNome()
        );

        data.put(
                "cognomeUtente",
                amministratore.getCognome()
        );

        data.put(
                "cfUtente",
                amministratore.getCF()
        );

        data.put(
                "cittaNascitaUtente",
                amministratore.getCitta_nascita()
        );

        data.put(
                "indirizzoUtente",
                amministratore.getIndirizzo()
        );

        data.put(
                "dataNascitaUtente",
                amministratore.getData_nascita() == null
                    ? ""
                    : amministratore
                        .getData_nascita()
                        .toString()
        );

        data.put(
                "patentiSelezionate",
                estraiTipiPatenti(
                        amministratore.getPatenti()
                )
        );

        data.put(
                "abilitaSelezionate",
                estraiNomiAbilita(
                        amministratore.getAbilita()
                )
        );
    }

    private void inserisciDatiOperatore(
            Map<String, Object> data,
            Operatore operatore
    ) {

        data.put(
                "emailUtente",
                operatore.getEmail()
        );

        data.put(
                "nomeUtente",
                operatore.getNome()
        );

        data.put(
                "cognomeUtente",
                operatore.getCognome()
        );

        data.put(
                "cfUtente",
                operatore.getCF()
        );

        data.put(
                "cittaNascitaUtente",
                operatore.getCitta_nascita()
        );

        data.put(
                "indirizzoUtente",
                operatore.getIndirizzo()
        );

        data.put(
                "dataNascitaUtente",
                operatore.getData_nascita() == null
                    ? ""
                    : operatore
                        .getData_nascita()
                        .toString()
        );

        data.put(
                "patentiSelezionate",
                estraiTipiPatenti(
                        operatore.getPatenti()
                )
        );

        data.put(
                "abilitaSelezionate",
                estraiNomiAbilita(
                        operatore.getAbilita()
                )
        );
    }

    private List<String> estraiTipiPatenti(
            List<Patente> patenti
    ) {

        List<String> risultato =
                new ArrayList<>();

        if (patenti == null) {
            return risultato;
        }

        for (Patente patente : patenti) {
            risultato.add(
                    patente.getTipoPatente()
            );
        }

        return risultato;
    }

    private List<String> estraiNomiAbilita(
            List<Abilita> abilita
    ) {

        List<String> risultato =
                new ArrayList<>();

        if (abilita == null) {
            return risultato;
        }

        for (Abilita elemento : abilita) {
            risultato.add(elemento.getNome());
        }

        return risultato;
    }

    private List<Patente> costruisciListaPatenti(
            HttpServletRequest request,
            DaoInterfacePatente daoPatente
    ) {

        List<Patente> risultato =
                new ArrayList<>();

        String[] valori =
                request.getParameterValues("patenti");

        if (valori == null) {
            return risultato;
        }

        Set<String> inseriti =
                new HashSet<>();

        for (String valore : valori) {
            String tipo =
                    normalizza(valore).toUpperCase();

            if (tipo.isBlank()
                    || !inseriti.add(tipo)) {
                continue;
            }

            Patente patente =
                    daoPatente.findByTipo(tipo);

            if (patente == null) {
                throw new IllegalArgumentException(
                        "Patente inesistente: " + tipo
                );
            }

            risultato.add(patente);
        }

        return risultato;
    }

    private List<Abilita> costruisciListaAbilita(
            HttpServletRequest request,
            DaoInterfaceAbilita daoAbilita
    ) {

        List<Abilita> risultato =
                new ArrayList<>();

        String[] valori =
                request.getParameterValues("abilita");

        if (valori == null) {
            return risultato;
        }

        Set<String> inserite =
                new HashSet<>();

        for (String valore : valori) {
            String nome = normalizza(valore);

            if (nome.isBlank()) {
                continue;
            }

            String chiave = nome.toLowerCase();

            if (!inserite.add(chiave)) {
                continue;
            }

            Abilita abilita =
                    daoAbilita.findByNome(nome);

            if (abilita == null) {
                throw new IllegalArgumentException(
                        "Abilità inesistente: " + nome
                );
            }

            risultato.add(abilita);
        }

        return risultato;
    }

    private LocalDate parseDataNascita(
            String valore
    ) {

        if (valore == null || valore.isBlank()) {
            return null;
        }

        try {
            LocalDate data = LocalDate.parse(valore);

            if (data.isAfter(LocalDate.now())) {
                return null;
            }

            return data;

        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void redirectErroreModifica(
            HttpServletRequest request,
            HttpServletResponse response,
            String ruolo,
            String email,
            String errore
    ) throws IOException {

        String emailCodificata =
                URLEncoder.encode(
                        email,
                        StandardCharsets.UTF_8
                );

        response.sendRedirect(
                request.getContextPath()
                + "/admin/utenti"
                + "?azione=modifica"
                + "&ruolo=" + ruolo
                + "&email=" + emailCodificata
                + "&errore=" + errore
        );
    }

    private void redirectUtenteNonTrovato(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.sendRedirect(
                request.getContextPath()
                + "/admin/utenti"
                + "?errore=utente_non_trovato"
        );
    }

    private Map<String, Object> creaDatiBase(
            HttpServletRequest request
    ) {

        Map<String, Object> data =
                new HashMap<>();

        data.put(
                "contextPath",
                request.getContextPath()
        );

        HttpSession session =
                request.getSession(false);

        if (session != null) {
            data.put(
                    "nomeAdmin",
                    session.getAttribute("nome")
            );
        }

        return data;
    }

    private boolean ruoloValido(String ruolo) {
        return "ADMIN".equals(ruolo)
                || "OPERATORE".equals(ruolo);
    }

    private String normalizza(String valore) {
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
                    "Errore nel template " + templateName,
                    e
            );
        }
    }
}