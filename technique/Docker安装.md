# Docker安装与应用

- Linux系统信息

  ```sh
  ## cat /etc/redhat-release 
  CentOS Linux release 7.9.2009 (Core)
  ## uname -r
  3.10.0-1160.11.1.el7.x86_64
  ```

## 安装Docker

### 常用插件

```sh
yum -y install vim wget cmake make unzip zip perl nodejs gcc* links* gcc-c++ build-essential zlib1g-devel libssl-devel libgdbm-devel libreadline-devel libncurses5-devel  openssh-server redis-server checkinstall lsb libxml2-devel libxslt-devel libcurl4-openssl-devel libicu-devel telnet logrotate python-docutils pkg-config autoconf libyaml-devel gdbm-devel ncurses-devel openssl* openssl-devel zlib* zlib-devel net-tools readline-devel curl curl-devel expat-devel gettext-devel  tk-devel libffi-devel sendmail patch libyaml* pcre* pcre-devel policycoreutils openssh-clients postfix policycoreutils-python lrzsz  cpio perl-ExtUtils-CBuilder perl-ExtUtils-MakeMaker yum-utils device-mapper-persistent-data lvm2 epel-release python-pip device-mapper-persistent-data
## docker 必须yum-utils device-mapper-persistent-data lvm2
## 更新
yum -y update
```

### 升级Bash

[下载地址](http://ftp.gnu.org/gnu/bash/)

```sh
## 当前最新版本5.1
mkdir bash && cd bash
wget http://ftp.gnu.org/gnu/bash/bash-5.1.tar.gz
tar -zxvf bash-5.1.tar.gz
cd bash-5.1
./configure && make && make install
bash --version
```

### 安装

```sh
## 安装必要的一些系统工具
yum install -y yum-utils device-mapper-persistent-data lvm2
## 添加软件源信息
yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
## 更新并安装Docker-CE
yum makecache fast
yum -y install docker-ce
## 开启Docker服务
service docker start
## 设置Docker开机启动
systemctl enable docker.service

# 注意：
# 官方软件源默认启用了最新的软件，您可以通过编辑软件源的方式获取各个版本的软件包。例如官方并没有将测试版本的软件源置为可用，您可以通过以下方式开启。同理可以开启各种测试版本等。
# vim /etc/yum.repos.d/docker-ee.repo
#   将[docker-ce-test]下方的enabled=0修改为enabled=1
#
# 安装指定版本的Docker-CE:
# Step 1: 查找Docker-CE的版本:
# yum list docker-ce.x86_64 --showduplicates | sort -r
#   Loading mirror speeds from cached hostfile
#   Loaded plugins: branch, fastestmirror, langpacks
#   docker-ce.x86_64            17.03.1.ce-1.el7.centos            docker-ce-stable
#   docker-ce.x86_64            17.03.1.ce-1.el7.centos            @docker-ce-stable
#   docker-ce.x86_64            17.03.0.ce-1.el7.centos            docker-ce-stable
#   Available Packages
# Step2: 安装指定版本的Docker-CE: (VERSION例如上面的17.03.0.ce.1-1.el7.centos)
# sudo yum -y install docker-ce-[VERSION]
```

### 配置阿里云镜像加速器

[镜像地址](https://cr.console.aliyun.com/cn-shanghai/instances/mirrors)

```sh
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
"registry-mirrors": ["https://你的加速器地址.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```

### 安装docker-compose

```sh
## 下载 
curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
## 下载慢可自行下载拷贝到/user/local/bin目录下改名 
## 下载页面 https://github.com/docker/compose/releases
cp docker-compose-Linux-x86_64 /usr/local/bin/docker-compose
## 将可执行权限应用于二进制文件
chmod +x /usr/local/bin/docker-compose
## 建立软连接 
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
## 查看版本
docker-compose -version
```



## 搭建Portainer集群

### 环境配置

| 主机名称     | IP地址       | 节点     |
| ------------ | ------------ | -------- |
| docker-node1 | 192.168.1.23 | 管理节点 |
| docker-node2 | 192.168.1.24 | 工作节点 |
| docker-node3 | 192.168.1.25 | 工作节点 |

#### node1

```sh
hostnamectl set-hostname docker-node1
echo "docker-node2" > /etc/hostname
vim /etc/hosts
192.168.1.23    docker-node1
192.168.1.24    docker-node2
192.168.1.25    docker-node3

## 开启docker的tcp链接方式
## 编辑docker.service
vim /usr/lib/systemd/system/docker.service
## 修改ExecStart字段 https://www.kubernetes.org.cn/5883.html
## 设置有问题
ExecStart=/usr/bin/dockerd -H fd:// -H tcp://0.0.0.0:2375 --containerd=/run/containerd/containerd.sock
## 重新读取docker配置文件，
systemctl daemon-reload
## 重新启动docker服务
systemctl restart docker
## 开放防火墙端口 或关闭防火墙
firewall-cmd --zone=public --add-port=2375/tcp --permanent
firewall-cmd --zone=public --add-port=7946/udp --permanent
firewall-cmd --zone=public --add-port=4789/udp --permanent
## 刷新防火墙
firewall-cmd --reload
## 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld
## 测试关闭
firewall-cmd --state
not running
## 关闭selinux
## 临时关闭
setenforce 0
## 查看状态
getenforce
## 临时开启
setenforce 1
## 永久关闭 修改如下
vi /etc/sysconfig/selinux
SELINUX=disabled

```

#### node2

```sh
hostnamectl set-hostname docker-node2
echo "docker-node1" > /etc/hostname
vim /etc/hosts
192.168.1.23    docker-node1
192.168.1.24    docker-node2
192.168.1.25    docker-node3

## 开启docker的tcp链接方式
## 编辑docker.service
vim /usr/lib/systemd/system/docker.service
## 修改ExecStart字段 https://www.kubernetes.org.cn/5883.html
## 设置有问题
ExecStart=/usr/bin/dockerd -H fd:// -H tcp://0.0.0.0:2375 --containerd=/run/containerd/containerd.sock
## 重新读取docker配置文件，
systemctl daemon-reload
## 重新启动docker服务
systemctl restart docker
## 开放防火墙端口 或关闭防火墙
firewall-cmd --zone=public --add-port=2375/tcp --permanent
firewall-cmd --zone=public --add-port=7946/udp --permanent
firewall-cmd --zone=public --add-port=4789/udp --permanent
## 刷新防火墙
firewall-cmd --reload
## 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld
## 测试关闭
firewall-cmd --state
not running
## 关闭selinux（这里临时关闭）
## 临时关闭
setenforce 0
## 查看状态
getenforce
## 临时开启
setenforce 1
## 永久关闭 修改如下
vi /etc/sysconfig/selinux
SELINUX=disabled
```

#### node3

```sh
hostnamectl set-hostname docker-node3
echo "docker-node3" > /etc/hostname
vim /etc/hosts
192.168.1.23    docker-node1
192.168.1.24    docker-node2
192.168.1.25    docker-node3

## 开启docker的tcp链接方式
## 编辑docker.service
vim /usr/lib/systemd/system/docker.service
## 修改ExecStart字段 https://www.kubernetes.org.cn/5883.html
## 设置有问题
ExecStart=/usr/bin/dockerd -H fd:// -H tcp://0.0.0.0:2375 --containerd=/run/containerd/containerd.sock
## 重新读取docker配置文件，
systemctl daemon-reload
## 重新启动docker服务
systemctl restart docker
## 开放防火墙端口 或关闭防火墙
firewall-cmd --zone=public --add-port=2375/tcp --permanent
firewall-cmd --zone=public --add-port=7946/udp --permanent
firewall-cmd --zone=public --add-port=4789/udp --permanent
## 刷新防火墙
firewall-cmd --reload
## 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld
## 测试关闭
firewall-cmd --state
not running
## 关闭selinux（这里临时关闭）
## 临时关闭
setenforce 0
## 查看状态
getenforce
## 临时开启
setenforce 1
## 永久关闭 修改如下
vi /etc/sysconfig/selinux
SELINUX=disabled
```



```bash
## 如果开启防火墙，则需要在所有节点的防火墙上依次放行2377/tcp（管理端口）、7946/udp（节点间通信端口）、4789/udp（overlay 网络端口）端口。
```

### 安装Swarm

#### node1

```sh
## 创建镜像
docker pull swarm
## 初始化
docker swarm init --advertise-addr 192.168.1.23
## 得到token 所有集群节点执行此join
docker swarm join --token SWMTKN-1-5u4f9thom3mxit3ey59cr3sdgluqi56bad2g0wrwba5mcxmcke-5jopnh00ammiyv7uuosbf3cnr 192.168.1.23:2377
## 查看集群节点
docker node list
```

#### node2

```sh
## 创建镜像
docker pull swarm
## 执行node1中的token
docker swarm join --token SWMTKN-1-5u4f9thom3mxit3ey59cr3sdgluqi56bad2g0wrwba5mcxmcke-5jopnh00ammiyv7uuosbf3cnr 192.168.1.23:2377

```

#### node3

```sh
## 创建镜像
docker pull swarm
## 执行node1中的token
docker swarm join --token SWMTKN-1-5u4f9thom3mxit3ey59cr3sdgluqi56bad2g0wrwba5mcxmcke-5jopnh00ammiyv7uuosbf3cnr 192.168.1.23:2377

```



### 安装Portainer

#### 创建目录

```sh
## node1节点
mkdir -p /root/portainer/data
```

#### 安装运行

```sh
## 拉取配置
docker pull portainer/portainer
## 运行镜像
docker run \
--name portainer \
--restart=always \
-v /var/run/docker.sock:/var/run/docker.sock \
-v /root/portainer/data:/data \
-p 9000:9000 \
-d portainer/portainer

```

#### 使用

- [访问Protainer](http://192.168.1.23:9000)
- 初始化登录密码
-  集群模式, 一定要选择Remote, 输入docker-node1的ip，然后点击Connect；
-  击左边栏的"Endpoints" - "+add endpoint", 选择"Docker"，添加集群节点；
- Home页可以看到节点信息



## 安装Nginx

### 创建目录

```sh
mkdir -p /root/nginx/html /root/nginx/conf/conf.d /root/nginx/logs
```

### 配置文件

#### 配置默认页面

```sh
sudo tee /root/nginx/html/index.html <<-'EOF'
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>北京欢迎您！</title>
</head>
<body>
<h1>北京欢迎您！</h1>
</body>
</html>
EOF
```

#### 配置nginx.pid

```sh
sudo tee /root/nginx/conf/nginx.pid <<-'EOF'
1
EOF
```

#### 配置nginx.conf

```sh
sudo tee /root/nginx/conf/nginx.conf <<-'EOF'
user  root;
worker_processes  auto;

error_log  /var/log/nginx/error.log warn;
pid        	/etc/nginx/conf/nginx.pid;

events {
    worker_connections  1024;
}

http {
    include       /etc/nginx/conf/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile			      on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 2048;
    client_max_body_size 20m;
    include /etc/nginx/conf/conf.d/*.conf;
}
EOF
```

#### 配置default.conf

```sh
sudo tee /root/nginx/conf/conf.d/default.conf <<-'EOF'
server {
    listen       80;
    server_name  localhost;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
    }
    
    error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
    }
}
EOF
```

#### 配置 mime.types

```sh
sudo tee /root/nginx/conf/mime.types <<-'EOF'
types {
    text/html                                        html htm shtml;
    text/css                                         css;
    text/xml                                         xml;
    image/gif                                        gif;
    image/jpeg                                       jpeg jpg;
    application/javascript                           js;
    application/atom+xml                             atom;
    application/rss+xml                              rss;

    text/mathml                                      mml;
    text/plain                                       txt;
    text/vnd.sun.j2me.app-descriptor                 jad;
    text/vnd.wap.wml                                 wml;
    text/x-component                                 htc;

    image/png                                        png;
    image/svg+xml                                    svg svgz;
    image/tiff                                       tif tiff;
    image/vnd.wap.wbmp                               wbmp;
    image/webp                                       webp;
    image/x-icon                                     ico;
    image/x-jng                                      jng;
    image/x-ms-bmp                                   bmp;

    font/woff                                        woff;
    font/woff2                                       woff2;

    application/java-archive                         jar war ear;
    application/json                                 json;
    application/mac-binhex40                         hqx;
    application/msword                               doc;
    application/pdf                                  pdf;
    application/postscript                           ps eps ai;
    application/rtf                                  rtf;
    application/vnd.apple.mpegurl                    m3u8;
    application/vnd.google-earth.kml+xml             kml;
    application/vnd.google-earth.kmz                 kmz;
    application/vnd.ms-excel                         xls;
    application/vnd.ms-fontobject                    eot;
    application/vnd.ms-powerpoint                    ppt;
    application/vnd.oasis.opendocument.graphics      odg;
    application/vnd.oasis.opendocument.presentation  odp;
    application/vnd.oasis.opendocument.spreadsheet   ods;
    application/vnd.oasis.opendocument.text          odt;
    application/vnd.openxmlformats-officedocument.presentationml.presentation
                                                     pptx;
    application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
                                                     xlsx;
    application/vnd.openxmlformats-officedocument.wordprocessingml.document
                                                     docx;
    application/vnd.wap.wmlc                         wmlc;
    application/x-7z-compressed                      7z;
    application/x-cocoa                              cco;
    application/x-java-archive-diff                  jardiff;
    application/x-java-jnlp-file                     jnlp;
    application/x-makeself                           run;
    application/x-perl                               pl pm;
    application/x-pilot                              prc pdb;
    application/x-rar-compressed                     rar;
    application/x-redhat-package-manager             rpm;
    application/x-sea                                sea;
    application/x-shockwave-flash                    swf;
    application/x-stuffit                            sit;
    application/x-tcl                                tcl tk;
    application/x-x509-ca-cert                       der pem crt;
    application/x-xpinstall                          xpi;
    application/xhtml+xml                            xhtml;
    application/xspf+xml                             xspf;
    application/zip                                  zip;

    application/octet-stream                         bin exe dll;
    application/octet-stream                         deb;
    application/octet-stream                         dmg;
    application/octet-stream                         iso img;
    application/octet-stream                         msi msp msm;

    audio/midi                                       mid midi kar;
    audio/mpeg                                       mp3;
    audio/ogg                                        ogg;
    audio/x-m4a                                      m4a;
    audio/x-realaudio                                ra;

    video/3gpp                                       3gpp 3gp;
    video/mp2t                                       ts;
    video/mp4                                        mp4;
    video/mpeg                                       mpeg mpg;
    video/quicktime                                  mov;
    video/webm                                       webm;
    video/x-flv                                      flv;
    video/x-m4v                                      m4v;
    video/x-mng                                      mng;
    video/x-ms-asf                                   asx asf;
    video/x-ms-wmv                                   wmv;
    video/x-msvideo                                  avi;
}
EOF
```

### 安装运行

```sh
## 拉取镜像
docker pull nginx
## 只读:ro
## 运行
docker run \
--restart=always \
--name nginx \
-v /root/nginx/html:/usr/share/nginx/html:ro \
-v /root/nginx/logs:/var/log/nginx \
-v /root/nginx/conf/conf.d:/etc/nginx/conf/conf.d \
-v /root/nginx/conf:/etc/nginx/conf \
-p 80:80 \
-p 443:443 \
-d nginx:latest

## 开放防火墙端口
firewall-cmd --zone=public --add-port=80/tcp --permanent
firewall-cmd --zone=public --add-port=443/tcp --permanent
## 刷新防火墙
firewall-cmd --reload

```



```sh
docker run -p 80:80 --restart=always --name nginx \
-v /root/nginx/html:/usr/share/nginx/html \
-v /root/nginx/config/nginx.conf:/etc/nginx/nginx.conf \
-v /root/nginx/conf.d:/etc/nginx/conf.d \
-v /root/nginx/logs:/var/log/nginx \
nginx


sudo tee /root/nginx/conf/nginx.conf <<-'EOF'
user  root;
worker_processes  auto;

error_log  /var/log/nginx/error.log warn;
pid        	/var/run/nginx.pid;

events {
    worker_connections  1024;
}

http {
    include       		/etc/nginx/mime.types;
    default_type  		application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile			      on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 2048;
    client_max_body_size 20m;
    include /etc/nginx/conf.d/*.conf;
}
EOF


sudo tee /root/nginx/conf.d/default.conf <<-'EOF'
server {
    listen       80;
    server_name  www.zgcenv.com;

    location / {
        root   /usr/share/nginx/html/www.zgcenv.com;
        index  index.html index.htm;
    }
}
```



## 安装Mysql

### 创建路径

```sh
mkdir -p /root/mysql/data /root/mysql/logs /root/mysql/conf
```

### 配置文件

```sh
sudo tee /root/mysql/my.cnf <<-'EOF'
[mysqld]
user=mysql
character-set-server=utf8
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
expire_logs_days=7
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=1000
[client]
default-character-set=utf8
[mysql]
default-character-set=utf8
EOF
```

### 安装

```sh
docker pull mysql:latest
## -v：主机和容器的目录映射关系，":"前为主机目录，之后为容器目录
## --restart=always： 当Docker 重启时，容器会自动启动。
## --privileged=true：容器内的root拥有真正root权限，否则容器内root只是外部普通用户权限

docker run \
--restart=always \
--privileged=true \
--name mysql \
-v /etc/localtime:/etc/localtime \
-v /etc/timezone:/etc/timezone \
-v /root/mysql/conf/:/etc/mysql \
-v /root/mysql/my.cnf:/etc/mysql/my.cnf \
-v /root/mysql/logs/:/var/log/mysql \
-v /root/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=YU8K3HDF8SH324K2347SJ \
-p 3306:3306 \
-d mysql:latest
```

### 测试数据

```sh
## 进入mysql容器
docker exec -it mysql /bin/bash
## 登录
mysql -u root -pYU8K3HDF8SH324K2347SJ
## 输入密码
## 创建数据库
create database 数据库名;
## 创建远程账户
create user '用户名'@'%' identified by '密码';
## 设置允许访问
grant all privileges on *.* to 用户名@'%' with grant option;
## 刷新权限
flush privileges;
## 退出
exit;

##实例
create database cms;
create user 'cms'@'%' identified by 'cms';
grant all privileges on *.* to userroot@'%' with grant option;
flush privileges;
```

## 安装主从Mysql

### 主数据库

#### 配置

```sh
## 创建目录
mkdir -p /root/mysql/data /root/mysql/conf /root/mysql/logs
## 创建配置
sudo tee /root/mysql/my.cnf <<-'EOF'
[mysqld]
user=mysql
character-set-server=utf8
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
expire_logs_days=7
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=2000
server_id = 1
log-bin= mysql-bin
read-only=0
replicate-ignore-db=mysql
replicate-ignore-db=sys
replicate-ignore-db=information_schema
replicate-ignore-db=performance_schema!includedir /etc/mysql/conf.d/ !includedir /etc/mysql/mysql.conf.d/
[client]
default-character-set=utf8
[mysql]
default-character-set=utf8
EOF
```

#### 安装运行

```sh

## 拉去镜像
docker pull mysql:latest
## 运行镜像
docker run \
--restart=always \
--privileged=true \
--name mysql \
-v /etc/localtime:/etc/localtime \
-v /root/mysql/conf/:/etc/mysql \
-v /root/mysql/my.cnf:/etc/mysql/my.cnf \
-v /root/mysql/logs/:/var/log/mysql \
-v /root/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=1234QWER \
-p 3306:3306 \
-d mysql:latest
```

#### 数据库配置

```sh
## 进入mysql容器
docker exec -it mysql /bin/bash
## 登录
mysql -u root -p1234QWER
# 创建从数据库同步账号slave
create user 'slave'@'%' identified by '1234QWER';
# 赋予权限
grant replication slave on *.* to 'slave'@'%' with grant option;
## 查看状态，记住File、Position的值，在Slave中将用到
show master status;
## 刷新权限
flush privileges;

## 开放防火墙端口
firewall-cmd --zone=public --add-port=3306/tcp --permanent
## 刷新防火墙
firewall-cmd --reload
```

### 从数据库

#### 配置

```sh
mkdir -p /root/mysql/data /root/mysql/conf /root/mysql/logs

sudo tee /root/mysql/my.cnf <<-'EOF'
[mysqld]
user=mysql
character-set-server=utf8
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
expire_logs_days=7
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
max_connections=2000
server_id = 2
log-bin= mysql-bin
read-only=1
replicate-ignore-db=mysql
replicate-ignore-db=sys
replicate-ignore-db=information_schema
replicate-ignore-db=performance_schema!includedir /etc/mysql/conf.d/ !includedir /etc/mysql/mysql.conf.d/
[client]
default-character-set=utf8
[mysql]
default-character-set=utf8
EOF
```

#### 安装运行

```bash
docker pull mysql:latest

docker run \
--restart=always \
--privileged=true \
--name mysql \
-v /etc/localtime:/etc/localtime \
-v /root/mysql/conf/:/etc/mysql \
-v /root/mysql/my.cnf:/etc/mysql/my.cnf \
-v /root/mysql/logs/:/var/log/mysql \
-v /root/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=1234QWER \
-p 3306:3306 \
-d mysql:latest
```

#### 数据库配置

```sh
## 进入mysql容器
docker exec -it mysql /bin/bash
## 登录
mysql -u root -p1234QWER
# 创建从数据库同步账号slave
change master to master_host='192.168.1.23',master_user='slave',master_password='1234QWER',master_log_file='mysql-bin.000004',master_log_pos=0,master_port=3306;
## 刷新权限
flush privileges;
## 启动从库同步
start slave;
## 查看状态 查看 Slave_IO_Running: Yes和Slave_SQL_Running: Yes 都是Yes就没有问题了
show slave status\G;
## 创建远程账户
create user '用户名'@'%' identified by '密码';
## 设置允许访问
grant all privileges on *.* to 用户名@'%' with grant option;
## 刷新权限
flush privileges;
```



### 测试

```sh
## 进入主服务器
## 进入mysql容器
docker exec -it mysql /bin/bash
## 登录
mysql -u root -p1234QWER
##实例
create database cms;
create user 'cms'@'%' identified by 'cms';
grant all privileges on *.* to cms@'%' with grant option;
flush privileges;
exit;

## 同时登陆主从数据库，主数据库建表插入数据，从数据库查看数据
```

## 安装MongoDB

### 安装运行

```sh
## 拉取镜像
docker pull mongo:latest
## 运行 --auth(不设置密码可以去掉)
docker run \
--restart=always \
--name mongo \
-v /root/mongo/configdb:/data/configdb \
-v /root/mongo/db:/data/db \
-p 27017:27017 \
-d mongo:latest \
--auth

```

### 测试数据

```sh
## admin用户进入mongo
docker exec -it mongo bash
docker exec -it mongo mongo admin
## 创建管理员账户密码数据库
db.createUser({ user: 'mongo', pwd: 'QWER1234ASDF', roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] });
## 管理员身份认证
db.auth("mongo","QWER1234ASDF");
## 创建 用户、密码和数据库
db.createUser({ user: '用户名', pwd: '密码', roles: [ { role: "readWrite", db: "数据库名" } ] });
## 对用户身份认证
db.auth("用户名","密码");
## 切换数据库
use 数据库名
## 向数据库test表插入数据
db.test.save({name:"zhangsan"});
## 查询test表数据
db.test.find();
## 退出
exit;

##实例
db.createUser({ user: 'mongo', pwd: 'QWER1234ASDF', roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] });
db.auth("mongo","QWER1234ASDF");
db.createUser({ user: 'zgcenv', pwd: '1234qwer', roles: [ { role: "readWrite", db: "hzx" } ] });
```

## 安装Redis

[官网redis配置文件](http://download.redis.io/redis-stable/redis.conf)

### 创建目录

```sh
mkdir -p /root/redis/data
mkdir -p /root/redis/conf
```

### 配置文件

```sh
sudo tee /root/redis/conf/redis.conf <<-'EOF'
## 注释掉
#bind 127.0.0.1 
## 允许远程连接
protected-mode no
## 默认no，yes意为以守护进程方式启动，这里禁用
daemonize no
## 持久化
appendonly yes
## 客户端闲置多少秒后，断开连接，默认为300（秒）
timeout 300
## 可用数据库数，默认值为16，默认数据库为0
databases 16
## 当有一条Keys数据被改变是，900秒刷新到disk一次
save 900 1
## 当有10条Keys数据被改变时，300秒刷新到disk一次
save 300 10
## 当有1w条keys数据被改变时，60秒刷新到disk一次
save 60 10000
## 当dump .rdb数据库的时候是否压缩数据对象
rdbcompression yes
## 本地数据库文件名，默认值为dump.rdb
dbfilename dump.rdb
## 本地数据库存放路径，默认值为 ./
dir /var/lib/redis/data
## 密码 
requirepass QWER1234ASDF
## 最大客户端连接数，默认不限制
#maxclients 128
## 最大内存使用设置，达到最大内存设置后，Redis会先尝试清除已到期或即将到期的Key，当此方法处理后，任到达最大内存设置，将无法再进行写入操作。
#maxmemory <bytes>
## 是否在每次更新操作后进行日志记录，如果不开启，可能会在断电时导致一段时间内的数据丢失。因为redis本身同步数据文件是按上面save条件来同步的，所以有的数据会在一段时间内只存在于内存中。默认值为no
appendonly no
## 更新日志文件名，默认值为appendonly.aof
#appendfilename
## 更新日志条件，共有3个可选值。
## no 表示等操作系统进行数据缓存同步到磁盘;
## always 表示每次更新操作后手动调用fsync()将数据写到磁盘;
## everysec 表示每秒同步一次（默认值）;
appendfsync everysec

EOF
```

### 安装运行

```sh
## 拉取镜像
docker pull redis
## 运行
docker run \
--name redis \
--restart=always \
--privileged=true \
-v /root/redis/data:/var/lib/redis/data \
-v /root/redis/conf/redis.conf:/etc/redis/redis.conf \
-p 6379:6379 \
-d redis:latest redis-server /etc/redis/redis.conf
## 查看运行
docker ps -a
```

### 测试数据

```sh
## 进入docker的redis数据库
docker exec -it redis redis-cli -h 127.0.0.1 -p 6379
## 认证
auth QWER1234ASDF
## 测试
set name 'zhangsan'
get name
## 退出
exit
```

## 单机Nacos

```sh
docker pull nacos/nacos-server:latest

docker run --name nacos --env MODE=standalone -p 8848:8848 -d nacos/nacos-server:latest
 docker exec -it nacos bash

```

## 集群Nacos

### 数据库配置

```sh
## 进入mysql容器
docker exec -it mysql /bin/bash
## 登录
mysql -u root -p1234QWER
## 创建实例
create database nacos;
create user 'nacos'@'%' identified by 'nacos';
grant all privileges on *.* to nacos@'%' with grant option;
flush privileges;
## 退出
exit;
```

```sh
/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

CREATE SCHEMA nacos AUTHORIZATION nacos;

CREATE TABLE config_info (
  id bigint NOT NULL generated by default as identity,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  tenant_id varchar(128) default '',
  app_name varchar(128),
  content CLOB,
  md5 varchar(32) DEFAULT NULL,
  gmt_create timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
  gmt_modified timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
  src_user varchar(128) DEFAULT NULL,
  src_ip varchar(50) DEFAULT NULL,
  c_desc varchar(256) DEFAULT NULL,
  c_use varchar(64) DEFAULT NULL,
  effect varchar(64) DEFAULT NULL,
  type varchar(64) DEFAULT NULL,
  c_schema LONG VARCHAR DEFAULT NULL,
  constraint configinfo_id_key PRIMARY KEY (id),
  constraint uk_configinfo_datagrouptenant UNIQUE (data_id,group_id,tenant_id));

CREATE INDEX configinfo_dataid_key_idx ON config_info(data_id);
CREATE INDEX configinfo_groupid_key_idx ON config_info(group_id);
CREATE INDEX configinfo_dataid_group_key_idx ON config_info(data_id, group_id);

CREATE TABLE his_config_info (
  id bigint NOT NULL,
  nid bigint NOT NULL generated by default as identity,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  tenant_id varchar(128) default '',
  app_name varchar(128),
  content CLOB,
  md5 varchar(32) DEFAULT NULL,
  gmt_create timestamp NOT NULL DEFAULT '2010-05-05 00:00:00.000',
  gmt_modified timestamp NOT NULL DEFAULT '2010-05-05 00:00:00.000',
  src_user varchar(128),
  src_ip varchar(50) DEFAULT NULL,
  op_type char(10) DEFAULT NULL,
  constraint hisconfiginfo_nid_key PRIMARY KEY (nid));

CREATE INDEX hisconfiginfo_dataid_key_idx ON his_config_info(data_id);
CREATE INDEX hisconfiginfo_gmt_create_idx ON his_config_info(gmt_create);
CREATE INDEX hisconfiginfo_gmt_modified_idx ON his_config_info(gmt_modified);


CREATE TABLE config_info_beta (
  id bigint NOT NULL generated by default as identity,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  tenant_id varchar(128) default '',
  app_name varchar(128),
  content CLOB,
  beta_ips varchar(1024),
  md5 varchar(32) DEFAULT NULL,
  gmt_create timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
  gmt_modified timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
  src_user varchar(128),
  src_ip varchar(50) DEFAULT NULL,
  constraint configinfobeta_id_key PRIMARY KEY (id),
  constraint uk_configinfobeta_datagrouptenant UNIQUE (data_id,group_id,tenant_id));

CREATE TABLE config_info_tag (
  id bigint NOT NULL generated by default as identity,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  tenant_id varchar(128) default '',
  tag_id varchar(128) NOT NULL,
  app_name varchar(128),
  content CLOB,
  md5 varchar(32) DEFAULT NULL,
  gmt_create timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
  gmt_modified timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
  src_user varchar(128),
  src_ip varchar(50) DEFAULT NULL,
  constraint configinfotag_id_key PRIMARY KEY (id),
  constraint uk_configinfotag_datagrouptenanttag UNIQUE (data_id,group_id,tenant_id,tag_id));

CREATE TABLE config_info_aggr (
  id bigint NOT NULL generated by default as identity,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  tenant_id varchar(128) default '',
  datum_id varchar(255) NOT NULL,
  app_name varchar(128),
  content CLOB,
  gmt_modified timestamp NOT NULL DEFAULT '2010-05-05 00:00:00',
  constraint configinfoaggr_id_key PRIMARY KEY (id),
  constraint uk_configinfoaggr_datagrouptenantdatum UNIQUE (data_id,group_id,tenant_id,datum_id));

CREATE TABLE app_list (
 id bigint NOT NULL generated by default as identity,
 app_name varchar(128) NOT NULL,
 is_dynamic_collect_disabled smallint DEFAULT 0,
 last_sub_info_collected_time timestamp DEFAULT '1970-01-01 08:00:00.0',
 sub_info_lock_owner varchar(128),
 sub_info_lock_time timestamp DEFAULT '1970-01-01 08:00:00.0',
 constraint applist_id_key PRIMARY KEY (id),
 constraint uk_appname UNIQUE (app_name));

CREATE TABLE app_configdata_relation_subs (
  id bigint NOT NULL generated by default as identity,
  app_name varchar(128) NOT NULL,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  gmt_modified timestamp DEFAULT '2010-05-05 00:00:00',
  constraint configdatarelationsubs_id_key PRIMARY KEY (id),
  constraint uk_app_sub_config_datagroup UNIQUE (app_name, data_id, group_id));


CREATE TABLE app_configdata_relation_pubs (
  id bigint NOT NULL generated by default as identity,
  app_name varchar(128) NOT NULL,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  gmt_modified timestamp DEFAULT '2010-05-05 00:00:00',
  constraint configdatarelationpubs_id_key PRIMARY KEY (id),
  constraint uk_app_pub_config_datagroup UNIQUE (app_name, data_id, group_id));

CREATE TABLE config_tags_relation (
  id bigint NOT NULL,
  tag_name varchar(128) NOT NULL,
  tag_type varchar(64) DEFAULT NULL,
  data_id varchar(255) NOT NULL,
  group_id varchar(128) NOT NULL,
  tenant_id varchar(128) DEFAULT '',
  nid bigint NOT NULL generated by default as identity,
  constraint config_tags_id_key PRIMARY KEY (nid),
  constraint uk_configtagrelation_configidtag UNIQUE (id, tag_name, tag_type));

CREATE INDEX config_tags_tenant_id_idx ON config_tags_relation(tenant_id);

CREATE TABLE group_capacity (
  id bigint NOT NULL generated by default as identity,
  group_id varchar(128) DEFAULT '',
  quota int DEFAULT 0,
  usage int DEFAULT 0,
  max_size int DEFAULT 0,
  max_aggr_count int DEFAULT 0,
  max_aggr_size int DEFAULT 0,
  max_history_count int DEFAULT 0,
  gmt_create timestamp DEFAULT '2010-05-05 00:00:00',
  gmt_modified timestamp DEFAULT '2010-05-05 00:00:00',
  constraint group_capacity_id_key PRIMARY KEY (id),
  constraint uk_group_id UNIQUE (group_id));

CREATE TABLE tenant_capacity (
  id bigint NOT NULL generated by default as identity,
  tenant_id varchar(128) DEFAULT '',
  quota int DEFAULT 0,
  usage int DEFAULT 0,
  max_size int DEFAULT 0,
  max_aggr_count int DEFAULT 0,
  max_aggr_size int DEFAULT 0,
  max_history_count int DEFAULT 0,
  gmt_create timestamp DEFAULT '2010-05-05 00:00:00',
  gmt_modified timestamp DEFAULT '2010-05-05 00:00:00',
  constraint tenant_capacity_id_key PRIMARY KEY (id),
  constraint uk_tenant_id UNIQUE (tenant_id));

CREATE TABLE tenant_info (
  id bigint NOT NULL generated by default as identity,
  kp varchar(128) NOT NULL,
  tenant_id varchar(128)  DEFAULT '',
  tenant_name varchar(128)  DEFAULT '',
  tenant_desc varchar(256)  DEFAULT NULL,
  create_source varchar(32) DEFAULT NULL,
  gmt_create bigint NOT NULL,
  gmt_modified bigint NOT NULL,
  constraint tenant_info_id_key PRIMARY KEY (id),
  constraint uk_tenant_info_kptenantid UNIQUE (kp,tenant_id));
CREATE INDEX tenant_info_tenant_id_idx ON tenant_info(tenant_id);

CREATE TABLE users (
	username varchar(50) NOT NULL PRIMARY KEY,
	password varchar(500) NOT NULL,
	enabled boolean NOT NULL DEFAULT true
);

CREATE TABLE roles (
	username varchar(50) NOT NULL,
	role varchar(50) NOT NULL,
	constraint uk_username_role UNIQUE (username,role)
);

CREATE TABLE permissions (
    role varchar(50) NOT NULL,
    resource varchar(512) NOT NULL,
    action varchar(8) NOT NULL,
    constraint uk_role_permission UNIQUE (role,resource,action)
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');


/******************************************/
/*   ipv6 support   */
/******************************************/
ALTER TABLE `config_info_tag`
MODIFY COLUMN `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip' AFTER `src_user`;

ALTER TABLE `his_config_info`
MODIFY COLUMN `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL AFTER `src_user`;

ALTER TABLE `config_info`
MODIFY COLUMN `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip' AFTER `src_user`;

ALTER TABLE `config_info_beta`
MODIFY COLUMN `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip' AFTER `src_user`;
```



```sh
#安装pip
yum -y install epel-release
yum -y install python-pip
#确认版本
pip --version
#更新pip
pip install --upgrade pip
#安装docker-compose
pip install docker-compose 
#查看版本
docker-compose version

curl -L https://github.com/docker/compose/releases/download/1.27.4/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
$chmod +x /usr/local/bin/docker-compose
#查看版本
$docker-compose version
```



### Node1配置

```sh
mkdir -p /root/nacos/init.d /root/nacos/compose


sudo tee /root/nacos/init.d/custom.properties <<-'EOF'
management.endpoints.web.exposure.include=*
EOF


sudo tee /root/nacos/compose/docker.yml <<-'EOF'
version: '3'
services:
  # nacos-server服务注册与发现，配置中心服务	
  docker-nacos-server:
    image: nacos/nacos-server:1.4.1
    container_name: nacos-server-1
    ports:
      - "8848:8848"
      - "9555:9555"
    networks: 
      - nacos_net
    restart: on-failure
    privileged: true
    environment:
      PREFER_HOST_MODE: ip #如果支持主机名可以使用hostname,否则使用ip，默认也是ip
      SPRING_DATASOURCE_PLATFORM: mysql #数据源平台 仅支持mysql或不保存empty
      NACOS_SERVER_IP: 192.168.1.23 #多网卡情况下，指定ip或网卡
      NACOS_SERVERS: 192.168.1.23:8848 192.168.1.24:8848 192.168.1.25:8848 #集群中其它节点[ip1:port ip2:port ip3:port]
      MYSQL_MASTER_SERVICE_HOST: 192.168.1.23 #mysql配置，Master为主节点，Slave为从节点
      MYSQL_MASTER_SERVICE_PORT: 3306
      MYSQL_MASTER_SERVICE_DB_NAME: nacos
      MYSQL_MASTER_SERVICE_USER: nacos
      MYSQL_MASTER_SERVICE_PASSWORD: nacos
      MYSQL_SLAVE_SERVICE_HOST: 192.168.1.23
      MYSQL_SLAVE_SERVICE_PORT: 3306
      #JVM调优参数
      #JVM_XMS:  #-Xms default :2g
      #JVM_XMX:  #-Xmx default :2g
      #JVM_XMN:  #-Xmn default :1g
      #JVM_MS:   #-XX:MetaspaceSize default :128m
      #JVM_MMS:  #-XX:MaxMetaspaceSize default :320m
      #NACOS_DEBUG: n #是否开启远程debug，y/n，默认n
      #TOMCAT_ACCESSLOG_ENABLED: true #是否开始tomcat访问日志的记录，默认false
    volumes:
      - ./cluster-logs/nacos1:/home/nacos/logs #日志输出目录
      - ../init.d/custom.properties:/home/nacos/init.d/custom.properties #../init.d/custom.properties内包含很多自定义配置，可按需配置

networks:
  nacos_net:
    driver: bridge
EOF

docker pull nacos/nacos-server:1.4.1

cd /root/nacos/compose
docker-compose -f docker.yml up -d

docker run \
--name nacos \
--restart=always \
--privileged=true \
-e MODE=cluster \
-e NACOS_SERVER_IP=192.168.1.23 \
-e NACOS_SERVERS="192.168.1.23:8848 192.168.1.24:8848 192.168.1.25:8848" \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_MASTER_SERVICE_HOST=192.168.1.23 \
-e MYSQL_MASTER_SERVICE_PORT=3306 \
-e MYSQL_MASTER_SERVICE_USER=nacos \
-e MYSQL_MASTER_SERVICE_PASSWORD=nacos \
-e MYSQL_MASTER_SERVICE_DB_NAME=nacos_config \
-p 8848:8848 \
-p 9555:9555 \
-d nacos/nacos-server:1.4.1

## 开启端口
firewall-cmd --zone=public --add-port=8848/tcp --permanent
## 刷新防火墙
firewall-cmd --reload
```

```sh

mkdir -p /root/nacos/init.d /root/nacos/compose


sudo tee /root/nacos/init.d/custom.properties <<-'EOF'
management.endpoints.web.exposure.include=*
EOF


sudo tee /root/nacos/compose/docker.yml <<-'EOF'
version: '3'
services:
  # nacos-server服务注册与发现，配置中心服务	
  docker-nacos-server:
    image: nacos/nacos-server:1.4.1
    container_name: nacos-server-2
    ports:
      - "8848:8848"
      - "9555:9555"
    networks: 
      - nacos_net
    restart: 
      - on-failure
    privileged: true
    environment:
      PREFER_HOST_MODE: ip #如果支持主机名可以使用hostname,否则使用ip，默认也是ip
      SPRING_DATASOURCE_PLATFORM: mysql #数据源平台 仅支持mysql或不保存empty
      NACOS_SERVER_IP: 192.168.1.24 #多网卡情况下，指定ip或网卡
      NACOS_SERVERS: 192.168.1.23:8848 192.168.1.24:8848 192.168.1.25:8848 #集群中其它节点[ip1:port ip2:port ip3:port]
      MYSQL_MASTER_SERVICE_HOST: 192.168.1.23 #mysql配置，Master为主节点，Slave为从节点
      MYSQL_MASTER_SERVICE_PORT: 3306
      MYSQL_MASTER_SERVICE_DB_NAME: nacos
      MYSQL_MASTER_SERVICE_USER: nacos
      MYSQL_MASTER_SERVICE_PASSWORD: nacos
      MYSQL_SLAVE_SERVICE_HOST: 192.168.1.23
      MYSQL_SLAVE_SERVICE_PORT: 3306
      #JVM调优参数
      #JVM_XMS:  #-Xms default :2g
      #JVM_XMX:  #-Xmx default :2g
      #JVM_XMN:  #-Xmn default :1g
      #JVM_MS:   #-XX:MetaspaceSize default :128m
      #JVM_MMS:  #-XX:MaxMetaspaceSize default :320m
      #NACOS_DEBUG: n #是否开启远程debug，y/n，默认n
      #TOMCAT_ACCESSLOG_ENABLED: true #是否开始tomcat访问日志的记录，默认false
    volumes:
      - ./cluster-logs/nacos2:/home/nacos/logs #日志输出目录
      - ../init.d/custom.properties:/home/nacos/init.d/custom.properties #../init.d/custom.properties内包含很多自定义配置，可按需配置

networks:
  nacos_net:
    driver: bridge
EOF

docker pull nacos/nacos-server:1.4.1

docker run \
--name nacos \
--restart=always \
-e MODE=cluster \
-e NACOS_SERVER_IP=192.168.1.24 \
-e NACOS_SERVER_PORT=8848 \
-e NACOS_SERVERS="192.168.1.23:8848 192.168.1.24:8848 192.168.1.25:8848" \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_MASTER_SERVICE_HOST=192.168.1.23 \
-e MYSQL_MASTER_SERVICE_PORT=3306 \
-e MYSQL_MASTER_SERVICE_USER=nacos \
-e MYSQL_MASTER_SERVICE_PASSWORD=nacos \
-e MYSQL_MASTER_SERVICE_DB_NAME=nacos_config \
-p 8848:8848 \
-d nacos/nacos-server:1.4.1
## 开启端口
firewall-cmd --zone=public --add-port=8848/tcp --permanent
## 刷新防火墙
firewall-cmd --reload
```

```sh

mkdir -p /root/nacos/init.d /root/nacos/compose


sudo tee /root/nacos/init.d/custom.properties <<-'EOF'
management.endpoints.web.exposure.include=*
EOF


sudo tee /root/nacos/compose/docker.yml <<-'EOF'
version: '3'
services:
  # nacos-server服务注册与发现，配置中心服务	
  docker-nacos-server:
    image: nacos/nacos-server:1.4.1
    container_name: nacos-server-3
    ports:
      - "8848:8848"
      - "9555:9555"
    networks: 
      - nacos_net
    restart: on-failure
    privileged: true
    environment:
      PREFER_HOST_MODE: ip #如果支持主机名可以使用hostname,否则使用ip，默认也是ip
      SPRING_DATASOURCE_PLATFORM: mysql #数据源平台 仅支持mysql或不保存empty
      NACOS_SERVER_IP: 192.168.1.25 #多网卡情况下，指定ip或网卡
      NACOS_SERVERS: 192.168.1.23:8848 192.168.1.24:8848 192.168.1.25:8848 #集群中其它节点[ip1:port ip2:port ip3:port]
      MYSQL_MASTER_SERVICE_HOST: 192.168.1.23 #mysql配置，Master为主节点，Slave为从节点
      MYSQL_MASTER_SERVICE_PORT: 3306
      MYSQL_MASTER_SERVICE_DB_NAME: nacos
      MYSQL_MASTER_SERVICE_USER: nacos
      MYSQL_MASTER_SERVICE_PASSWORD: nacos
      MYSQL_SLAVE_SERVICE_HOST: 192.168.1.23
      MYSQL_SLAVE_SERVICE_PORT: 3306
      #JVM调优参数
      #JVM_XMS:  #-Xms default :2g
      #JVM_XMX:  #-Xmx default :2g
      #JVM_XMN:  #-Xmn default :1g
      #JVM_MS:   #-XX:MetaspaceSize default :128m
      #JVM_MMS:  #-XX:MaxMetaspaceSize default :320m
      #NACOS_DEBUG: n #是否开启远程debug，y/n，默认n
      #TOMCAT_ACCESSLOG_ENABLED: true #是否开始tomcat访问日志的记录，默认false
    volumes:
      - ./cluster-logs/nacos3:/home/nacos/logs #日志输出目录
      - ../init.d/custom.properties:/home/nacos/init.d/custom.properties #../init.d/custom.properties内包含很多自定义配置，可按需配置

networks:
  nacos_net:
    driver: bridge
EOF
docker pull nacos/nacos-server:1.4.1

docker run \
--name nacos \
--restart=always \
-e MODE=cluster \
-e NACOS_SERVER_IP=192.168.1.25 \
-e NACOS_SERVER_PORT=8848 \
-e NACOS_SERVERS="192.168.1.23:8848 192.168.1.24:8848 192.168.1.25:8848" \
-e SPRING_DATASOURCE_PLATFORM=mysql \
-e MYSQL_MASTER_SERVICE_HOST=192.168.1.23 \
-e MYSQL_MASTER_SERVICE_PORT=3306 \
-e MYSQL_MASTER_SERVICE_USER=nacos \
-e MYSQL_MASTER_SERVICE_PASSWORD=nacos \
-e MYSQL_MASTER_SERVICE_DB_NAME=nacos_config \
-p 8848:8848 \
-d nacos/nacos-server:1.4.1
## 开启端口
firewall-cmd --zone=public --add-port=8848/tcp --permanent
## 刷新防火墙
firewall-cmd --reload
```

nginx配置

```sh
http{
	upstream nacos-cluster {
		server 192.168.1.23:8848;
		server 192.168.1.24:8848;
		server 192.168.1.25:8848;
	}
	server {
		listen 8848;
		location /{
			proxy_pass http://nacos-cluster;
		}
	}
}
```



## 安装Node

```sh

docker pull node:latest

docker run -itd --name node --restart=always -d node:latest

docker exec -it node bash

node -v

v15.5.1

```



```sh
tee /root/elip/web/Dockerfile <<-'EOF'
# 设置基础镜像,如果本地没有该镜像，会从Docker.io服务器pull镜像
FROM node:v15.5.1
# 设置时区 暂不设置
RUN apk --update add tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata
# 创建app目录
RUN mkdir -p /usr/src/elip/web
# 设置工作目录
WORKDIR /usr/src/elip/web

# 拷贝package.json文件到工作目录
# !!重要：package.json需要单独添加。
# Docker在构建镜像的时候，是一层一层构建的，仅当这一层有变化时，重新构建对应的层。
# 如果package.json和源代码一起添加到镜像，则每次修改源码都需要重新安装npm模块，这样木有必要。
# 所以，正确的顺序是: 添加package.json；安装npm模块；添加源代码。
COPY package.json /usr/src/elip/web/package.json
# 安装npm依赖(使用淘宝的镜像源)
# 如果使用的境外服务器，无需使用淘宝的镜像源，即改为`RUN npm i`。
RUN npm i --registry=https://registry.npm.taobao.org
# 拷贝所有源代码到工作目录
COPY . /usr/src/elip/web
# 暴露容器端口
EXPOSE 7002
# 启动node应用
CMD EGG_SERVER_ENV=prod npm start
EOF

sudo docker build -t elip-web .
## 创建网络
docker network create my-network
# 普通node.js应用
sudo docker run -d --name elip-web -p 7002:7002 elip-web
# eggjs应用
sudo docker run -it --net my-network --name elip-web -v /root/logs/elip-web:/root/logs -p 7002:7002 -d elip-web:latest




docker network create my-bridge
docker run --name mysql --net my-bridge -p 3306:3306 -e MYSQL_ROOT_PASSWORD=pwd -d mysql:5.7
docker run -d -p 8080:8080 --net my-bridge randomapp
```































