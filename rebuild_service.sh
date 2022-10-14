#!/bin/bash
service_name=$1
echo ${service_name}
#cd ~/train-ticket
#mvn  clean package -Dmaven.test.skip=true -pl ts-basic-service -am
#cd ~/train-ticket/ts-basic-service || exit
#docker build -t codewisdom/ts-basic-service:1.0.0 .
#kubectl delete pod `kubectl get pods |grep ts-basic-service | awk '{print $1}'`

