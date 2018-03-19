#!/bin/bash
set -eux

dockerRepositoryIp=192.168.99.100
dockerRepositoryPort=30007
tag=0.0.8-test
moduleName="sewing-admin sewing-api"


if [[ $1 = 'build' ]];then
cd ..
mvn clean install -DskipTests
cd bin
mkdir -p k8s
cp ../admin/target/classes/META-INF/fabric8/kubernetes.yml ./k8s/admin.yml
cp ../api/target/classes/META-INF/fabric8/kubernetes.yml ./k8s/api.yml

registryIp=$(kubectl get svc | grep registry-service | awk '{print $3}')
registryPort=$(kubectl get svc | grep registry-service | awk '{print $5}' | awk -F ":" '{print $1}')

sed 's/'$dockerRepositoryIp':'$dockerRepositoryPort'/'$registryIp':'$registryPort'/g' ./k8s/admin.yml >./k8s/admin1.yml
sed 's/'$dockerRepositoryIp':'$dockerRepositoryPort'/'$registryIp':'$registryPort'/g' ./k8s/api.yml >./k8s/api1.yml

rm -rf ./k8s/admin.yml
rm -rf ./k8s/api.yml

mv ./k8s/admin1.yml ./k8s/admin.yml
mv ./k8s/api1.yml ./k8s/api.yml

elif [[ $1 = 'push' ]];then
for i in $tag
do
    for j in $moduleName
    do
    docker push $dockerRepositoryIp:$dockerRepositoryPort/$j:$i
    done
done
elif [[ $1 = 'clean' ]];then
for i in $tag
do
    for j in $moduleName
    do
    tmp=$(docker images $dockerRepositoryIp:$dockerRepositoryPort/$j:$i | awk '{print $1}' | tail -n 1)
    if [[ $tmp = $dockerRepositoryIp:$dockerRepositoryPort/$j ]];then
        docker rmi $dockerRepositoryIp:$dockerRepositoryPort/$j:$i -f
    fi
    done
done
elif [[ $1 = 'pull' ]];then
for i in $tag
do
    for j in $moduleName
    do
    docker pull $dockerRepositoryIp:$dockerRepositoryPort/$j:$i
    done
done
else
echo "Please use command 'sh deploy-docker-install build' to build docker images"
echo "Please use command 'sh deploy-docker-install push' to push docker images to docker repository"
echo "Please use command 'sh deploy-docker-install clean' to remove local docker images"
echo "Please use command 'sh deploy-docker-install pull' to pull docker images from docker repository"
fi
