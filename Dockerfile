FROM openjdk:11
MAINTAINER Mingming Li  "21374618@qq.com"

WORKDIR /app
COPY target/chat-0.0.1.jar /app

ENV JAVA_OPTS="-XX:+UseG1GC -server -Xms2048m -Xmx2048m -Xmn512m"
#ENV JAVA_OPTS="-XX:+UseZGC -server -Xms2048m -Xmx2048m -Xmn512m"

RUN echo 'Asia/Shanghai' > /etc/timezone

ENTRYPOINT ["java", "-Duser.timezone=GMT+08", "-Duser.timezone=Asia/Shanghai", "-jar", "/app/chat-0.0.1.jar"]