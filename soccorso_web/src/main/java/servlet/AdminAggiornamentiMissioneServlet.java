package servlet;

import dao.DaoInterfaceMissione;
import dao.dao_impl.DaoInterfaceMissioneImpl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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

import model.Aggiornamento;
import model.Missione;
import model.Operatore;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(
        name = "AdminAggiornamentiMissioneServlet",
        urlPatterns = {"/admin/missioni/aggiornamenti"}
)
public class AdminAggiornamentiMissioneServlet extends HttpServlet {

    private Configuration cfg;

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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

        Object ruolo =
                session.getAttribute("ruolo");

        return ruolo != null
                && "ADMIN".equalsIgnoreCase(
                        ruolo.toString()
                );
    }

    private Integer parseId(String idParam) {

        if (idParam == null || idParam.isBlank()) {
            return null;
        }

        try {
            return Integer.valueOf(idParam.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /*
     * Converte la lista di entità Aggiornamento in una
     * lista di Map con la data già formattata come stringa,
     * per evitare che FreeMarker debba gestire direttamente
     * un tipo java.time.LocalDateTime (non supportato in
     * modo nativo da ?string con pattern).
     */
    private List<Map<String, Object>> costruisciVistaAggiornamenti(
            List<Aggiornamento> aggiornamenti
    ) {

        List<Map<String, Object>> vista =
                new ArrayList<>();

        for (Aggiornamento aggiornamento : aggiornamenti) {

            if (aggiornamento == null) {
                continue;
            }

            Map<String, Object> item = new HashMap<>();

            item.put(
                    "descrizione",
                    aggiornamento.getDescrizione()
            );

            LocalDateTime dataUpdate =
                    aggiornamento.getData_update();

            item.put(
                    "dataFormattata",
                    dataUpdate != null
                            ? dataUpdate.format(FORMATO_DATA)
                            : "Data non disponibile"
            );

            vista.add(item);
        }

        return vista;
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

        Integer missioneId =
                parseId(request.getParameter("id"));

        if (missioneId == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/missioni"
            );

            return;
        }

        EntityManager entityManager = null;

        try {

            entityManager =
                    JPAUtil.getEntityManager();

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(
                            entityManager
                    );

            Missione missione =
                    daoMissione.findById(missioneId);

            if (missione == null) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/missioni"
                );

                return;
            }

            List<Aggiornamento> aggiornamentiRecuperati =
                    new ArrayList<>(
                            missione.getAggiornamenti()
                    );

            List<Aggiornamento> aggiornamenti =
                    new ArrayList<>();

            for (Aggiornamento aggiornamento
                    : aggiornamentiRecuperati) {

                if (aggiornamento != null) {
                    aggiornamenti.add(aggiornamento);
                }
            }

            aggiornamenti.sort(
                    Comparator.comparing(
                            Aggiornamento::getData_update,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

            List<Map<String, Object>> vistaAggiornamenti =
                    costruisciVistaAggiornamenti(
                            aggiornamenti
                    );

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "contextPath",
                    request.getContextPath()
            );

            data.put(
                    "missione",
                    missione
            );

            data.put(
                    "aggiornamenti",
                    vistaAggiornamenti
            );

            String errore =
                    request.getParameter("errore");

            if (errore != null && !errore.isBlank()) {
                data.put("errore", errore);
            }

            renderTemplate(
                    response,
                    "aggiornamenti-missione.ftl",
                    data
            );

        } catch (RuntimeException e) {

            throw new ServletException(
                    "Errore durante il recupero "
                    + "degli aggiornamenti",
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

        Integer missioneId =
                parseId(request.getParameter("id"));

        if (missioneId == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/missioni"
            );

            return;
        }

        String testo =
                request.getParameter("descrizione");

        if (testo == null || testo.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/missioni/aggiornamenti?id="
                    + missioneId
                    + "&errore=Il+testo+non+puo+essere+vuoto"
            );

            return;
        }

        EntityManager entityManager = null;

        try {

            entityManager =
                    JPAUtil.getEntityManager();

            DaoInterfaceMissione daoMissione =
                    new DaoInterfaceMissioneImpl(
                            entityManager
                    );

            Aggiornamento nuovoAggiornamento =
                    new Aggiornamento();

            nuovoAggiornamento.setDescrizione(
                    testo.trim()
            );

            nuovoAggiornamento.setData_update(
                    LocalDateTime.now()
            );

            Missione missione =
                    daoMissione.aggiungiAggiornamento(
                            missioneId,
                            nuovoAggiornamento
                    );

            if (missione == null) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/missioni"
                );

                return;
            }

            for (Operatore operatore
                    : missione.getOperatori()) {

                if (operatore != null
                        && operatore.getEmail() != null) {

                    MailUtil.inviaNotificaAggiornamento(
                            operatore.getEmail(),
                            operatore.getNome(),
                            missione.getDescrizione(),
                            nuovoAggiornamento.getDescrizione()
                    );
                }
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/missioni/aggiornamenti?id="
                    + missioneId
            );

        } catch (RuntimeException e) {

            throw new ServletException(
                    "Errore durante l'inserimento "
                    + "dell'aggiornamento",
                    e
            );

        } finally {

            if (entityManager != null
                    && entityManager.isOpen()) {

                entityManager.close();
            }
        }
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

        } catch (TemplateException e) {

            throw new ServletException(
                    "Errore durante l'elaborazione "
                    + "del template "
                    + templateName,
                    e
            );
        }
    }
}