FROM java:8
ENV APPLICATION='customer-ext'
VOLUME /tmp

ADD customer-ext-server-4.1.0.jar customer-ext.jar

RUN bash -c 'touch /customer-ext.jar'
RUN ln -sf /usr/share/zoneinfo/America/New_York /etc/localtime
RUN echo 'America/New_York' >/etc/timezone

EXPOSE 9528

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/customer-ext.jar"]