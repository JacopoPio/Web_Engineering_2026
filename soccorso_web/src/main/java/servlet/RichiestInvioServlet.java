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
import jakarta.servlet.http.Part;
import model.Richiesta;
import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;
import java.io.IOException;

/**
 *
 * @author alesp
 */
@WebServlet(name = "RichiestaInvioServlet", urlPatterns = {"/richiesta-invio"})
public class RichiestInvioServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("SoccorsoWebPU");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String descrizione = request.getParameter("descrizione");
        String indirizzo = request.getParameter("posizione");
        String emailSegnalante = request.getParameter("emailSegnalante");

        if (descrizione == null || descrizione.isBlank()
                || indirizzo == null || indirizzo.isBlank()
                || emailSegnalante == null || emailSegnalante.isBlank()) {

            response.sendRedirect(request.getContextPath() + "/home?errore=campi");
            return;
        }

        descrizione = descrizione.trim();
        indirizzo = indirizzo.trim();
        emailSegnalante = emailSegnalante.trim().toLowerCase();

        String pathFoto = salvaFoto(request);

        Richiesta richiesta = new Richiesta(
                emailSegnalante,
                descrizione,
                indirizzo,
                "DA_CONFERMARE",
                pathFoto
        );

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Richiesta esistente = em.find(Richiesta.class, emailSegnalante);

            if (esistente != null) {
                em.getTransaction().rollback();
                response.sendRedirect(request.getContextPath() + "/home?errore=email");
                return;
            }

            em.persist(richiesta);

            em.getTransaction().commit();

            response.sendRedirect(request.getContextPath() + "/richiesta-inviata");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw new ServletException("Errore durante il salvataggio della richiesta", e);

        } finally {
            em.close();
        }
    }

    private String salvaFoto(HttpServletRequest request)
            throws IOException, ServletException {

        Part fotoPart = request.getPart("foto");

        if (fotoPart == null || fotoPart.getSize() == 0) {
            return null;
        }

        String submittedFileName = fotoPart.getSubmittedFileName();

        if (submittedFileName == null || submittedFileName.isBlank()) {
            return null;
        }

        String nomeOriginale = Paths.get(submittedFileName)
                .getFileName()
                .toString();

        String estensione = "";

        int punto = nomeOriginale.lastIndexOf(".");
        if (punto >= 0) {
            estensione = nomeOriginale.substring(punto);
        }

        String nomeFile = UUID.randomUUID().toString() + estensione;

        String uploadPath = getServletContext().getRealPath("/uploads");

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileCompleto = uploadPath + File.separator + nomeFile;

        fotoPart.write(fileCompleto);

        return "uploads/" + nomeFile;
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
 }