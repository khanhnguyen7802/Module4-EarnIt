package nl.utwente.di.first.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.EmploymentDAO;
import nl.utwente.di.first.model.Employment;

import java.util.List;

@Path("/employments")
public class EmploymentsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employment> getEmploymentList() {
        return EmploymentDAO.instance.getAllEmployments();
    }
}
