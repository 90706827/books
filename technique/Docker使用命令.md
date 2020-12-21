```shell script
## 安装 docker

curl -fsSL https://mirrors.tuna.tsinghua.edu.cn/docker-ce/linux/debian/gpg | sudo apt-key add -

echo 'deb https://mirrors.tuna.tsinghua.edu.cn/docker-ce/linux/debian/ buster stable' | sudo tee /etc/apt/sources.list.d/docker.list

sudo apt-get update

sudo apt-get install docker-ce

sudo systemctl start docker
sudo systemctl enable docker

## 安装pip

sudo apt-get install python3-pip


docker images 

docker search images 

docker pull images

docker rmi images

docker ps

docker container ls

docker start/stop ContainerID

docker ps -a

docker rm ContainerID

systemctl restart docker


systemctl enable docker

docker update --restart=always xxx

docker exec [containerId] date

## Dockerfile
----------------
FROM java:8
LABEL vendor="EasyTech" version="0.0.2"
ADD easy-cms-0.0.2.jar easycms.jar
EXPOSE 8003
ENTRYPOINT ["java","-jar","easycms.jar"]
----------------
# 进入Dockerfile目录执行 生成images命令
docker build -t easycms .
## 查看images
docker images
## 删除images
docker rmi easycms
## 查看运行的程序
docker ps
## 停止运行程序
docker stop 

docker run -d --name easycms -p 8003:8003 --restart=always \
-v /etc/localtime:/etc/localtime \
-v /root/docker/easy-cms/logs:/root/logs \
-v /root/nginx/html/www.zgcenv.com/upload:/root/upload \
easycms

```