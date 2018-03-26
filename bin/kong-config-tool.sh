#!/bin/bash
set -eux
#base_url=http://192.168.99.100:31633
base_url=http://192.168.1.128:32299
## upstream:config virtual_host,and one virtual_host can bind with many targets
api_virtual_host=api-php
admin_virtual_host=admin-php

## target:targets are the backend services.kong will proxy requests to these targets
api_target=192.168.1.47:20001
api_weight=100

admin_target=192.168.1.47:20000
admin_weight=100

## proxy api:proxy api--->upstream--->target
project_name=kongtest
temp0="api admin"
temp1="secure insecure"


if [[ $1 = 'config' ]];then
## 添加一个 upstream
curl -X POST $base_url/upstreams/ \
    --data "name=$api_virtual_host"

curl -X POST $base_url/upstreams/ \
    --data "name=$admin_virtual_host"

## 添加一个target
curl -X POST $base_url/upstreams/$api_virtual_host/targets \
    --data "target=$api_target" \
    --data "weight=$api_weight"

curl -X POST $base_url/upstreams/$admin_virtual_host/targets \
    --data "target=$admin_target" \
    --data "weight=$admin_weight"

## 添加反向代理api
for j in $temp1
do
curl -X POST $base_url/apis/ \
    --data "name=${project_name}_api_$j" \
    --data "upstream_url=http://$api_virtual_host/api/$j" \
    --data "uris=/${project_name}/api/$j"
done

for j in $temp1
do
curl -X POST $base_url/apis/ \
    --data "name=${project_name}_admin_$j" \
    --data "upstream_url=http://$admin_virtual_host/admin/$j" \
    --data "uris=/$project_name/admin/$j"
done

## 给某个反向代理api设置jwt认证
for i in $temp0
do
curl -X POST $base_url/apis/${project_name}_${i}_secure/plugins \
    --data "name=jwt" \
    --data "config.claims_to_verify=exp" \
    --data "config.secret_is_base64=true"
done

## 提示输出
echo "Now you can visit your web site using "
for i in $temp0
do
    for j in $temp1
    do
    echo "http://{kong_proxy_ip:port}/$project_name/$i/$j/{xxxx}"
    done
done

elif [[ $1 = 'clean' ]];then
## 删除所有的upstream
curl -X DELETE $base_url/upstreams/$api_virtual_host
curl -X DELETE $base_url/upstreams/$admin_virtual_host

## 删除所有的proxy api

for i in $temp0
do
    for j in $temp1
    do
    curl -X DELETE $base_url/apis/${project_name}_${i}_$j
    done
done

## 删除plugins
else
echo "Please use command 'sh kong-config-tool config' to config kong"
echo "Please use command 'sh kong-config-tool clean' to clean kong config"
fi