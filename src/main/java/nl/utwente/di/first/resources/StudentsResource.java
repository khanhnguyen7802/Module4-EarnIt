package nl.utwente.di.first.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.UserDAO;
import nl.utwente.di.first.model.Student;

import java.util.ArrayList;
import java.util.List;

@Path("/students")
public class StudentsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Student> getAllStudents() {
        return UserDAO.instance.getStudentList();
    }


}
