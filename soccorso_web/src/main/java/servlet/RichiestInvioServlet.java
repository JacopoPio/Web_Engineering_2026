/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.DaoInterfaceRichiesta;
import dao.dao_impl.DaoInterfaceRichiestaImpl;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta_configuration.resources.JPAUtil;
import model.Richiesta;
import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;
import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author alesp
 */
@WebServlet(name = "RichiestaInvioServlet", urlPatterns = {"/richiesta-invio"})
@MultipartConfig
public class RichiestInvioServlet extends HttpServlet {

    private EntityManager emf;

    @Override
    public void init() throws ServletException {
        this.emf = JPAUtil.getEntityManager();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String descrizione = request.getParameter("descrizione");
        String indirizzo = request.getParameter("posizione");
        String emailSegnalante = request.getParameter("emailSegnalante");
        String nomeSegnalante = request.getParameter("nomeSegnalante");
        byte[] ipOrigine = request.getRemoteAddr().equals("0:0:0:0:0:0:0:1") ? new byte[]{127,0,0,1} 
                : InetAddress.getByName(request.getRemoteAddr()).getAddress();

        if (descrizione == null || descrizione.isBlank()
                || indirizzo == null || indirizzo.isBlank()
                || emailSegnalante == null || emailSegnalante.isBlank() || nomeSegnalante.isBlank()) {

            response.sendRedirect(request.getContextPath() + "/home?errore=campi");
            return;
        }

        descrizione = descrizione.trim();
        indirizzo = indirizzo.trim();
        emailSegnalante = emailSegnalante.trim().toLowerCase();
        nomeSegnalante = nomeSegnalante.trim().toLowerCase();

        String pathFoto = salvaFoto(request);

        Richiesta richiesta = new Richiesta(
                emailSegnalante,
                descrizione,
                indirizzo,
                "in corso",
                pathFoto,
                nomeSegnalante,
                ipOrigine
        );

        DaoInterfaceRichiesta gestore_richiesta = new DaoInterfaceRichiestaImpl(this.emf);
        Richiesta richiesta_salvata = gestore_richiesta.save(richiesta);
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