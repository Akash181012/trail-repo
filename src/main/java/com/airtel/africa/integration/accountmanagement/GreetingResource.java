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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/test")
public class GreetingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    ProducerTemplate producerTemplate;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context HttpHeaders headers) throws Exception{

        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        // Install a trust manager that trusts everything
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }}, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // The URL to test
        String urlStr = "https://maps.googleapis.com/maps/api/geocode/json?address=Seattle";
        System.out.println("Requesting: " + urlStr);

        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(15_000);
        con.setReadTimeout(15_000);

        int code = con.getResponseCode();
        System.out.println("HTTP response code: " + code);

        try (InputStream in = (code >= 200 && code < 400) ? con.getInputStream() : con.getErrorStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            int lines = 0;
            while ((line = br.readLine()) != null && lines++ < 50) {
                System.out.println(line);
            }
            if (lines == 50) System.out.println("...truncated...");
        } catch (Exception e) {
            System.err.println("Read error:");
            e.printStackTrace();
        }
        return "Hello from Quarkus REST " ;
    }
}
