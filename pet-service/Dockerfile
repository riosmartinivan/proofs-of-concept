# Dockerfile Pet Service

FROM openjdk:8u212-jdk-alpine as build
MAINTAINER Martin Ivan Rios

WORKDIR /app

ADD ./ ./
RUN java -version
RUN ./mvnw -DskipTests -B clean package

FROM openjdk:8u212-jre-alpine as startup
MAINTAINER  Martin Ivan Rios

WORKDIR /app

COPY --from=0 ./app ./sources
RUN ls -la ./sources

# Entrypoint
ADD ./etc/start.sh /root/start.sh
ADD ./etc/stop.sh /root/stop.sh
ADD ./etc/healthcheck.sh /root/healthcheck.sh
COPY --from=0 /app/target/pet-service.jar /app/pet-service.jar

RUN chmod +x /root/start.sh
RUN chmod +x /root/stop.sh
RUN chmod +x /root/healthcheck.sh

ENTRYPOINT ["/root/start.sh"]

HEALTHCHECK --interval=15s --start-period=45s CMD /root/healthcheck.sh || exit 1
