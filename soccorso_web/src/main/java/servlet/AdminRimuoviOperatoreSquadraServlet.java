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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(
        name = "AdminRimuoviOperatoreSquadraServlet",
        urlPatterns = {
            "/admin/operatori/rimuovi-squadra"
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

        String email =
                normalizza(
                        request.getParameter("email")
                );

        if (email.isBlank()) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/operatori"
                    + "?errore=email_non_valida"
            );

            return;
        }

        EntityManager entityManager =
                JPAUtil.getEntityManager();

        try {

            DaoInterfaceOperatore daoOperatore =
                    new DaoInterfaceOperatoreImpl(
                            entityManager
                    );

            OperatoreRisultato risultato =
                    rimuoviOperatore(
                            daoOperatore,
                            email
                    );

            if (risultato
                    == OperatoreRisultato.NON_TROVATO) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/operatori"
                        + "?errore=operatore_non_trovato"
                );

                return;
            }

            if (risultato
                    == OperatoreRisultato.NON_ASSEGNATO) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/operatori"
                        + "?errore=operatore_non_assegnato"
                );

                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/operatori"
                    + "?successo=rimosso_da_squadra"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    request.getContextPath()
                    + "/operatori"
                    + "?errore=rimozione_fallita"
            );

        } finally {

            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    private OperatoreRisultato rimuoviOperatore(
            DaoInterfaceOperatore daoOperatore,
            String email
    ) {

        model.Operatore operatore =
                daoOperatore.findByEmail(email);

        if (operatore == null) {
            return OperatoreRisultato.NON_TROVATO;
        }

        if (operatore.getSquadra() == null) {
            return OperatoreRisultato.NON_ASSEGNATO;
        }

        boolean rimosso =
                daoOperatore.rimuoviDaSquadra(
                        email
                );

        if (rimosso) {
            return OperatoreRisultato.RIMOSSO;
        }

        return OperatoreRisultato.NON_ASSEGNATO;
    }

    private String normalizza(String valore) {

        if (valore == null) {
            return "";
        }

        return valore.trim();
    }

    private enum OperatoreRisultato {
        RIMOSSO,
        NON_TROVATO,
        NON_ASSEGNATO
    }
}