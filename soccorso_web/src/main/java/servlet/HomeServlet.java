/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import java.util.Random;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
                    request.setAttribute("titolo", "HOME-Soccorso Web");
                    request.setAttribute("messaggio", "Benvenuto nel portale di soccorso");
                   
                    HttpSession session = request.getSession(false);
                    if(session != null && session.getAttribute("nome") != null ){
                            request.setAttribute("nome", session.getAttribute("nome"));
                    }else{
                        request.setAttribute("nome", "Ospite");
                    }
                    
                    int a = 1 + new Random().nextInt(9);
                    int b = 1 + new Random().nextInt(9);
                    request.setAttribute("captchaA", a);
                    request.setAttribute("captchaB", b);
                    request.setAttribute("captchaRisposta", a+b);
                    
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
