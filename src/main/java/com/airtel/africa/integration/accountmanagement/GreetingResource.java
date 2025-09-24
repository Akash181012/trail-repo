package com.airtel.africa.integration.accountmanagement;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.apache.camel.ProducerTemplate;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/api")
public class GreetingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    @ConfigProperty(name = "greeting.message")
    String message;

    @Inject
    ProducerTemplate producerTemplate;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context HttpHeaders headers) {
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=Seattle");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            System.out.println(con.getResponseCode());
        } catch (Exception e){
            LOGGER.error(e.getMessage());
        }
        return "Hello from Quarkus REST " + message;
    }
}
