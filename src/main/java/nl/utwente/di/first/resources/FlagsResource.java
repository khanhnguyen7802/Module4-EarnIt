package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.FlagDAO;
import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Employment;
import nl.utwente.di.first.model.Flag;

import java.util.List;

@Path("/flags")
public class FlagsResource {
    @Context
    HttpServletRequest req;

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addWeekInstance(Flag flag) {
        return (FlagDAO.instance.addFlag(flag)) ? "SUCCESS" : "FAILURE";
    }
    
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flag> getWeekInstances(@QueryParam("week") int week,
       @QueryParam("year") int year) {

        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        String role = session.getAttribute("role").toString();
        return FlagDAO.instance.getAllWeeklyFlags(email, role, week, year);
    }

    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateStatus(Flag newStatusFlag) {
        return (FlagDAO.instance.updateStatus(newStatusFlag)) ? "SUCCESS" : "FAILURE";
    }


    @Path("rejected")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int getTotalRejected() {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();

        return FlagDAO.instance.getTotalRejectedWeeks(email);
    }

    @Path("rejected/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flag> getWeekYearOfReflected() {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        return FlagDAO.instance.getAllRejectedWeeks(email);
    }
    
    @POST
    @Path("appeals")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flag> getAppealsByEmployment(Employment employment) {
        return FlagDAO.instance.getAppealsByEmployment(employment);
    }
}
