package com.cevher.keycloak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;

import javax.inject.Inject;
import java.io.IOException;

import static com.cevher.keycloak.Client.postServiceAdminEvent;
import static com.cevher.keycloak.Client.postServiceUserEvent;

public class CustomEventListenerProvider
        implements EventListenerProvider {



    //TODO: Fix this
    // AdminEvent is working correctly
    @Override
    public void onEvent(Event event) {
        if (EventType.REGISTER.equals(event.getType())) {
            System.out.println("User Registered with self registration : " + event.getUserId());

            String json;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                json = ow.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            try {

                // Post to *User* Service
                postServiceUserEvent(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        if (OperationType.CREATE.equals(event.getOperationType()) && ResourceType.USER.equals(event.getResourceType())) {
            System.out.println("User Created through admin: " + event.getResourcePath());

            String json;
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                json = ow.writeValueAsString(event);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            try {

                // Post to *Admin* Service
                postServiceAdminEvent(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void close() {

    }
}
