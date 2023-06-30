package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.CompanyDAO;
import nl.utwente.di.first.dao.StudentDAO;
import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.Student;

import java.util.List;

@Path("/companies")
public class CompaniesResource {
    @Context
    HttpServletRequest req;

    /**
     * Session as a student and given the student's email
     * @return list of companies that the student is working for
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getCompanyList() {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        return CompanyDAO.instance.getCompanyByStudent(email);
    }

    /**
     * Given a company's email
     * @param cid the id of the company which is requested.
     * @return the information of that specific company
     */
    @Path("{cid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Company getCompany(
            @PathParam("cid") String cid
    ) {
        return CompanyDAO.instance.getCompany(cid);
    }

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getAllCompanies() {
        return CompanyDAO.instance.getAllCompany();
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void updateCompany(
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("name") String name,
            @FormParam("location") String location,
            @FormParam("field") String field,
            @FormParam("contact") String contact,
            @FormParam("btw_num") String kvk_num
    ) {
        HttpSession session = req.getSession();
        String currentEmail = session.getAttribute("email").toString();
        Company company = new Company();
        company.setEmail(email);
        company.setPassword(password);
        company.setName(name);
        company.setLocation(location);
        company.setField(field);
        company.setContact(contact);
        company.setKvk_num(kvk_num);

        CompanyDAO.instance.updateCompany(currentEmail, company);

        session.setAttribute("email", company.getEmail());
    }
}
