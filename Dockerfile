FROM openjdk

ENV appName krkn-movie-service

COPY /build/libs/${appName}*.jar /var/${appName}/${appName}.jar

WORKDIR /var/${appName}
ENTRYPOINT ["java", "-jar", "krkn-movie-service.jar"]