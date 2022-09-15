#!/bin/bash
cd ~/train-ticket
git pull
mvn clean package -Dmaven.test.skip=true
cd ts-basic-service
docker build -t bigbanglwb/ts-basic-service:1.0.0 .
cd ../ts-station-service
docker build -t bigbanglwb/ts-station-service:1.0.0 .
kubectl delete pod `kubectl get pods |grep ts-basic-service | awk '{print $1}'`
kubectl delete pod `kubectl get pods |grep ts-station-service | awk '{print $1}'`

# kubectl logs -f `kubectl get pods |grep ts-basic-service | awk '{print $1}'`
# kubectl logs -f `kubectl get pods |grep ts-station-service | awk '{print $1}'`
