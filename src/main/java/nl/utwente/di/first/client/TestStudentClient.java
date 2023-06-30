package nl.utwente.di.first.client;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import nl.utwente.di.first.model.Student;
import nl.utwente.di.first.model.User;

import java.net.URI;

public class TestStudentClient {
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/earnit").build();
    }
    public static void main(String[] args) {
        String url_pattern = "api";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());

        //Login as a student, a response "STUDENT" is expected
        User student = new User();
        student.setEmail("student1@gmail.com");
        student.setPassword("student1");
        System.out.println("Logging in as student...");
        String results = target.path(url_pattern).path("login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(student, MediaType.APPLICATION_JSON), String.class);
        System.out.println("Response:\n" + results);

        //Try to get the list of company
        System.out.println("Fetching companies...");
        results = target.path(url_pattern).path("companies")
                        .request(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("Response:\n" + results);

        //Try to get the list of submission in the current week
        System.out.println("Fetching submissions in a week...");
        results = target.path(url_pattern).path("submissions").path("students").path("week")
                .queryParam("week", 1).queryParam("year", 2023)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);
        System.out.println("Response:\n" + results);
        //TODO: week number and year
        //TODO: fetching the flags api
        client.close();
    }
}
