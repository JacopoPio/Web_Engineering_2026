/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {
     @Override
     protected void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException{
                HttpSession session = request.getSession(false);
           
                if (session == null || session.getAttribute("ruolo") == null) {
                    
                  request.getRequestDispatcher("/accesso-negato.ftl").forward(request, response);
            return;
        }

        String ruolo = session.getAttribute("ruolo").toString();
        if (!"ADMIN".equalsIgnoreCase(ruolo)) {
            request.getRequestDispatcher("/accesso-negato.ftl").forward(request, response);
            return;
        }

        String nome = "Amministratore";
        Object nomeSessione = session.getAttribute("nome");
        if (nomeSessione != null) {
            nome = nomeSessione.toString();
        }

        request.setAttribute("nome", nome);
        request.setAttribute("ruolo", ruolo);

        request.setAttribute("richiesteAttive", 0);
        request.setAttribute("richiesteInCorso", 0);
        request.setAttribute("richiesteChiuse", 0);
        request.setAttribute("operatoriDisponibili", 0);
        request.setAttribute("mezziDisponibili", 0);
        request.setAttribute("materialiDisponibili", 0);

        request.getRequestDispatcher("/admin.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet area amministratore";
    }
}
