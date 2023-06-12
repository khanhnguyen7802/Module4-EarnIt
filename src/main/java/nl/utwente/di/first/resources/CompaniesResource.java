package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.CompanyDAO;
import nl.utwente.di.first.dao.LoginDAO;
import nl.utwente.di.first.dao.StudentDAO;
import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;

import java.util.List;

@Path("/companies")
public class CompaniesResource {
    @Context
    HttpServletRequest req;
    HttpSession session = req.getSession();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getCompanyList(
            @QueryParam("student") String sid
    ) {
        return CompanyDAO.instance.getCompanyByStudent(sid);
    }

    @Path("{cid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Company getStudent(
            @PathParam("cid") String cid
    ) {
        return CompanyDAO.instance.getCompany(cid);
    }
}
