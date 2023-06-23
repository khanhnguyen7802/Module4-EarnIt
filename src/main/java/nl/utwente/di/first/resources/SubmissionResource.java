package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.RegisterDAO;
import nl.utwente.di.first.dao.StudentDAO;
import nl.utwente.di.first.dao.SubmissionDAO;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.model.Submission;

import javax.print.attribute.standard.Media;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("/submissions")
public class SubmissionResource {
    @Context
    HttpServletRequest req;

    //TODO: Several check might need to be added to prevent unauthorized requests

    /**
     * Add a new submission object
     * @param submission
     * @return string indicating success or failure
     */
    @Path("day")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addSubmission(Submission submission) {
        return (SubmissionDAO.instance.addSubmission(submission)) ? "SUCCESS" : "FAILURE";
    }

//    @Path("/day")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public List<Submission> getDateSubmission(
//            @QueryParam("date") String date,
//            @DefaultValue("%%") @QueryParam("flag") String flag
//    ) {
//        HttpSession session = req.getSession();
//        String role = session.getAttribute("role").toString();
//        String email = session.getAttribute("email").toString();
//        if (role.equals("STUDENT")) {
//            return SubmissionDAO.instance.getStudentDateSubmissions(email, date, flag);
//        } else if (role.equals("COMPANY")) {
//            return SubmissionDAO.instance.getCompanyDateSubmissions(email, date, flag);
//        }
//        return new ArrayList<>();
//    }

    @Path("/week")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> getWeekSubmission(
            @QueryParam("date") String date,
            @DefaultValue("%%") @QueryParam("flag") String flag
    ) {
        HttpSession session = req.getSession();
        String role = session.getAttribute("role").toString();
        String email = session.getAttribute("email").toString();
        if (role.equals("STUDENT")) {
            return SubmissionDAO.instance.getStudentWeekSubmissions(email, date, flag);
        } else if (role.equals("COMPANY")) {
            return SubmissionDAO.instance.getCompanyWeekSubmissions(email, date, flag);
        }
        return new ArrayList<>();
    }


    @Path("/flag")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void flagSubmission(
        @QueryParam("subId") String subId,
        @QueryParam("flag") String flag
    ) {
        SubmissionDAO.instance.flagSubmission(subId, flag);
    }
}
