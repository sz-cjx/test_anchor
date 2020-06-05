FROM java:8

VOLUME /tmp

ADD customer.restapi-0.0.3.jar customer-clientapi.jar

RUN bash -c 'touch /customer-clientapi.jar'
RUN ln -sf /usr/share/zoneinfo/America/New_York /etc/localtime
RUN echo 'America/New_York' >/etc/timezone

EXPOSE 9102

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/customer-clientapi.jar"]