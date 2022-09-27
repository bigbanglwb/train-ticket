#!/bin/bash
cd ~/train-ticket
git pull
mvn clean package -Dmaven.test.skip=true


svc_list=("order-other" "contacts" "assurance")
#svc_list=("travel" "basic" "station" "train" "route" "price" "seat" "order" "config" "station-food" "food" "consign" "user" "preserve" "train-food" "security" )
# shellcheck disable=SC2068
for svc in ${svc_list[@]}
do
        # kubectl delete deployment ts-${svc}-service
        # kubectl delete svc ts-${svc}-service
        cd ~/train-ticket/ts-${svc}-service || exit
        docker build -t codewisdom/ts-${svc}-service:1.0.0 .
#        kubectl delete pod `kubectl get pods |grep ts-${svc}-service | awk '{print $1}'`
done

kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/deploy.yaml
kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/svc.yaml


#svc_list=("travel" "basic" "station")
## shellcheck disable=SC2068
#for svc in ${svc_list[@]}
#do
#        kubectl delete deployment ts-"${svc}"-service
#        kubectl delete svc ts-"${svc}"-service
#        cd ~/train-ticket/ts-"${svc}"-service || exit
#        # shellcheck disable=SC2086
#        docker build -t bigbanglwb/ts-${svc}-service:1.0.0 .
##        kubectl delete pod `kubectl get pods |grep ts-${svc}-service | awk '{print $1}'`
#done
#
# kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/deploy.yaml
# kubectl apply -f /home/liwenbo/train-ticket/deployment/kubernetes-manifests/quickstart-k8s/yamls/svc.yaml


