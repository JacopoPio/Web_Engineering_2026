package servlet;

import dao.DaoInterfaceMissione;
import dao.dao_impl.DaoInterfaceMissioneImpl;
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
import model.Missione;
import model.Operatore;

@WebServlet(name = "AdminChiudiMissioneServlet", urlPatterns = {"/admin/missioni/chiudi"})
public class AdminChiudiMissioneServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("ruolo"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int idMissione;
        int successo;
        try {
            idMissione = Integer.parseInt(request.getParameter("idMissione"));
            successo = Integer.parseInt(request.getParameter("successo"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/missioni?errore=parametri_non_validi");
            return;
        }

        if (successo < 0 || successo > 5) {
            response.sendRedirect(request.getContextPath() + "/admin/missioni?errore=successo_non_valido");
            return;
        }

        String commento = request.getParameter("commentoFinale");
        EntityManager em = JPAUtil.getEntityManager();

        try {
            DaoInterfaceMissione daoMissione = new DaoInterfaceMissioneImpl(em);
            Missione missione = daoMissione.chiudiMissione(idMissione, successo, commento);

            if (missione == null) {
                response.sendRedirect(request.getContextPath() + "/admin/missioni?errore=missione_non_trovata");
                return;
            }

            for (Operatore operatore : missione.getOperatori()) {
                if (operatore != null && operatore.getEmail() != null) {
                    MailUtil.inviaNotificaMissioneChiusa(
                            operatore.getEmail(),
                            operatore.getNome(),
                            missione.getDescrizione(),
                            successo,
                            missione.getCommentoFinale()
                    );
                }
            }

            response.sendRedirect(request.getContextPath() + "/admin/missioni?successo=chiusa");

        } catch (IllegalStateException | IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/admin/missioni?errore=chiusura_non_valida");
        } finally {
            em.close();
        }
    }
}
