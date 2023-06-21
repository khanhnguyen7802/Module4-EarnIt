package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.StudentDAO;
import nl.utwente.di.first.dao.SubmissionDAO;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.model.Submission;

import java.util.ArrayList;
import java.util.List;

@Path("/submissions")
public class SubmissionResource {
    @Context
    HttpServletRequest req;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> getSubmission() {
        HttpSession session = req.getSession();
        String role = session.getAttribute("role").toString();
        String email = session.getAttribute("email").toString();
        if (role.equals("STUDENT")) {
            return SubmissionDAO.instance.getStudentSubmissions(email);
        } else if (role.equals("COMPANY")) {
            return SubmissionDAO.instance.getCompanySubmissions(email);
        }
        return new ArrayList<>();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addSubmission(
            @FormParam("eid") String eid,
            @FormParam("hours") int hours,
            @FormParam("comment") String comment,
            @FormParam("date") String date
    ){
        Submission submission = new Submission();
        submission.setEmploymentId(eid);
        submission.setHours(hours);
        submission.setComment(comment);
        submission.setDate(date);
        SubmissionDAO.instance.addSubmission(submission);
    }

    //TODO: POST request for flagging

}
