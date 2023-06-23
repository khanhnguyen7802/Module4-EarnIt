package nl.utwente.di.first.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.RegisterDAO;
import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;

@Path("/register")
public class RegisterResource {
    
    @POST
    @Path("student")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String registerStudent(Student student) {
        return (RegisterDAO.instance.registerStudent(student)) ? "SUCCESS" : "FAILURE";
    }
    
    @POST
    @Path("company")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String registerCompany(Company company) {
        return (RegisterDAO.instance.registerCompany(company)) ? "SUCCESS" : "FAILURE";
        
    }
}
