FROM openjdk:11

#设置工作目录
WORKDIR /app

ADD /target/netdisk-front-1.0-SNAPSHOT.jar /app/app.jar

CMD mkdir /app/config/

#设置变量 ENV username=""

ENV JAVA_OPTS=""

#公开端口

EXPOSE 9527

#设置启动命令

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]