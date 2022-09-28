#!/bin/bash
cd ~/train-ticket
git pull
mvn  clean package -Dmaven.test.skip=true -pl ts-time-service -am
cd ~/train-ticket/ts-time-service || exit
docker build -t codewisdom/ts-time-service:2.0.0 .
# shellcheck disable=SC2046
kubectl patch pod ts-time-service-7b8c4964fd-hgjlg   -p '{"spec":{"containers":[{"name": "ts-time-service","image": "codewisdom/ts-time-service:2.0.0"}]}}'
