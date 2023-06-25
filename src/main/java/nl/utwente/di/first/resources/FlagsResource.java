package nl.utwente.di.first.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.FlagDAO;
import nl.utwente.di.first.model.Flag;

import java.util.List;

@Path("flags")
public class FlagsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flag> getWeekInstances() {
        return FlagDAO.instance.getAllWeekInstances();
    }
}
