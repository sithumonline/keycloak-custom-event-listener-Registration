FROM quay.io/keycloak/keycloak:21.1.1

COPY ./target/custom-event-listener.jar /opt/keycloak/providers