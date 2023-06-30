package nl.utwente.di.first.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import nl.utwente.di.first.model.Company;
import nl.utwente.di.first.model.User;

import java.net.URI;

public class TestCompanyClient {
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/earnit").build();
    }

    public static void main(String[] args) {
        String url_pattern = "api";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());

        //Login as a company, "COMPANY" result is expected
        User company = new Company();
        company.setEmail("company1@gmail.com");
        company.setPassword("company1");
        System.out.println("Logging in as a company...");
        String results = target.path(url_pattern).path("login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(company, MediaType.APPLICATION_JSON), String.class);
        System.out.println("Response:\n" + results);

        //Try to get the list of students
        System.out.println("Fetching students");
        results = target.path(url_pattern).path("students")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("Response:\n" + results);

        //Try to get the list of submission in the current week
        System.out.println("Fetching submissions in a week...");
        results = target.path(url_pattern).path("submissions").path("students").path("week")
                .queryParam("week", 1).queryParam("year", 2023)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println("Response:\n" + results);

        client.close();
    }
}
