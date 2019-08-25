FROM java:8
VOLUME /tmp
ADD customer.restapi-0.0.2-SNAPSHOT.jar restapi.jar
RUN bash -c 'touch /restapi.jar'
EXPOSE 8401
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/restapi.jar"]