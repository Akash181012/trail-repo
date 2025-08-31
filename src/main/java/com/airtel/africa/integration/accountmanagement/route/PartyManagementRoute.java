package com.airtel.africa.integration.accountmanagement.route;

import com.airtel.africa.integration.accountmanagement.config.BillingServiceConfig;
import com.airtel.africa.integration.accountmanagement.model.party.IndividualCreate;
import com.airtel.africa.integration.accountmanagement.model.party.IndividualUpdate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.Exchange;
import org.apache.camel.model.dataformat.JsonLibrary;

@ApplicationScoped
public class PartyManagementRoute extends BaseRouteBuilder {

    @Inject
    BillingServiceConfig billingServiceConfig;

    @Override
    public void configure() throws Exception {
        super.configure();
        interceptFrom("rest*").process("IncomingRequestLogger");
        interceptSendToEndpoint("http*").process("OutgoingRequestLogger");

       //rest().openApi("openapi/party632Individual.yaml");

        rest("/party-management/v4/individual")
                .get("/{Id}")
                        .produces("application/json")
                        .to("direct:retrieveIndividual")

                .post()
                        .type(IndividualCreate.class)
                        .consumes("application/json")
                        .produces("application/json")
                        .to("direct:createIndividual")

                .patch("/{Id}")
                        .type(IndividualUpdate.class)
                        .consumes("application/json")
                        .produces("application/json")
                        .to("direct:patchIndividual");

        from("direct:validateHeaders")
                .validate(header("X-Opco").isNotNull())
                .validate(header("X-Request-ID").isNotNull())
                .validate(header("X-Channel").isNotNull());


        from("direct:retrieveIndividual")
                .log("QP: ${header.id}")
                .to("direct:validateHeaders")
                .setHeader(Exchange.HTTP_PATH, constant(""))
                .removeHeaders("*","Id")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Authorization", constant(billingServiceConfig.auth()))
                .toD(billingServiceConfig.partyURL()+"/${header.id}?bridgeEndpoint=true&throwExceptionOnFailure=false", true)
                .setProperty("externalStatusCode", header(Exchange.HTTP_RESPONSE_CODE))
                .log("POST API Response: ${body}")
                .choice()
                    .when(simple("${exchangeProperty.externalStatusCode} >= 200 && ${exchangeProperty.externalStatusCode} < 300"))
                        .unmarshal().json();

        from("direct:createIndividual")
                .to("direct:validateHeaders")
                .to("bean-validator://PartyCreationValidator")
                .setHeader(Exchange.HTTP_PATH, constant(""))
                .removeHeaders("*","Id")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Authorization", constant("Basic VFJFUkVTVDpXb3JrRm9yQWlydGVsJDEyMzQ="))
                .marshal().json(JsonLibrary.Jackson)
                .toD(billingServiceConfig.partyURL()+"?bridgeEndpoint=true&throwExceptionOnFailure=false", true)
                .setProperty("externalStatusCode", header(Exchange.HTTP_RESPONSE_CODE))
                .log("POST API Response: ${body}")
                .choice()
                    .when(simple("${exchangeProperty.externalStatusCode} >= 200 && ${exchangeProperty.externalStatusCode} < 300"))
                        .unmarshal().json();

        from("direct:patchIndividual")
                //.to("bean-validator://PartyCreationValidator")
                .to("direct:validateHeaders")
                .setHeader(Exchange.HTTP_PATH, constant(""))
                .removeHeaders("*","Id")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Authorization", constant("Basic VFJFUkVTVDpXb3JrRm9yQWlydGVsJDEyMzQ="))
                .setHeader(Exchange.HTTP_METHOD, constant("PATCH"))
                .marshal().json(JsonLibrary.Jackson)
                .toD(billingServiceConfig.partyURL()+"/${header.id}?bridgeEndpoint=true&throwExceptionOnFailure=false", true)
                .setProperty("externalStatusCode", header(Exchange.HTTP_RESPONSE_CODE))
                .log("POST API Response: ${body}")
                .choice()
                    .when(simple("${exchangeProperty.externalStatusCode} >= 200 && ${exchangeProperty.externalStatusCode} < 300"))
                        .unmarshal().json();
    }

}
