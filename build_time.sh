#!/bin/bash
cd ~/train-ticket
git pull
mvn  clean package -Dmaven.test.skip=true -pl ts-time-service -am
cd ~/train-ticket/ts-time-service || exit
docker build -t codewisdom/ts-time-service:1.0.0 .
# shellcheck disable=SC2046
kubectl delete -f ./deploy.yaml
kubectl apply -f ./deploy.yaml



