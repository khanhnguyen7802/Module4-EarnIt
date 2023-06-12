package nl.utwente.di.first.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.utwente.di.first.dao.SubmissionDAO;
import nl.utwente.di.first.model.Submission;

import java.io.IOException;
import java.time.LocalDate;

//WEB-INF/progress.html
public class SubmissionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //TODO: Student: new submission, approve+reject suggestion - different servlets?
        //TODO: Company: approve/reject submission (with comment for reasoning)

        int hours = Integer.parseInt(request.getParameter("hours"));
        String description = request.getParameter("comment");
        LocalDate date = LocalDate.parse(request.getParameter("date"));

        Submission submission = new Submission();

        boolean status = SubmissionDAO.instance.addSubmission(submission);
        //TODO if status

    }
}
