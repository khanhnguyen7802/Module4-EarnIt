package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

@Path("/logout")
public class LogoutResource {
    @POST
    public void logOut(@Context HttpServletRequest request) {
        System.out.println("user tried to logout");
        request.getSession().invalidate();
        System.out.println(request.getSession().getAttribute("role"));
    }
}
