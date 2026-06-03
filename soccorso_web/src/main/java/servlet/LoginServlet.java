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

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
                request.getRequestDispatcher("/login.ftl").forward(request, response);
            }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                
                if("admin".equals(username) && "admin123".equals(password)){
                    HttpSession session = request.getSession();
                    session.setAttribute("nome: ", username);
                    session.setAttribute("ruolo", "ADMIN");
                    
                    response.sendRedirect("admin");
                }else{
                    request.setAttribute("errore", "Username o password non validi");
                    request.getRequestDispatcher("/login.ftl").forward(request, response);
                }
            }
    @Override
    public String getServletInfo(){
        return "Servlet login";
    }
   
}