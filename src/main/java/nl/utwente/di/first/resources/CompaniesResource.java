package nl.utwente.di.first.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.UserDAO;
import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;

import java.util.ArrayList;
import java.util.List;

@Path("/company")
public class CompaniesResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getAllCompanies() {
        return UserDAO.instance.getCompanyList();
    }

    @Path("{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getStudentByEmail (@PathParam("email") String email) {
        return UserDAO.instance.getStudentListByEmail(email);
    }
}
