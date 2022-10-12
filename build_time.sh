#!/bin/bash
#cd ~/train-ticket
#git pull
#mvn  clean package -Dmaven.test.skip=true -pl ts-time-service -am
#cd ~/train-ticket/ts-time-service || exit
#docker build -t codewisdom/ts-time-service:3.0.0 .
## shellcheck disable=SC2046
#kubectl patch pod ts-time-service-6cff58dc4c-6q68r   -p '{"spec":{"containers":[{"name": "ts-time-service","image": "codewisdom/ts-time-service:3.0.0"}]}}'



cd ~/train-ticket
mvn  clean package -Dmaven.test.skip=true -pl ts-basic-service -am
cd ~/train-ticket/ts-basic-service || exit
docker build -t codewisdom/ts-basic-service:1.0.0 .
# shellcheck disable=SC2046
kubectl delete pod `kubectl get pods |grep ts-basic-service | awk '{print $1}'`

#cd ~/train-ticket
#mvn  clean package -Dmaven.test.skip=true -pl ts-basic-service -am
#cd ~/train-ticket/ts-basic-service || exit
#docker build -t codewisdom/ts-basic-service:1.0.0 .
## shellcheck disable=SC2046
#kubectl delete pod `kubectl get pods |grep ts-basic-service | awk '{print $1}'`

#kubectl delete deployment  ts-basic-service
#kubectl delete deployment  ts-basic-service
#kubectl delete svc  ts-basic-service
#kubectl delete svc  ts-basic-service
#
#kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/deploy.yaml
#kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/svc.yaml