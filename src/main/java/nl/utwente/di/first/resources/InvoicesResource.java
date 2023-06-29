package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.InvoiceDAO;
import nl.utwente.di.first.dao.SubmissionDAO;
import nl.utwente.di.first.model.Invoice;
import nl.utwente.di.first.model.Submission;

import java.util.List;

@Path("/invoices")
public class InvoicesResource {
    @Context
    HttpServletRequest req;

    @GET
    @Path("week")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Invoice> getAllInvoicesByWeek(@QueryParam("week") int week,
                                                    @QueryParam("year") int year) {
        HttpSession session = req.getSession();
        String email = session.getAttribute("email").toString();
        return InvoiceDAO.instance.getWeeklyInvoices(email, week, year);
    }

    /**
     * Add a new invoice object
     * @param invoice
     * @return string indicating success or failure
     */
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String addInvoice(Invoice invoice) {
        return (InvoiceDAO.instance.addInvoice(invoice)) ? "SUCCESS" : "FAILURE";
    }
}
