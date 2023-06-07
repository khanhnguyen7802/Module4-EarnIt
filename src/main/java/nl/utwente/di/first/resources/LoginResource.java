package nl.utwente.di.first.resources;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.UserDAO;
import nl.utwente.di.first.model.User;

import java.net.http.HttpRequest;
import java.sql.SQLException;

@Path("/login")
public class LoginResource {
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String attemptLogin(@Context HttpServletRequest req, @FormParam("email") String email, @FormParam("password") String password) {
        System.out.printf("A user tried to log in with the credentials: email: %s, password: %s%n", email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        UserDAO userDao = UserDAO.instance;
        try {
            String role = userDao.loginUser(user);
            HttpSession session = req.getSession(); // creating a session
            if (!role.equals("INVALID")) {
                session.setAttribute("role", role);
                session.setAttribute("email", email);
            }
            return role;
        } catch (SQLException e) {
            // TODO handle exception correctly, since the throwing of RuntimeExceptions should be kept to a minimum.
            return "Bullshit";
        }
    }
}
