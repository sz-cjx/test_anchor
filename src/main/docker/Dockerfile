FROM java:8
VOLUME /tmp
ADD loan-0.0.1-SNAPSHOT.jar loan.jar
RUN bash -c 'touch /loan.jar'
EXPOSE 8201
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/loan.jar"]