FROM openjdk:8-jre-alpine
RUN mkdir app & mkdir cupload1
COPY target/*.jar /app/app.jar
RUN  apk add -U tzdata; \
ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime; \
echo 'Asia/Shanghai' >/etc/timezone; \
touch /app/app.jar;

EXPOSE 8080
VOLUME cupload1

ENTRYPOINT [ "sh", "-c", "java -jar /app/app.jar" ]
