package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

//This class is used only for testing purposes
@Path("/session")
public class SessionResource {
    @Context
    HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getUserSession() {
        HttpSession session = request.getSession();
        List<String> sessionData = new ArrayList<>();
        String email = (session.getAttribute("email") != null) ? session.getAttribute("email").toString() : "";
        String role = (session.getAttribute("role") != null) ? session.getAttribute("role").toString() : "";
        sessionData.add(email);
        sessionData.add(role);
        return sessionData;
    }
}
