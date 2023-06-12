package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.StudentDAO;
import nl.utwente.di.first.model.Student;

import java.util.List;

@Path("/students")
public class StudentsResource {
    @Context
    HttpServletRequest req;
    HttpSession session = req.getSession();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getStudentList(
            @QueryParam("company") String cid
    ) {
        return StudentDAO.instance.getStudentByCompany(cid);
    }

    @Path("{sid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent(
            @PathParam("sid") String sid
    ) {
        return StudentDAO.instance.getStudent(sid);
    }
}
