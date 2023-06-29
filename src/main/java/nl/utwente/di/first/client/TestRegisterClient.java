package nl.utwente.di.first.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;

public class TestRegisterClient {
    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/earnit").build();
    }
    public static void main(String[] args) {
        String url_pattern = "api";
        String path = "register";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getBaseURI());

    }
}
