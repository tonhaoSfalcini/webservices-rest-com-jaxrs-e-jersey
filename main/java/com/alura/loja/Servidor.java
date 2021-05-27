package com.alura.loja;
import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Servidor {
	static HttpServer server = null;
	
	public static void main(String[] args) throws IOException {
		ResourceConfig config = new ResourceConfig().packages("br.com.alura.loja");
		URI uri = URI.create("http://localhost:8888/");
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
		System.out.println("Server is running");
		
		System.in.read();
		server.stop();
	}
	
	public void serverStop() {
		if(server != null)server.stop();		
	}

	public ResourceConfig getResourceConfig() {
		return new ResourceConfig().packages("br.com.alura.loja");
	}
	
	public URI getUri() {
		return URI.create("http://localhost:8888/");
	}
	
	public HttpServer serverStart(URI uri, ResourceConfig config) {
		server = GrizzlyHttpServerFactory.createHttpServer(uri, config);
		System.out.println("Server is running");
		return server;
	}
}
