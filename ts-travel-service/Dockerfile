FROM java:8-jre

RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

ADD ./target/ts-travel-service-1.0.jar /app/
#CMD ["java", "-Xmx200m",  "-jar", "/app/ts-travel-service-1.0.jar"]
CMD ["java",  "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=32697","-Xmx200m",  "-jar", "/app/ts-travel-service-1.0.jar"]
EXPOSE 12346
EXPOSE 32697