package nl.utwente.di.first.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.utwente.di.first.dao.UserDAO;
import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;

import java.io.IOException;
import java.io.Serial;

public class RegisterServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    public RegisterServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = request.getParameter("role");
        UserDAO userDao = UserDAO.instance;

        if (role.equals("STUDENT")) {
            Student student = new Student();
            student.setEmail(request.getParameter("email"));
            student.setPassword(request.getParameter("password"));
            student.setName(request.getParameter("name"));
            student.setUniversity(request.getParameter("university"));
            student.setBirth(request.getParameter("birth"));
            student.setStudy(request.getParameter("study"));
            student.setSkills(request.getParameter("skills"));
            student.setBtw_num(request.getParameter("btw_num"));

            boolean status = userDao.registerStudent(student);
            if (status) {
                request.getRequestDispatcher("/student_home.jsp").forward(request, response);
            } else {
                request.setAttribute("errMessage", "FAIL");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } else if (role.equals("COMPANY")) {
            Company company = new Company();
            company.setEmail(request.getParameter("email"));
            company.setPassword(request.getParameter("password"));
            company.setName(request.getParameter("name"));
            company.setLocation(request.getParameter("location"));
            company.setField(request.getParameter("field"));
            company.setContact(request.getParameter("contact"));
            company.setKvk_num(request.getParameter("kvk_num"));

            boolean status = userDao.registerCompany(company);
            if (status){
                request.getRequestDispatcher("/company_home.jsp").forward(request, response);
            } else {
                request.setAttribute("errMessage", "FAIL");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        }

    }
}
