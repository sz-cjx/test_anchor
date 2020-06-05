FROM java:8

VOLUME /tmp

ADD customer.restapi-0.0.3.jar customer-restapi.jar

RUN bash -c 'touch /customer-restapi.jar'
RUN ln -sf /usr/share/zoneinfo/America/New_York /etc/localtime
RUN echo 'America/New_York' >/etc/timezone

EXPOSE 8102

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/customer-restapi.jar"]