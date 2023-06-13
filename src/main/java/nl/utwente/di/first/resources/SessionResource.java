package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//This class is used only for testing purposes
@Path("/session")
public class SessionResource {
    @Context
    HttpServletRequest req;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getUserSession() {
        HttpSession session = req.getSession();
        List<String> sessionData = new ArrayList<>();
        String email = session.getAttribute("email").toString();
        String role = session.getAttribute("role").toString();
        sessionData.add(email);
        sessionData.add(role);
        return sessionData;
    }
}
