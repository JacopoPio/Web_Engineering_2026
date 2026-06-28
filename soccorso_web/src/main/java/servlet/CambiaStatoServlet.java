package servlet;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta_configuration.resources.JPAUtil;
import model.Richiesta;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

@WebServlet(
        name = "CambiaStatoServlet",
        urlPatterns = {"/admin/cambia-stato"}
)
public class CambiaStatoServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        /*
         * Controllo accesso amministratore.
         */
        if (session == null
                || !"ADMIN".equalsIgnoreCase(
                        String.valueOf(
                                session.getAttribute("ruolo")
                        )
                )) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }

        request.setCharacterEncoding("UTF-8");

        String emailSegnalante =
                normalizza(
                        request.getParameter(
                                "email_segnalante"
                        )
                );

        String nuovoStato =
                normalizza(
                        request.getParameter("stato")
                )
                .toUpperCase(Locale.ROOT)
                .replace(" ", "_");

        /*
         * Gli stati devono essere identici a quelli usati
         * nel template e nel resto dell'applicazione.
         */
        Set<String> statiValidi = Set.of(
                "ATTIVA",
                "IN_CORSO",
                "CHIUSA"
        );

        if (emailSegnalante.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/richieste"
                    + "?errore=richiesta_non_valida"
            );

            return;
        }

        if (!statiValidi.contains(nuovoStato)) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/richieste"
                    + "?errore=stato_non_valido"
            );

            return;
        }

        EntityManager entityManager =
                JPAUtil.getEntityManager();

        EntityTransaction transaction =
                entityManager.getTransaction();

        try {

            transaction.begin();

            /*
             * email_segnalante è la chiave primaria
             * dell'entità Richiesta.
             */
            Richiesta richiesta =
                    entityManager.find(
                            Richiesta.class,
                            emailSegnalante
                    );

            if (richiesta == null) {

                transaction.rollback();

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/richieste"
                        + "?errore=richiesta_non_trovata"
                );

                return;
            }

            richiesta.setStato(nuovoStato);

            /*
             * richiesta è gestita dall'EntityManager:
             * non è necessario chiamare merge().
             */
            entityManager.flush();

            transaction.commit();

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/richieste"
                    + "?ok=1"
            );

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            /*
             * Mostra l'errore reale nella console di Tomcat.
             */
            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/richieste"
                    + "?errore=aggiornamento_fallito"
            );

        } finally {

            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    private String normalizza(String valore) {

        if (valore == null) {
            return "";
        }

        return valore.trim();
    }
}