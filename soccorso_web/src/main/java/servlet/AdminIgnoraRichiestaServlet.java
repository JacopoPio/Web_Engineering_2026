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
import model.StatoRichiesta;

@WebServlet(
        name = "AdminIgnoraRichiestaServlet",
        urlPatterns = {"/admin/richieste/ignora"}
)
public class AdminIgnoraRichiestaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("ruolo"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String email = normalizza(request.getParameter("email_segnalante"))
                .toLowerCase();

        if (email.isBlank()) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/richieste?errore=richiesta_non_valida"
            );
            return;
        }

        EntityManager em = JPAUtil.getEntityManager();
        try {
            DaoInterfaceRichiesta dao = new DaoInterfaceRichiestaImpl(em);
            Richiesta richiesta = dao.findByEmail(email);

            if (richiesta == null) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/richieste?errore=richiesta_non_trovata"
                );
                return;
            }

            if (!StatoRichiesta.ATTIVA.equalsIgnoreCase(richiesta.getStato())) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/richieste?errore=richiesta_non_attiva"
                );
                return;
            }

            Richiesta aggiornata = dao.updateStato(
                    email,
                    StatoRichiesta.IGNORATA
            );

            if (aggiornata == null) {
                response.sendRedirect(
                        request.getContextPath()
                        + "/admin/richieste?errore=aggiornamento_fallito"
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/richieste?successo=ignorata"
            );
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    private String normalizza(String valore) {
        return valore == null ? "" : valore.trim();
    }
}
