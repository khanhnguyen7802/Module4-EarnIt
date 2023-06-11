package nl.utwente.di.first.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.UserDAO;
import nl.utwente.di.first.model.Company;

import java.util.ArrayList;
import java.util.List;

@Path("/company")
public class CompaniesResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getAllCompanies() {
        return UserDAO.instance.getCompanyList();
    }

//    @POST

}
