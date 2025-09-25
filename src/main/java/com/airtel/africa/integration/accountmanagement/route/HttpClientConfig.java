package com.airtel.africa.integration.accountmanagement.route;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.camel.component.http.HttpClientConfigurer;
import org.apache.camel.component.http.HttpComponent;
import org.apache.camel.component.http.ProxyHttpClientConfigurer;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;

@ApplicationScoped
public class HttpClientConfig {

    @Produces
    @Startup
    public HttpComponent configureHttpComponent() {
        try {
           /* // Trust-all strategy
            TrustStrategy trustAll = (X509Certificate[] chain, String authType) -> true;

            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(null, trustAll)
                    .build();

            HttpClient httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();*/

            HttpComponent httpComponent = new HttpComponent();
            HttpClientConfigurer proxyConfigurer = new ProxyHttpClientConfigurer("skyhighproxy.ug.airtel.africa", 4146, "http");
            httpComponent.setHttpClientConfigurer(proxyConfigurer);

            return httpComponent;

        } catch (Exception e) {
            throw new RuntimeException("Failed to configure HTTP component", e);
        }
    }
}
