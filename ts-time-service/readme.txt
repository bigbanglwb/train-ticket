
rest url:
http://time

build:
mvn clean package

docker:
注意更新DockerFile
cd mongo-cluster
docker run -d --name time-mongo mongo
cd target
docker build -t my/ts-time-service .
docker run -d -p 12346:12346  --name ts-time-service --link time-mongo:time-mongo my/ts-time-service
(mongo-local is in config file: resources/application.yml)

!!!!!notice: please add following lines into /etc/hosts to simulate the network access:
127.0.0.1	ts-time-service