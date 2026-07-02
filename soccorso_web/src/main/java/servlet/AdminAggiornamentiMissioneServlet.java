package servlet;

import dao.DaoInterfaceMissione;
import dao.dao_impl.DaoInterfaceMissioneImpl;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Aggiornamento;
import model.Missione;
import model.Operatore;
import model.StatoRichiesta;

@WebServlet(name = "AdminAggiornamentiMissioneServlet", urlPatterns = {"/admin/missioni/aggiornamenti"})
public class AdminAggiornamentiMissioneServlet extends HttpServlet {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
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

        Integer id = parseId(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/missioni");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceMissione dao = new DaoInterfaceMissioneImpl(em);
            Missione missione = dao.findById(id);
            if (missione == null) {
                response.sendRedirect(request.getContextPath() + "/admin/missioni");
                return;
            }

            List<Aggiornamento> aggiornamenti = new ArrayList<>(missione.getAggiornamenti());
            aggiornamenti.sort(Comparator.comparing(
                    Aggiornamento::getData_update,
                    Comparator.nullsLast(Comparator.reverseOrder())
            ));

            List<Map<String, Object>> vista = new ArrayList<>();
            for (Aggiornamento aggiornamento : aggiornamenti) {
                Map<String, Object> item = new HashMap<>();
                item.put("descrizione", aggiornamento.getDescrizione());
                item.put("dataFormattata", aggiornamento.getData_update() == null
                        ? "-" : aggiornamento.getData_update().format(FORMATO));
                vista.add(item);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.getContextPath());
            data.put("missione", missione);
            data.put("aggiornamenti", vista);
            data.put("missioneChiusa", StatoRichiesta.CHIUSA.equalsIgnoreCase(missione.getRichiesta().getStato()));
            if (request.getParameter("errore") != null) {
                data.put("errore", request.getParameter("errore"));
            }

            renderTemplate(response, "aggiornamenti-missione.ftl", data);
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

        Integer id = parseId(request.getParameter("id"));
        String testo = request.getParameter("descrizione");
        if (id == null || testo == null || testo.isBlank()) {
            redirectErrore(request, response, id, "Il testo non può essere vuoto");
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceMissione dao = new DaoInterfaceMissioneImpl(em);
            Missione missioneCorrente = dao.findById(id);

            if (missioneCorrente == null) {
                response.sendRedirect(request.getContextPath() + "/admin/missioni");
                return;
            }
            if (StatoRichiesta.CHIUSA.equalsIgnoreCase(missioneCorrente.getRichiesta().getStato())) {
                redirectErrore(request, response, id, "La missione è chiusa");
                return;
            }

            Aggiornamento aggiornamento = new Aggiornamento();
            aggiornamento.setDescrizione(testo.trim());
            aggiornamento.setData_update(LocalDateTime.now());

            Missione missione = dao.aggiungiAggiornamento(id, aggiornamento);
            for (Operatore operatore : missione.getOperatori()) {
                MailUtil.inviaNotificaAggiornamento(
                        operatore.getEmail(),
                        operatore.getNome(),
                        missione.getDescrizione(),
                        aggiornamento.getDescrizione()
                );
            }

            response.sendRedirect(request.getContextPath() + "/admin/missioni/aggiornamenti?id=" + id);
        } catch (IllegalStateException e) {
            redirectErrore(request, response, id, e.getMessage());
        } finally {
            em.close();
        }
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && "ADMIN".equals(session.getAttribute("ruolo"));
    }

    private Integer parseId(String valore) {
        try {
            return Integer.valueOf(valore);
        } catch (Exception e) {
            return null;
        }
    }

    private void redirectErrore(HttpServletRequest request,
                                HttpServletResponse response,
                                Integer id,
                                String errore) throws IOException {
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/missioni");
            return;
        }
        response.sendRedirect(
                request.getContextPath() + "/admin/missioni/aggiornamenti?id=" + id
                + "&errore=" + URLEncoder.encode(errore, StandardCharsets.UTF_8)
        );
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
            throw new ServletException("Errore nel template " + templateName, e);
        }
    }
}
