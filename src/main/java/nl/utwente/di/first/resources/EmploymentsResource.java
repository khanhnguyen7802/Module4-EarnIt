package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.EmploymentDAO;
import nl.utwente.di.first.model.Employment;

import java.util.List;


@Path("/employments")
public class EmploymentsResource {
    @Context
    HttpServletRequest req;
    /**
     * Return the full list of employment (which student is connected to which company)
     * @return a list of employments
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employment> getEmploymentListByEmail() {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        return EmploymentDAO.instance.getAllEmploymentsByEmail(email);
    }
    
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employment> getEmploymentList() {
        return EmploymentDAO.instance.getAllEmployments();
    }
    
    @GET
    @Path("vacancies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employment> getVacancyList() {
        return EmploymentDAO.instance.getAllVacancies();
    }
    
    @POST
    @Path("link")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String linkStudent(Employment newLink) {
        return (EmploymentDAO.instance.linkStudent(newLink)) ? "SUCCESS" : "FAILURE";
    }
    
    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String createJob(Employment newJob) {
        return (EmploymentDAO.instance.createJob(newJob)) ? "SUCCESS" : "FAILURE";
    }
}   
