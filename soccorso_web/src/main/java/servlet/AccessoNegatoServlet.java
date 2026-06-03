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


@WebServlet(name = "AccessoNegatoServlet", urlPatterns = {"/accesso-negato.ftl"})
public class AccessoNegatoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
            request.setAttribute("titolo","Accesso Negato");
            request.setAttribute("messaggio", "Non hai i permessi necessari per visualizzare la pagina");
            
            request.getRequestDispatcher("/accesso-negato.ftl").forward(request, response);
        }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException{
                    doGet(request, response);
            }
     @Override
     public String getServletInfo(){
         return "Servlet accesso negato";
     }
}
