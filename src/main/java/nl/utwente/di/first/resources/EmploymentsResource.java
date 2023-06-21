package nl.utwente.di.first.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.EmploymentDAO;
import nl.utwente.di.first.model.Employment;

import java.util.List;


@Path("/employments")
public class EmploymentsResource {
    /**
     * Return the full list of employment (which student is connected to which company)
     * @return a list of employments
     */
    @GET
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
}   
