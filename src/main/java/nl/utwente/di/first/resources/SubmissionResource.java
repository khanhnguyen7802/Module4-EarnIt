package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.SubmissionDAO;
import nl.utwente.di.first.model.Submission;

import java.util.ArrayList;
import java.util.List;

@Path("/submissions")
public class SubmissionResource {
    @Context
    HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> getSubmissions() {
        String role = request.getAttribute("role").toString();
        if (role.equals("STUDENT")) {
            //TODO: return SubmissionDAO.instance.getStudentSubmissions();
            return null;
        } else if (role.equals("COMPANY")) {
            //TODO: return SubmissionDAO.instance.getCompanySubmissions();
            return null;
        }
        return new ArrayList<>();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public List<Submission> getWeekOfSubmissions(){
        String role = request.getAttribute("role").toString();
        if(role.equals("STUDENT")){
            //TODO
            return null;
        }
        else if (role.equals("COMPANY")){
            //TODO
            return null;
        }
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addSubmission(
            @FormParam("hours") int hours,
            @FormParam("comment") String comment,
            @FormParam("date") String date
    ){
        //Input date should be in format yyyy-MM-dd
        //New submission has empty status (unconfirmed submission)
        Submission submission = new Submission();
        submission.setHours(hours);
        submission.setComment(comment);
        submission.setDate(date);
        SubmissionDAO.instance.addSubmission(submission);
    }


}
