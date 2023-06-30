package nl.utwente.di.first.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import nl.utwente.di.first.model.User;

import java.net.URI;

public class TestAdminClient {
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/earnit").build();
    }
    public static void main(String[] args) {
        String url_pattern = "api";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());

        //Login as an admin, "ADMIN" response expected
        User admin = new User();
        admin.setEmail("admin1@gmail.com");
        admin.setPassword("admin1");
        System.out.println("Logging in as admin...");
        String results = target.path(url_pattern).path("login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(admin, MediaType.APPLICATION_JSON), String.class);
        System.out.println("Response:\n" + results);

        //Try to get all employments
        System.out.println("Fetching employments...");
        results = target.path(url_pattern).path("employments")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("Response:\n" + results);

        //Try to get all students
        System.out.println("Fetching all students...");
        results = target.path(url_pattern).path("students").path("all")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("Response:\n" + results);

        //Try to get all companies
        System.out.println("Fetching all students...");
        results = target.path(url_pattern).path("companies").path("all")
                .request(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("Response:\n" + results);
    }
}
