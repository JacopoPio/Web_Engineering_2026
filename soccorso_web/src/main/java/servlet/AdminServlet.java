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
                String nome = "Amministratore";
                String ruolo = "ADMIN";
                
                if (session != null) {
                    Object nomeSessione = session.getAttribute("nome");
                    Object ruoloSessione = session.getAttribute("ruolo");
                        
                    if(nomeSessione != null){
                        nome = nomeSessione.toString();
                    }
                    if(ruoloSessione != null){
                        ruolo = ruoloSessione.toString();
                    }
                }
        
                request.setAttribute("nome", nome);
                request.setAttribute("ruolo", ruolo);
                request.getRequestDispatcher("/admin.ftl").forward(request, response);
     }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
     protected void doPost(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException{
                doGet(request, response);
            }
     @Override
     public String getServletInfo(){
         return "Serrvlet area amministratore";
     }
 }