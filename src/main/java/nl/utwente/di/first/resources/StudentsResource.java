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

    /**
     * Session as a company and given a company's email
     * @return list of students employed by that company
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getStudentList() {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();

        return StudentDAO.instance.getStudentByCompany(email);
    }

    /**
     * Given a student's email
     * @param sid
     * @return the personal information of that specific student
     */
    @Path("{sid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent(
            @PathParam("sid") String sid
    ) {
        return StudentDAO.instance.getStudent(sid);
    }
    
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAllStudents() {
        return StudentDAO.instance.getAllStudents();
    }
}
