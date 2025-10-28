package com.airtel.africa.integration.accountmanagement.route;

import com.airtel.africa.integration.accountmanagement.config.BillingServiceConfig;
import com.airtel.africa.integration.accountmanagement.model.customer.CustomerCreate;
import com.airtel.africa.integration.accountmanagement.model.customer.CustomerUpdate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;

@ApplicationScoped
public class CustomerManagementRoute extends BaseRouteBuilder {

    @Inject
    BillingServiceConfig billingServiceConfig;

    @Override
    public void configure() throws Exception {
        super.configure();
        //rest().openApi("openapi/customer629.yaml");

        rest("/customer-management/v4/customer")
                .get("/{Id}")
                        .produces("application/json")
                        .to("direct:retrieveCustomer")

                .post()
                        .type(CustomerCreate.class)
                        .consumes("application/json")
                        .produces("application/json")
                        .to("direct:createCustomer")

                .patch("/{Id}")
                        .type(CustomerUpdate.class)
                        .consumes("application/json")
                        .produces("application/json")
                        .to("direct:patchCustomer");

        rest("/customer-management/v4/customer/customerByAccountName")
                .patch("/{Id}")
                        .type(CustomerUpdate.class)
                        .consumes("application/json")
                        .produces("application/json")
                        .to("direct:patchCustomerByName")

                .get("/{Id}")
                        .produces("application/json")
                        .to("direct:retrieveCustomerByName");


        from("direct:retrieveCustomer")
                .to("direct:validateHeaders")
                .setHeader(Exchange.HTTP_PATH, constant(""))
                .removeHeaders("*","Id")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Authorization", constant("Basic VFJFUkVTVDpXb3JrRm9yQWlydGVsJDEyMzQ="))
                .toD(billingServiceConfig.customerURL()+"/${header.id}?bridgeEndpoint=true&throwExceptionOnFailure=false",true)
                .setProperty("externalStatusCode", header(Exchange.HTTP_RESPONSE_CODE))
                //.unmarshal().json()
                .log("POST API Response: ${body}")
                .choice()
                    .when(simple("${exchangeProperty.externalStatusCode} >= 200 && ${exchangeProperty.externalStatusCode} < 300"))
                        .unmarshal().json();

        from("direct:createCustomer")
                .to("direct:validateHeaders")
                .to("bean-validator://CustomerCreationValidator")
                .setHeader(Exchange.HTTP_PATH, constant(""))
                .removeHeaders("*","Id")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Authorization", constant("Basic VFJFUkVTVDpXb3JrRm9yQWlydGVsJDEyMzQ="))
                .marshal().json(JsonLibrary.Jackson)
                .toD(billingServiceConfig.customerURL()+"?bridgeEndpoint=true&throwExceptionOnFailure=false", true)
                .setProperty("externalStatusCode", header(Exchange.HTTP_RESPONSE_CODE))
                //.unmarshal().json()
                .log("POST API Response: ${body}")
                .choice()
                   .when(simple("${exchangeProperty.externalStatusCode} >= 200 && ${exchangeProperty.externalStatusCode} < 300"))
                      .unmarshal().json();

        from("direct:patchCustomer")
                .to("direct:validateHeaders")
                .setHeader(Exchange.HTTP_PATH, constant(""))
                .removeHeaders("*","Id")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Authorization", constant("Basic VFJFUkVTVDpXb3JrRm9yQWlydGVsJDEyMzQ="))
                .setHeader(Exchange.HTTP_METHOD, constant("PATCH"))
                .marshal().json(JsonLibrary.Jackson)
                .toD(billingServiceConfig.customerURL()+"/${header.id}?bridgeEndpoint=true&throwExceptionOnFailure=false", true)
                .setProperty("externalStatusCode", header(Exchange.HTTP_RESPONSE_CODE))
                .log("POST API Response: ${body}")
                .choice()
                    .when(simple("${exchangeProperty.externalStatusCode} >= 200 && ${exchangeProperty.externalStatusCode} < 300"))
                        .unmarshal().json();

        from("direct:patchCustomerByName")
                .to("direct:validateHeaders")
                .setHeader(Exchange.HTTP_PATH, constant(""))
                .removeHeaders("*","Id")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Authorization", constant("Basic VFJFUkVTVDpXb3JrRm9yQWlydGVsJDEyMzQ="))
                .setHeader(Exchange.HTTP_METHOD, constant("PATCH"))
                .marshal().json(JsonLibrary.Jackson)
                .toD(billingServiceConfig.customerAccountURL()+"/${header.id}?bridgeEndpoint=true&throwExceptionOnFailure=false", true)
                .setProperty("externalStatusCode", header(Exchange.HTTP_RESPONSE_CODE))
                .log("POST API Response: ${body}")
                .choice()
                .when(simple("${exchangeProperty.externalStatusCode} >= 200 && ${exchangeProperty.externalStatusCode} < 300"))
                .unmarshal().json();

        from("direct:retrieveCustomerByName")
                .to("direct:validateHeaders")
                .setHeader(Exchange.HTTP_PATH, constant(""))
                .removeHeaders("*","Id")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Authorization", constant("Basic VFJFUkVTVDpXb3JrRm9yQWlydGVsJDEyMzQ="))
                .toD(billingServiceConfig.customerAccountURL()+"/${header.id}?bridgeEndpoint=true&throwExceptionOnFailure=false",true)
                .setProperty("externalStatusCode", header(Exchange.HTTP_RESPONSE_CODE))
                //.unmarshal().json()
                .log("POST API Response: ${body}")
                .choice()
                .when(simple("${exchangeProperty.externalStatusCode} >= 200 && ${exchangeProperty.externalStatusCode} < 300"))
                .unmarshal().json();
    }
}
