package servlet;

import dao.DaoInterfaceOperatore;
import dao.dao_impl.DaoInterfaceOperatoreImpl;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta_configuration.resources.JPAUtil;

import java.io.IOException;

@WebServlet(
        name = "AdminRimuoviOperatoreDaSquadraServlet",
        urlPatterns = {
            "/admin/utenti/rimuovi-squadra"
        }
)
public class AdminRimuoviOperatoreSquadraServlet
        extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        /*
         * Solo un amministratore può rimuovere
         * un operatore da una squadra.
         */
        if (session == null
                || !"ADMIN".equals(
                        session.getAttribute("ruolo")
                )) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login"
            );

            return;
        }

        String email =
                request.getParameter("email");

        if (email == null || email.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/utenti"
                    + "?errore=emailNonValida"
            );

            return;
        }

        EntityManager entityManager = null;

        try {
            entityManager =
                    JPAUtil.getEntityManager();

            DaoInterfaceOperatore operatoreDao =
                    new DaoInterfaceOperatoreImpl(
                            entityManager
                    );

            boolean rimosso =
                    operatoreDao.rimuoviDaSquadra(
                            email.trim()
                    );

            if (rimosso) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/utenti"
                        + "?successo=rimossoDaSquadra"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/utenti"
                        + "?errore=operatoreNonAssegnato"
                );
            }

        } catch (RuntimeException e) {

            throw new ServletException(
                    "Errore durante la rimozione "
                    + "dell'operatore dalla squadra",
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
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        /*
         * L'operazione modifica il database,
         * quindi deve essere eseguita tramite POST.
         */
        response.sendRedirect(
                request.getContextPath()
                + "/admin/utenti"
        );
    }
}