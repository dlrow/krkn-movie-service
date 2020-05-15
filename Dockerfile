FROM openjdk

ENV appName krkn-movie-service

COPY ${appName}*.jar /var/${appName}/${appName}.jar

WORKDIR /var/${appName}
ENTRYPOINT ["java", "-jar", "krkn-movie-service.jar"]