/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Richiesta;

import java.io.IOException;
import java.util.Set;

@WebServlet(name = "CambiaStatoServlet", urlPatterns = {"/admin/cambia-stato"})
public class CambiaStatoServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("Soccorso");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("ruolo"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String emailSegnalante = request.getParameter("email_segnalante");
        String nuovoStato = request.getParameter("stato");

        Set<String> statiValidi = Set.of(
                "attiva",
                "in corso",
                "chiusa"
        );

        if (emailSegnalante == null || emailSegnalante.isBlank()
                || nuovoStato == null || !statiValidi.contains(nuovoStato)) {

            response.sendRedirect(request.getContextPath() + "/admin/richieste?errore=1");
            return;
        }

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Richiesta richiesta = em.find(Richiesta.class, emailSegnalante);

            if (richiesta == null) {
                em.getTransaction().rollback();
                response.sendRedirect(request.getContextPath() + "/admin/richieste?errore=1");
                return;
            }

            richiesta.setStato(nuovoStato);

            em.getTransaction().commit();

            response.sendRedirect(request.getContextPath() + "/admin/richieste?ok=1");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            response.sendRedirect(request.getContextPath() + "/admin/richieste?errore=1");

        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}