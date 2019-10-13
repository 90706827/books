# Docker 手册

## Docker 安装

### CentOs 7 安装 Docker-ce 社区版本

#### 安装docker依赖包

```
yum install -y yum-utils device-mapper-persistent-data lvm2 
```

#### 添加Docker-ce 软件源

```
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo 
```

#### 关闭Docker-ce 的边缘版本和测试版本

```
yum-config-manager --enable docker-ce-edge 
yum-config-manager --enable docker-ce-test 
```

#### 更新yum源

```
yum makecache fast 
```

#### 安装Docker-ce（第一种安装方式）

```
yum install docker-ce -y 
```

#### 查看是否安装成功

```
docker version  Client: Version:           18.06.1-ce-rc1 API version:       1.38 Go version:        go1.10.3 Git commit:        0928140 Built:             Wed Aug  8 01:35:58 2018 OS/Arch:           linux/amd64 Experimental:      false Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running? 
```

#### 查看可以提供的安装版本（第二种安装方式）

```
yum list docker-ce --showduplicates|sort -r   
```

#### 安装指定版本的Docker-ce

```
yum install docker-ce-17.09.0.ce -y 
```

#### 使用Docker 脚本安装Docker（第三种方式）

##### 下载Docker 安装脚本

```
curl -fsSL get.docker.com -o get-docker.sh 
```

##### 执行 Docker 脚本使用 aliyun 镜像

```
sh get-docker.sh --mirror Aliyun 
```

### 更换Docker 镜像仓库为国内镜像源

#### 注册Daocloud账号，并登陆Daocloud

[国内镜像源](https://www.daocloud.io/mirror#accelerator-doc)

#### 执行更换Daocloud 国内镜像源命令

```
curl -sSL https://get.daocloud.io/daotools/set_mirror.sh | sh -s http://e674da1e.m.daocloud.io 
```

#### 重启Docker 使国内源生效

```
systemctl restart docker  
```

#### 添加Docker 为随机启动

```
 systemctl enable docker  
```

### Docker 镜像的基础操作

##### 从github 拉取 镜像

命令格式：`docker pull [选项] [Docker Registry 地址[:端口号]/]仓库名[:标签] `Docker 镜像仓库地址：地址的格式一般是 <域名/IP>[:端口号]。默认地址是 Docker Hub。 
仓库名：两段式名称，即 <用户名>/<软件名>。对于 Docker Hub，如果不给出用户名，则默认为 library，也就是官方镜像。 
实例：` docker pull ubuntu  Using default tag: latest latest: Pulling from library/ubuntu c64513b74145: Pull complete  01b8b12bad90: Pull complete  c5d85cf7a05f: Pull complete  b6b268720157: Pull complete  e12192999ff1: Pull complete  Digest: sha256:8c3cced1211d1a566088c0af049cc8cd6f33f5b275e62e6285ca6a13e66a98f0 Status: Downloaded newer image for ubuntu:latest `

##### 列出本地已下载的镜像

```
docker image  REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE ubuntu              latest              735f80812f90        2 weeks ago         83.5MB hello-world         latest              2cb0d9787c4d        4 weeks ago         1.85kB 
```

##### 删除本地镜像

`docker image rm -f 2cb0d9787c4d `根据镜像 ID、镜像名、摘要删除镜像

### 使用Dockerfile构建自己的镜像

#### 创建一个空白的目录并进入此目录

```
mkdir first  cd first/ 
```

#### 上传定制镜像需要的文件到此目录下

```
[root@MiWiFi-R3-srv ~]# cd first/ [root@MiWiFi-R3-srv first]# ll 总用量 12 -rw-r--r--. 1 root root 604 8月  11 15:50 Dockerfile -rw-r--r--. 1 root root 230 8月  11 14:57 index.js -rw-r--r--. 1 root root 228 8月  11 14:58 package.json [root@MiWiFi-R3-srv first]#  
```

#### 编辑Dockerfile文件

```sh
#基础镜像来自于 hub.docker.com
FROM centos

#镜像维护信息 可选择
MAINTAINER ZhangGuoQiang

#指定工作目录 镜像的工作目录
WORKDIR /app 

#将项目相关文件拷贝到app下,如果目录不存在会自动创建  当前目录的文件
COPY index.js /app
COPY package.json /app

#安装命令来自于 nodejs 官网 https://nodejs.org/en/download/package-manager/#enterprise-linux-and-fedora

RUN curl --silent --location https://rpm.nodesource.com/setup_12.x | bash - && yum -y install nodejs && npm install

#暴露端口
EXPOSE 8080

#容器启动命令
CMD ["node", "/app/index.js"]
```

#### 构建镜像

```sh
docker build -t centos-nodejs-1.0 .
```

#### 查看构建成功的images

```sh
docker images
```



#### 运行新构建的images

```sh
#运行
docker run -p 8080:8080 -d centos-nodejs:1.0
# 查看docker进程
docker ps
```



#### 访问8080，验证镜像是否制作成功

```sh
curl http://127.0.0.1:8080
```



**恭喜你镜像制作成功了**

遇到的问题



#### 1. docker build 报错[Warning] IPv4 forwarding is disabled. Networking will not work.

编辑00-system.conf 
vim /usr/lib/sysctl.d/00-system.conf追加以下内容`net.ipv4.ip_forward=1 `重启网卡服务`systemctl restart network `




## Docker 容器基础操作

### Docker操作命令

#### 初次启动 Docker 容器命令

```sh
docker run -d（后台运行容器） -p 8080:8080（绑定端口）centos-nodejs:1.0
```

解释： 
通过docker run 首先从本地查询是否有这个镜像，如果有直接启动本镜像，如果没有镜像，直接从官方镜像下载镜像，并启动容器

#### 启动已停止的Docker 容器命令

```
docker start eab4ad610972（容器ID） 
```

#### 查看正在运行的Docker容器

```
docker ps 
```

#### 查看所有的停止或正在运行的容器

```
docker ps -a 
```

#### 停止Docker 容器

```
 docker stop eab4ad610972(容器ID)
```

#### 查看Docker 容器运行的日志

```
docker logs -f eab4ad610972(容器ID)
```

#### 删除已停止的Docker 容器

```
docker rm eab4ad610972
```

#### 登录容器（类似ssh）

```
docker exec -it eab4ad610972（容器ID） bash(执行的命令)
```

### Docker 仓库

#### 公有仓库

##### hub 注册

[Docker hub 注册地址](https://cloud.docker.com/)

##### 搜索镜像

```
docker search centos 
```

##### 拉取镜像

```
docker pull centos 
```

##### 登录公有仓库

```
docker login 
```

##### 推送镜像到共有仓库

```sh
#给你的镜像打标签
docker tag centos-nodejs:1.0 jangni912/centos-nodejs:1.0 
#查看打好标签的本地镜像
docker images
#推送官方镜像库
docker push jangni912/centos-nodejs
#查看上传镜像是否成功
docker search jangni912                                  `
```

#### 私有仓库



##### 安装私有仓库

```sh
#将私有仓库的存储路径通过-v 参数设置 到/opt/data/registry路径下
docker run -d -p 5000:5000 -v /opt/data/registry:/var/lib/registry registry
```

##### 私有仓库操作

```sh
#标记镜像到私有仓库
docker tag centos-nodejs:1.0 127.0.0.1:5000/centos-nodejs:1.0
#上传镜像到私有仓库
docker push 127.0.0.1:5000/centos-nodejs:1.0
#查看仓库中已经上传的镜像
curl 127.0.0.1:5000/v2/_catalog
#删除本地镜像
docker image rm 127.0.0.1:5000/centos-nodejs:1.0
#从本地仓库下载镜像
docker pull 127.0.0.1:5000/centos-nodejs:1.0
```

### Docker 数据管理

#### 数据卷

##### 创建数据卷

```
docker volume create my-vol 
```

##### 查看所有的数据卷

```
docker volume ls
```

##### 查看数据卷的详细信息

```
docker volume inspect my-vol
```

#### 挂载带有数据卷的容器

```sh
docker run -p 8081:8081 -d --mount source=my-vol,target=/webapp centos-nodejs:1.0

docker ps 
docker exec -it ID bash
df -h
exit
```

##### 查看容器的挂载信息

```sh
docker inspect 5f73feb5e99a
```

##### 删除一个不在使用的数据卷

```sh
docker volume rm my-vol
```

##### 清理所有没有占用的数据卷

```sh
docker volume prune
```

#### 挂载主机目录

##### 挂载主机目录到容器

```
docker run -p 8080:8080 -d --mount type=bind,source=/first,target=/webapp centos-nodejs:1.0
```

##### 查看容器挂载信息

```
docker inspect ecc09d5c7ecc 
```

### Docker 网络配置

#### Docker 基础网络配置

##### 外部访问容器

启动容器时，使用 -P 或 -p 参数来指定端口映射，-P 随机生成本地端口绑定容器指定端口，- 
p手动指定主机端口映射容器端口`docker run -p 8080:8080 -d  centos-nodejs:1.0 `

##### 查看端口映射信息

```
docker ps
```

#### 端口映射说明

##### 默认映射所有地址所有端口

`-p 8080:8080 `效果同外部访问容器

```sh
#映射到本机指定地址的指定端口
docker run -p 127.0.0.1:8080:8080 -d centos-nodejs:1.0
#映射到主机地址的任意端口
docker run -p 127.0.0.1::8080 -d centos-nodejs:1.0
#查看容器端口对应绑定的主机端口
docker port be23197aef2a 8080
```



#### 容器互联

##### 容器互联网络原理

Docker 启动时，会自动在主机上创建一个 docker0 虚拟网桥，实际上是 Linux 的一个 bridge，可以理解为一个软件交换机。它会在挂载到它的网口之间进行转发。同时，Docker 随机分配一个本地未占用的私有网段（在 RFC1918 中定义）中的一个地址给 docker0 接口。比如典型的 172.17.42.1，掩码为 255.255.0.0。此后启动的容器内的网口也会自动分配一个同一网段（172.17.0.0/16）的地址。当创建一个 Docker 容器的时候，同时会创建了一对 veth pair 接口（当数据包发送到一个接口时，另外一个接口也可以收到相同的数据包）。这对接口一端在容器内，即 eth0；另一端在本地并被挂载到 docker0 网桥，名称以 veth 开头（例如 vethAQI2QT）。通过这种方式，主机可以跟容器通信，容器之间也可以相互通信。Docker 就创建了在主机和所有容器之间一个虚拟共享网络。![img](Docker%20%E7%B3%BB%E5%88%97%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B(%E5%9B%9B).resources/http%3A__oez1g2mok.bkt.clouddn.com_network.png)

##### 创建一个自己的虚拟网桥

```sh
docker network create -d bridge my-bridge 
#安装ip命令
yun install -y iproute
```

##### 创建两个链接到新网桥的两个容器

```sh
#相互能够ping通，每台能够获取自己的本网段IP。
docker run -it --name test5 --network my-bridge  centos
docker run -it --name test6 --network my-bridge  centos
#在test6 ping test5
ping test5
#在test 6查看其IP地址
ip a
#在test5 ping test6
ping test6
#在test5 查看其ip 地址
ip a
```




#### Docker Compose 安装

Docker Compose 是什么

docker compose 通过docker-compose.yml是将多个服务（即容器）构建成一个项目，来完成某个需求。Compose 的默认管理对象是项目，通过子命令对项目中的一组容器进行便捷地生命周期管理。

##### 安装epl 软件源

```sh
 yum install -y epel-release 	
```

##### 安装python-pip

```sh
yum install -y python-pip 
```

##### 安装Docker Compose

```sh
pip install docker-compose 
```

##### 查看Docker Compose 版本

```sh
docker-compose version
```

**解决问题：**

```sh
#执行命令 pip install docker-compose 现实如下错误

#ext/_yaml.c:4:20: 致命错误：Python.h：没有那个文件或目录
#     #include "Python.h"

# 执行如下命令解决
yum install python-devel

```



#### Docker Compose 项目构建

**通过Docker compose file 构建 wordpress 应用**

##### 编写 wordpress docker compose file 文件



###### 新建wordpress 文件夹，并切换进入wordpress文件夹

```
[root@localhost ~]# mkdir wordpress [root@localhost ~]# cd wordpress/ [root@localhost wordpress]# ll 总用量 0 [root@localhost wordpress]# pwd /root/wordpress 
```

###### 新建wordpress docker compose file 文件

文件名:docker-compose.yml`version: "3" services:     db:      image: mysql:latest      volumes:        - db_data:/var/lib/mysql      restart: always      environment:        MYSQL_ROOT_PASSWORD: somewordpress        MYSQL_DATABASE: wordpress        MYSQL_USER: wordpress        MYSQL_PASSWORD: wordpress     wordpress:      depends_on:        - db      image: wordpress:latest      ports:        - "80:80"      restart: always      environment:        WORDPRESS_DB_HOST: db:3306        WORDPRESS_DB_USER: wordpress        WORDPRESS_DB_PASSWORD: wordpress  volumes:     db_data: `

##### Docker compose file 字段解析

version：声明构建项目的版本号services：声明此项目有几个服务（容器）构成db、wordpress: 服务名称image: 镜像名称（默认来自于docker官方镜像仓库）

[mysql image 官方地址](https://hub.docker.com/_/mysql/)

[wordpress image 官方地址](https://hub.docker.com/_/wordpress/)

volumes: 挂载主机目录到此服务中并给此目录命名 
使用命名卷必须先在首层docker file 文件中声明

restart： 服务异常后的重启方式

environment： 需要添加什么变量根据官方镜像说明添加

![img](Docker%20%E7%B3%BB%E5%88%97%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B(%E4%BA%94).resources/http%3A__oez1g2mok.bkt.clouddn.com_WX20180819-142733@2x.png)

![img](Docker%20%E7%B3%BB%E5%88%97%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B(%E4%BA%94).resources/http%3A__oez1g2mok.bkt.clouddn.com_WX20180819-142655@2x.png)

ports: 主机端口和服务端口的映射关系

##### 启动wordpress docker compose 项目构建

```
docker-compose up -d  
```

##### 构成成功后的wordpress

![img](Docker%20%E7%B3%BB%E5%88%97%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B(%E4%BA%94).resources/http%3A__oez1g2mok.bkt.clouddn.com_WX20180819-130447@2x.png)

##### wordpress 安装成功后的界面

![img](Docker%20%E7%B3%BB%E5%88%97%E5%9F%BA%E7%A1%80%E6%95%99%E7%A8%8B(%E4%BA%94).resources/http%3A__oez1g2mok.bkt.clouddn.com_QQ20180819-145419@2x.png)

#### Docker compose 命令使用

**Docker compose 指令必须在含有docker-compose.yml或者 docker-compose.yaml的目录执行**

##### 查看compose 文件含有的镜像

```
docker-compose images  ``[root@localhost wordpress]# docker-compose images        Container         Repository    Tag       Image Id      Size  ------------------------------------------------------------------- wordpress_db_1          mysql        latest   29e0ae3b69b9   462 MB wordpress_wordpress_1   wordpress    latest   e2c4085bbc2b   389 MB 
```

##### 列出项目中所有的容器

```
docker-compose ps ``[root@localhost wordpress]# docker-compose ps         Name                       Command               State          Ports        ------------------------------------------------------------------------------------ wordpress_db_1          docker-entrypoint.sh mysqld      Up      3306/tcp, 33060/tcp wordpress_wordpress_1   docker-entrypoint.sh apach ...   Up      0.0.0.0:80->80/tcp  
```

##### 停止项目中正在运行的容器

```
docker-compose stop ``[root@localhost wordpress]# docker-compose stop Stopping wordpress_wordpress_1 ... done Stopping wordpress_db_1        ... done 
```

##### 删除项目中所有停止的容器

```
docker-compose rm ``[root@localhost wordpress]# docker-compose rm Going to remove wordpress_wordpress_1, wordpress_db_1 Are you sure? [yN] y Removing wordpress_wordpress_1 ... done Removing wordpress_db_1        ... done 
```

##### 构建项目镜像并启动

```
docker-compose up -d ``[root@localhost wordpress]# docker-compose up -d Creating wordpress_db_1 ... done Creating wordpress_wordpress_1 ... done 
```

##### 启动compose 项目中的容器

```
docker-compose start ``[root@localhost wordpress]# docker-compose start Starting db        ... done Starting wordpress ... done 
```

##### 指定项目中容器的启动数量

```
docker-compose scale wordpress=3 db=2 ``[root@localhost wordpress]# docker-compose scale wordpress=3 db=2 WARNING: The scale command is deprecated. Use the up command with the --scale flag instead. Creating wordpress_db_1 ... done Creating wordpress_db_2 ... done Creating wordpress_wordpress_1 ... done Creating wordpress_wordpress_2 ... done Creating wordpress_wordpress_3 ... done 
```

##### 查看项目中服务对应绑定的端口号

```
docker-compose port wordpress 80 ``[root@localhost wordpress]# docker-compose port wordpress 80 0.0.0.0:80 
```

##### 查看项目中服务的日志信息

```
docker-compose logs -f wordpress ``[root@localhost wordpress]# docker-compose logs -f wordpress Attaching to wordpress_wordpress_1 wordpress_1  | WordPress not found in /var/www/html - copying now... wordpress_1  | Complete! WordPress has been successfully copied to /var/www/html wordpress_1  | AH00558: apache2: Could not reliably determine the server's fully qualified domain name, using 172.20.0.3. Set the 'ServerName' directive globally to suppress this message wordpress_1  | AH00558: apache2: Could not reliably determine the server's fully qualified domain name, using 172.20.0.3. Set the 'ServerName' directive globally to suppress this message wordpress_1  | [Sun Aug 19 07:13:26.743777 2018] [mpm_prefork:notice] [pid 1] AH00163: Apache/2.4.25 (Debian) PHP/7.2.8 configured -- resuming normal operations wordpress_1  | [Sun Aug 19 07:13:26.743842 2018] [core:notice] [pid 1] AH00094: Command line: 'apache2 -D FOREGROUND' 
```


