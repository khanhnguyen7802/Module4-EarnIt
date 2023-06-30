package nl.utwente.di.first.resources;

//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.pdf.draw.LineSeparator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.utwente.di.first.dao.InvoiceDAO;
import nl.utwente.di.first.model.Invoice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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


    /*
    @GET
    @Path("/pdf")
    @Produces("application/pdf")
    public Response getInvoice(@QueryParam("eid") int eid,
                               @QueryParam("week") int week,
                               @QueryParam("year") int year){

        Invoice invoice = InvoiceDAO.instance.getInvoice(eid, week, year);

        Document doc = new Document();
        try{

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(doc, out);

            //adding the logo TODO scale logo
            Image logo = Image.getInstance(invoice.getLogo());
            logo.setAlignment(Element.ALIGN_CENTER);
            doc.add(logo);

            //company details
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph(invoice.getCompany_name()));
            doc.add(new Paragraph(invoice.getCompany_address()));
            doc.add(new Paragraph(invoice.getKvk_number()));

            //issue date (aligned to the right)
            Paragraph issueDate = new Paragraph("Factuurdatum: " + String.valueOf(invoice.getDate_of_issue()));
            issueDate.setAlignment(Element.ALIGN_RIGHT);

            //details
            doc.add(new Paragraph(invoice.getStudent_name()));
            doc.add(new Paragraph(invoice.getKvk_number()));
            doc.add(new Paragraph(invoice.getBtw_number()));
            doc.add(Chunk.NEWLINE);

            //line
            doc.add(new LineSeparator());

            //week number
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("Weeknummer: " + invoice.getWeek()));
            doc.add(Chunk.NEWLINE);

            //job and salary
            doc.add(new Paragraph(invoice.getJob_title()));
            doc.add(new Paragraph((float) invoice.getTotal_salary()));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Total Bedrag excl. BTW"));
            doc.add(new Paragraph("BTW 21%:  " + (invoice.getTotal_salary()*21/100)));

            //line
            doc.add(Chunk.NEWLINE);
            doc.add(new LineSeparator());
            doc.add(Chunk.NEWLINE);

            //final amount
            doc.add(new Paragraph("EinBedrag:  " + (invoice.getTotal_salary() - invoice.getTotal_salary()*21/100)));

            doc.close();

            byte[] pdf = out.toByteArray();

            Response.ResponseBuilder response = Response.ok(pdf);
            response.header("Content-Disposition", "attachment; filename = invoice.pdf");

            return response.build();

        }catch(DocumentException | IOException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

     */
}
