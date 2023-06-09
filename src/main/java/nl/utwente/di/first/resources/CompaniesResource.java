package nl.utwente.di.first.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.UserDAO;
import nl.utwente.di.first.model.Company;

import java.util.ArrayList;

public class CompaniesResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Company> getAllCompanies() {
        return UserDAO.instance.getCompanyList();
    }
}
