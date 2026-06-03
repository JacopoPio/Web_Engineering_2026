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

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
                    request.setAttribute("titolo", "HOME-Soccorso Web");
                    request.setAttribute("messaggio", "Benvenuto nel portale di soccorso");
                    request.setAttribute("nome", "Utente");
                    
                    request.getRequestDispatcher("/home.ftl").forward(request,response);
                }
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException{
                    doGet(request, response);
                }
        @Override
        public String getServletInfo(){
            return "Servlet Home";
        }
}
