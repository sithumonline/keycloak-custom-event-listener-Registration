package com.cevher.keycloak;

import org.jboss.logging.Logger;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.UserModel;

import java.util.Map;

import static com.cevher.keycloak.Client.postService;

public class CustomEventListenerProvider
        implements EventListenerProvider {


    @Override
    public void onEvent(Event event) {
        if(EventType.REGISTER.equals(event.getType())) {
            System.out.println("User Registered: " + event.getUserId());
            //postService(event.toString());
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        if(OperationType.CREATE.equals(event.getOperationType()) && ResourceType.USER.equals(event.getResourceType())) {
            System.out.println("User Created: " + event.getResourcePath());
            //postService(event.toString());
        }
    }

    @Override
    public void close() {

    }
}
