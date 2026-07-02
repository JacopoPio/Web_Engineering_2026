package servlet;

import dao.DaoInterfaceAbilita;
import dao.DaoInterfaceMissione;
import dao.DaoInterfaceOperatore;
import dao.DaoInterfacePatente;
import dao.dao_impl.DaoInterfaceImplAbilita;
import dao.dao_impl.DaoInterfaceImplPatente;
import dao.dao_impl.DaoInterfaceMissioneImpl;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Abilita;
import model.Aggiornamento;
import model.Missione;
import model.Operatore;
import model.Patente;

@WebServlet(
        name = "OperatoreServlet",
        urlPatterns = {"/operatori", "/operatore"}
)
public class OperatoreServlet extends HttpServlet {

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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

        HttpSession session = sessioneOperatore(request, response);
        if (session == null) {
            return;
        }

        String email = String.valueOf(session.getAttribute("email"));
        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceOperatore daoOperatore = new DaoInterfaceOperatoreImpl(em);
            DaoInterfaceMissione daoMissione = new DaoInterfaceMissioneImpl(em);
            DaoInterfacePatente daoPatente = new DaoInterfaceImplPatente(em);
            DaoInterfaceAbilita daoAbilita = new DaoInterfaceImplAbilita(em);

            Operatore operatore = daoOperatore.findByEmail(email);
            if (operatore == null || !operatore.isAttivo()) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login?errore=1");
                return;
            }

            inizializzaProfilo(operatore);
            List<Missione> missioni = daoMissione.findByOperatore(email);

            Set<String> patentiSelezionate = new LinkedHashSet<>();
            for (Patente patente : operatore.getPatenti()) {
                patentiSelezionate.add(patente.getTipoPatente());
            }

            Set<String> abilitaSelezionate = new LinkedHashSet<>();
            for (Abilita abilita : operatore.getAbilita()) {
                abilitaSelezionate.add(abilita.getNome());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("nome", operatore.getNome());
            data.put("ruolo", "OPERATORE");
            data.put("operatore", operatore);
            data.put("dataNascita", operatore.getData_nascita() == null
                    ? ""
                    : operatore.getData_nascita().toString());
            data.put("listaPatenti", daoPatente.findAll());
            data.put("listaAbilita", daoAbilita.findAll());
            data.put("patentiSelezionate", new ArrayList<>(patentiSelezionate));
            data.put("abilitaSelezionate", new ArrayList<>(abilitaSelezionate));
            data.put("missioni", costruisciVistaMissioni(missioni));

            String successo = pulisci(request.getParameter("successo"));
            String errore = pulisci(request.getParameter("errore"));
            if (!successo.isBlank()) {
                data.put("successo", successo);
            }
            if (!errore.isBlank()) {
                data.put("errore", errore);
            }

            renderTemplate(response, "operatori.ftl", data);
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = sessioneOperatore(request, response);
        if (session == null) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String nome = pulisci(request.getParameter("nome"));
        String cognome = pulisci(request.getParameter("cognome"));
        String cittaNascita = pulisci(request.getParameter("citta_nascita"));
        String indirizzo = pulisci(request.getParameter("indirizzo"));
        String dataNascitaTesto = pulisci(request.getParameter("data_nascita"));

        if (nome.isBlank() || cognome.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/operatori?errore=campi");
            return;
        }

        LocalDate dataNascita = null;
        if (!dataNascitaTesto.isBlank()) {
            try {
                dataNascita = LocalDate.parse(dataNascitaTesto);
                if (dataNascita.isAfter(LocalDate.now())) {
                    throw new DateTimeParseException(
                            "Data futura",
                            dataNascitaTesto,
                            0
                    );
                }
            } catch (DateTimeParseException e) {
                response.sendRedirect(request.getContextPath() + "/operatori?errore=data");
                return;
            }
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceOperatore daoOperatore = new DaoInterfaceOperatoreImpl(em);
            DaoInterfacePatente daoPatente = new DaoInterfaceImplPatente(em);
            DaoInterfaceAbilita daoAbilita = new DaoInterfaceImplAbilita(em);

            Operatore operatore = daoOperatore.findByEmail(
                    String.valueOf(session.getAttribute("email"))
            );

            if (operatore == null || !operatore.isAttivo()) {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login?errore=1");
                return;
            }

            List<Patente> patenti = new ArrayList<>();
            String[] patentiParam = request.getParameterValues("patenti");
            if (patentiParam != null) {
                for (String tipo : new LinkedHashSet<>(List.of(patentiParam))) {
                    Patente patente = daoPatente.findByTipo(tipo);
                    if (patente == null) {
                        response.sendRedirect(
                                request.getContextPath() + "/operatori?errore=selezione"
                        );
                        return;
                    }
                    patenti.add(patente);
                }
            }

            List<Abilita> abilita = new ArrayList<>();
            String[] abilitaParam = request.getParameterValues("abilita");
            if (abilitaParam != null) {
                for (String nomeAbilita : new LinkedHashSet<>(List.of(abilitaParam))) {
                    Abilita voce = daoAbilita.findByNome(nomeAbilita);
                    if (voce == null) {
                        response.sendRedirect(
                                request.getContextPath() + "/operatori?errore=selezione"
                        );
                        return;
                    }
                    abilita.add(voce);
                }
            }

            operatore.setNome(nome);
            operatore.setCognome(cognome);
            if (dataNascita != null) {
                operatore.setData_nascita(dataNascita);
            }
            operatore.setCitta_nascita(cittaNascita.isBlank() ? null : cittaNascita);
            operatore.setIndirizzo(indirizzo.isBlank() ? null : indirizzo);
            operatore.setPatenti(patenti);
            operatore.setAbilita(abilita);

            daoOperatore.update(operatore);

            session.setAttribute("nome", nome);
            response.sendRedirect(request.getContextPath() + "/operatori?successo=profilo");
        } finally {
            em.close();
        }
    }

    private HttpSession sessioneOperatore(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"OPERATORE".equals(session.getAttribute("ruolo"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return session;
    }

    private void inizializzaProfilo(Operatore operatore) {
        operatore.getPatenti().size();
        operatore.getAbilita().size();
        if (operatore.getSquadra() != null) {
            operatore.getSquadra().getNome();
        }
    }

    private List<Map<String, Object>> costruisciVistaMissioni(
            List<Missione> missioni) {

        List<Map<String, Object>> vista = new ArrayList<>();

        for (Missione missione : missioni) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", missione.getId());
            item.put("descrizione", missione.getDescrizione());
            item.put("posizione", missione.getPosizione());
            item.put("stato", missione.getRichiesta().getStato());
            item.put("dataInizio", missione.getDataInizio() == null
                    ? "-"
                    : missione.getDataInizio().format(FORMATO));
            item.put("dataFine", missione.getDataFine() == null
                    ? "-"
                    : missione.getDataFine().format(FORMATO));
            item.put("successo", missione.getSuccesso());
            item.put("commentoFinale", missione.getCommentoFinale());

            List<Aggiornamento> ordinati =
                    new ArrayList<>(missione.getAggiornamenti());
            ordinati.sort(Comparator.comparing(
                    Aggiornamento::getData_update,
                    Comparator.nullsLast(Comparator.naturalOrder())
            ));

            List<Map<String, Object>> aggiornamenti = new ArrayList<>();
            for (Aggiornamento aggiornamento : ordinati) {
                Map<String, Object> aggiornamentoVista = new HashMap<>();
                aggiornamentoVista.put("descrizione", aggiornamento.getDescrizione());
                aggiornamentoVista.put("data", aggiornamento.getData_update() == null
                        ? "-"
                        : aggiornamento.getData_update().format(FORMATO));
                aggiornamenti.add(aggiornamentoVista);
            }
            item.put("aggiornamenti", aggiornamenti);
            vista.add(item);
        }

        return vista;
    }

    private String pulisci(String valore) {
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
            throw new ServletException("Errore nel template " + templateName, e);
        }
    }
}
