#!/bin/bash
cd ~/train-ticket
git pull
kubectl delete -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/deploy.yaml
kubectl delete -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/svc.yaml


python3 ./compile_and_build_image.py


kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/deploy.yaml
kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/svc.yaml




