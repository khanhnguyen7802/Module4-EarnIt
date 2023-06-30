package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
     * @return the personal information of that specific student
     */
    @Path("/profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Student getCurrentProfile() {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        return StudentDAO.instance.getStudent(email);
    }
    
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAllStudents() {
        return StudentDAO.instance.getAllStudents();
    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStudent(Student student) {
        HttpSession session = req.getSession();
        String currentEmail = session.getAttribute("email").toString();
        StudentDAO.instance.updateStudent(currentEmail, student);
        session.setAttribute("email", student.getEmail());
        return Response.ok("SUCCESS").build();
    }
}
