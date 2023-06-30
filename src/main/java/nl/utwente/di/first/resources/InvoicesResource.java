package nl.utwente.di.first.resources;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import nl.utwente.di.first.dao.InvoiceDAO;
import nl.utwente.di.first.model.Invoice;

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
    public String addInvoice(Invoice invoice) { //TODO check if flag is confirmed
        return (InvoiceDAO.instance.addInvoice(invoice)) ? "SUCCESS" : "FAILURE";
    }

//    @GET
//    @Path("/pdf")
//    @Produces("application/pdf")
//    public Document getInvoice(@QueryParam("eid") int eid,
//                               @QueryParam("week") int week,
//                               @QueryParam("year") int year){
//
//        Invoice invoice = InvoiceDAO.instance.getInvoice(eid, week, year);
//
//        Document doc = new Document();
//        try{
//            doc.add(new Paragraph(invoice.getCompany_name()));
//            doc.add(new Paragraph(invoice.getJob_title()));
//            doc.add(new Paragraph(invoice.getStudent_name()));
//            doc.add(new Paragraph((float) invoice.getTotal_salary()));
//        }catch(DocumentException e){/*TODO*/};
//        return doc;
//    }

}
