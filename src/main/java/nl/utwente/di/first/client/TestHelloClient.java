package nl.utwente.di.first.client;

import java.net.URI;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.client.WebTarget;

import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;


public class TestHelloClient {
	
	public static void main(String[] args) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(getBaseURI());
		String url_pattern = "api";
		String path = "hello";
		
		// Get plain text
		System.out.println("--> GET text/plain message from rest/hello:");
		System.out.println(target.path(url_pattern).path(path).request(
				MediaType.TEXT_PLAIN).get(String.class));
		// Get XML
		System.out.println("\n--> GET text/xml message from rest/hello:");
		System.out.println(target.path(url_pattern).path(path).request(
				MediaType.TEXT_XML).get(String.class));
		// Get HTML
		System.out.println("\n--> GET text/hmtl message from rest/hello:");
		System.out.println(target.path(url_pattern).path(path).request(
						MediaType.TEXT_HTML).get(String.class));
		// Get JSON
		System.out.println("\n--> GET application/json message from rest/hello:");
		System.out.println(target.path(url_pattern).path(path).request(
				MediaType.APPLICATION_JSON).get(String.class));
	}
	
	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/restFirst").build();
	}

}

