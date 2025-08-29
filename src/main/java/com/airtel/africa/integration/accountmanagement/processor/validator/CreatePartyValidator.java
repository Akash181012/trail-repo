package com.airtel.africa.integration.accountmanagement.processor.validator;

import com.airtel.africa.integration.accountmanagement.model.party.IndividualCreate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@ApplicationScoped()
@Named("CreatePartyValidator")
public class CreatePartyValidator implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        IndividualCreate individualCreate = exchange.getIn().getBody(IndividualCreate.class);
        exchange.getIn().setBody(individualCreate);
    }
}