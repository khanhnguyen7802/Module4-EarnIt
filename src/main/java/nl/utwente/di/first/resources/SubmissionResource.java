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

    // TODO: Not necessary at the moment?
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<Submission> getSubmissions() {
//        HttpSession session = req.getSession();
//        String role = session.getAttribute("role").toString();
//        String email = session.getAttribute("email").toString();
//
//        if (role.equals("STUDENT")) {
//            return getSubmissionByStudent(email);
//
//        } else if (role.equals("COMPANY")) {
//            return null;
//        }
//
//        return null;
//    }

    /**
     * Return all the submissions of a specific student
     * @param sid
     * @return a full list of all submissions of that student
     */
    @Path("{sid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> getSubmissionByStudent(
            @PathParam("sid") String sid
    ) {
        return SubmissionDAO.instance.getStudentSubmissions(sid);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public List<Submission> getWeekOfSubmissions(@FormParam("?"/*TODO*/) String weekNumber){
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        String role = session.getAttribute("role").toString();

        if(role.equals("STUDENT")){ //TODO is this necessary?
            return null;
        }
        else if (role.equals("COMPANY")){
            return null;
        }
        return SubmissionDAO.instance.getWeekOfSubmissions(email, weekNumber);
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
