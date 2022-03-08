FROM amazoncorretto:latest
ENV APPLICATION='customer-restapi'
VOLUME /tmp

ADD customer.restapi-3.2.0.jar customer.jar

RUN bash -c 'touch /customer.jar'
RUN ln -sf /usr/share/zoneinfo/America/New_York /etc/localtime
RUN echo 'America/New_York' >/etc/timezone

EXPOSE 8101

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "-XX:+UseContainerSupport","-XX:MaxRAMPercentage=70.0", "/customer.jar"]
