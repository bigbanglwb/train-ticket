#!/bin/bash
service_name=$1
echo ${service_name}
cd ~/train-ticket
mvn  clean package -Dmaven.test.skip=true -pl ts-${service_name}-service -am
cd ~/train-ticket/ts-${service_name}-service || exit
docker build -t codewisdom/ts-${service_name}-service:1.0.0 .
kubectl delete pod `kubectl get pods |grep ts-${service_name}-service | awk '{print $1}'`

