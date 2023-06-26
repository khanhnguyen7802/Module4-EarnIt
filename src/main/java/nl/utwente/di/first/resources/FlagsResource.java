package nl.utwente.di.first.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.FlagDAO;
import nl.utwente.di.first.model.Flag;

import java.util.List;

@Path("/flags")
public class FlagsResource {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addWeekInstance(Flag flag) {
        return (FlagDAO.instance.addFlag(flag)) ? "SUCCESS" : "FAILURE";
    }
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flag> getWeekInstances() {
        return FlagDAO.instance.getAllWeekInstances();
    }

}
