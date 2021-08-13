# Dockerfile Pet Service

FROM adoptopenjdk/openjdk11:alpine as build
MAINTAINER Martin Ivan Rios

WORKDIR /app

ADD ./gradlew ./gradlew
ADD ./gradle ./gradle
RUN ./gradlew --version

ADD ./ ./
RUN ./gradlew bootJar -x test

FROM adoptopenjdk/openjdk11:alpine-jre as startup
MAINTAINER  Martin Ivan Rios

WORKDIR /app

# Entrypoint
ADD ./etc/start.sh /root/start.sh
ADD ./etc/stop.sh /root/stop.sh
ADD ./etc/healthcheck.sh /root/healthcheck.sh
COPY --from=0 /app/build/libs/*.jar /app/app.jar

RUN chmod +x /root/start.sh
RUN chmod +x /root/stop.sh
RUN chmod +x /root/healthcheck.sh

ENTRYPOINT ["/root/start.sh"]

HEALTHCHECK --interval=15s --start-period=45s CMD /root/healthcheck.sh || exit 1