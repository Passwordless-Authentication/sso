FROM quay.io/keycloak/keycloak:26.2.4
EXPOSE 8080
EXPOSE 8443
COPY target/sso.jar /opt/keycloak/providers/providers.jar
WORKDIR /opt/keycloak
RUN /opt/keycloak/bin/kc.sh build # TODO: make sure this is needed
