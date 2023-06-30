package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    HttpServletRequest req;

    //TODO: Several check might need to be added to prevent unauthorized requests

    /**
     * Given week and year, return all the daily submission within that time range
     * @param week
     * @param year
     * @return all submissions within that specific week
     */
    @GET
    @Path("student/week")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> getAllSubmissionsByWeek(@QueryParam("week") int week,
                             @QueryParam("year") int year) {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        return SubmissionDAO.instance.getWeeklySubmission(email, week, year);
    }

    @GET
    @Path("company/week")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Submission> getAllSubmissionsByWeekForCompany(@QueryParam("eid") int eid, @QueryParam("week") int week,
                             @QueryParam("year") int year) {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        return SubmissionDAO.instance.getWeeklySubmissionForCompany(eid, email, week, year);
    }

    /**
     * Add a new submission object
     * @param submission
     * @return string indicating success or failure
     */
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addSubmission(Submission submission) {
        return (SubmissionDAO.instance.addSubmission(submission)) ? "SUCCESS" : "FAILURE";
    }

    /**
     * Given a specific week and year with an eid, return the total worked hours
     * @param week - the week being queried
     * @param year - the year being queried
     *
     * @return the total worked hours of that student
     */
    @Path("hours")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int getTotalHours(@QueryParam("week") int week,
                                @QueryParam("year") int year,
                                @QueryParam("eid") int eid) {
        return SubmissionDAO.instance.getTotalHoursOfWeek(eid, week, year);
    }
}
