package nl.utwente.di.first.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.utwente.di.first.dao.UserDAO;
import nl.utwente.di.first.model.User;

import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        UserDAO userDao = UserDAO.instance;

        try {
            String role = userDao.loginUser(user);
            HttpSession session = request.getSession(); // creating a session
            session.setAttribute(role, email); // setting session attribute
            RequestDispatcher dispatcher;
            switch(role) {
                case "COMPANY":
                    dispatcher = request.getRequestDispatcher("/company_home.jsp");
                    dispatcher.forward(request, response);
                    break;
                case "STUDENT":
                    dispatcher = request.getRequestDispatcher("/student_home.jsp");
                    dispatcher.forward(request, response);
                    break;
                default:
                    request.setAttribute("errMessage", "Wrong email/password. Please try again.");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}