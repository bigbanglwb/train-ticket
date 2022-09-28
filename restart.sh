#!/bin/bash
cd ~/train-ticket
git pull
mvn clean package -Dmaven.test.skip=true

python3 ./build_upload_image.py
kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/deploy.yaml
kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/svc.yaml




