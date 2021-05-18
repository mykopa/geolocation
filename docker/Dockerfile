FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} geolocation.jar
ENTRYPOINT ["java", "-jar", "geolocation.jar"]