package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

@Path("/logout")
public class LogoutResource {
    @POST
    public void logOut(@Context HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
