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
        String path = "students";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());
        studentLogin(target);
        requestCompanyList(target);
        client.close();
    }

    public static void studentLogin(WebTarget target) {
        User student = new User();
        student.setEmail("student1@gmail.com");
        student.setPassword("student1");
        System.out.println("Logging in as student...");
        Response response = target.request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(student));
        System.out.println("Response: " + response);
    }
    private static void requestCompanyList(WebTarget target) {
        System.out.println("Requesting company list");
        Response response = target.request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println("Response: " + response);
    }
}
