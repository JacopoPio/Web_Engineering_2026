package servlet;

import dao.DaoInterfaceRichiesta;
import dao.dao_impl.DaoInterfaceRichiestaImpl;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta_configuration.resources.JPAUtil;
import java.io.IOException;
import model.Richiesta;

@WebServlet(
        name = "AdminIgnoraRichiestaServlet",
        urlPatterns = {"/admin/richieste/ignora"}
)
public class AdminIgnoraRichiestaServlet extends HttpServlet {

    private static final String STATO_ATTIVA = "attiva";
    private static final String STATO_IGNORATA = "ignorata";

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                || !"ADMIN".equals(session.getAttribute("ruolo"))) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        String emailSegnalante =
                request.getParameter("email_segnalante");

        if (emailSegnalante == null
                || emailSegnalante.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/richieste"
                    + "?errore=richiesta_non_valida"
            );
            return;
        }

        emailSegnalante =
                emailSegnalante.trim().toLowerCase();

        EntityManager entityManager =
                JPAUtil.getEntityManager();

        try {

            DaoInterfaceRichiesta daoRichiesta =
                    new DaoInterfaceRichiestaImpl(
                            entityManager
                    );

            Richiesta richiesta =
                    daoRichiesta.findByEmail(
                            emailSegnalante
                    );

            if (richiesta == null) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/richieste"
                        + "?errore=richiesta_non_trovata"
                );
                return;
            }

            String statoAttuale =
                    richiesta.getStato();

            if (statoAttuale == null
                    || !STATO_ATTIVA.equalsIgnoreCase(
                            statoAttuale.trim()
                    )) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/richieste"
                        + "?errore=richiesta_non_attiva"
                );
                return;
            }

            Richiesta richiestaAggiornata =
                    daoRichiesta.updateStato(
                            emailSegnalante,
                            STATO_IGNORATA
                    );

            if (richiestaAggiornata == null) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/richieste"
                        + "?errore=aggiornamento_fallito"
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/richieste"
                    + "?successo=ignorata"
            );

        } catch (Exception e) {

            e.printStackTrace();

            if (!response.isCommitted()) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/richieste"
                        + "?errore=aggiornamento_fallito"
                );
            }

        } finally {

            if (entityManager != null
                    && entityManager.isOpen()) {

                entityManager.close();
            }
        }
    }
}

