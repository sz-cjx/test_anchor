FROM java:8
ENV APPLICATION='customer'
VOLUME /tmp

ADD customer-4.0.1.jar customer.jar

RUN bash -c 'touch /customer.jar'
RUN ln -sf /usr/share/zoneinfo/America/New_York /etc/localtime
RUN echo 'America/New_York' >/etc/timezone

EXPOSE 8204

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/customer.jar"]