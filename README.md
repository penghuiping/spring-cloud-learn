# 说明

本项目将会使用docker镜像的方式打包运行，并且运行在k8s上

## 部署步骤

### 1. 编译镜像

进入bin文件夹

使用下面命令可以编译生成镜像

```
sh docker-package-tool.sh build
```

命令执行完毕以后，会在bin目录下的k8s文件夹中生成对应的yml配置文件，如需修改配置可以直接编辑修改yml，使其对应各自的部署环境


### 2. 往远程docker仓库推送镜像

可以推送到阿里云的docker镜像仓库中

可以修改docker-packagee-tool.sh文件中的

dockerRepositoryIp

dockerRepositoryPort

两个变量，来指定远程仓库的地址

```
sh docker-package-tool.sh push
```

### 3. 清理本地镜像文件(可以跳过，如果不想清理)

使用下面命令可以删除本地的docker镜像文件
```
sh docker-package-tool.sh clean
```

### 4. 配置相关运行环境

由于项目运用了 mysql,redis,kong。请先配置好对应的服务器环境

### 5. 修改bin目录中的k8s目录里的yml,并且运行项目

把项目的bin文件夹上传到对应的可以访问互联网的服务器上。然后修改对应的参数配置，如mysql的ip与端口号，在configmap配置里面。
配置完毕就可以使用下面命令运行

```
kubectl apply -f api.yml
kubectl apply -f admin.yml
```

### 6. 修改kong网关的配置，是他支持反向代理与jwt认证

默认会拦截uri中有secure与insecure关键字的api，存在secure会需要jwt认证，
insecure则不需要。
```
sh kong-config-tool.sh config
```

