# Docker安装与应用

## 安装Docker

- 常用插件

```sh
yum -y install vim wget cmake make unzip zip perl nodejs gcc* links* gcc-c++ build-essential zlib1g-devel libssl-devel libgdbm-devel libreadline-devel libncurses5-devel  openssh-server redis-server checkinstall lsb libxml2-devel libxslt-devel libcurl4-openssl-devel libicu-devel telnet logrotate python-docutils pkg-config autoconf libyaml-devel gdbm-devel ncurses-devel openssl* openssl-devel zlib* zlib-devel net-tools readline-devel curl curl-devel expat-devel gettext-devel  tk-devel libffi-devel sendmail patch libyaml* pcre* pcre-devel policycoreutils openssh-clients postfix policycoreutils-python lrzsz yum-utils device-mapper-persistent-data lvm2
## docker 必须yum-utils device-mapper-persistent-data lvm2
## 更新
yum -y update
```

- 升级Bash

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

- 安装

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

- 阿里云镜像加速器配置

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

## 安装Portainer

### 运行Portainer

```sh
mkdir -p /root/portainer/data
docker run -d -p 9000:9000
--restart=always \
--name portainer \
-v /var/run/docker.sock:/var/run/docker.sock \
-v /root/portainer/data:/data docker.io/portainer/portainer
```

### 链接远程Docker

#### 配置远程Docker

```sh
## 开启docker的tcp链接方式
## 编辑docker.service
vim /usr/lib/systemd/system/docker.service
## 修改ExecStart字段 https://www.kubernetes.org.cn/5883.html
## 设置有问题
ExecStart=/usr/bin/dockerd-current -H tcp://0.0.0.0:2375 -H unix://var/run/docker.sock 
## 重新读取docker配置文件，
systemctl daemon-reload
## 重新启动docker服务
systemctl restart docker
## 开放防火墙端口
firewall-cmd --zone=public --add-port=2375/tcp --permanent
## 刷新防火墙
firewall-cmd --reload
```

## 安装Swarm



## 安装Mysql

### 创建路径

```sh
mkdir -p /root/mysql/data
mkdir -p /root/mysql/logs
mkdir -p /root/mysql/conf
```

### 创建配置文件

```sh
sudo tee /root/my.cnf <<-'EOF'
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
-v /root/mysql/conf/:/etc/mysql \
-v /root/mysql/my.cnf:/etc/mysql/my.cnf \
-v /root/mysql/logs/:/var/log/mysql \
-v /root/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=YU8K3HDF8SH324K2347SJ \
-p 3306:3306 \
-d mysql:latest
```

### 配置

```sh
## 进入mysql容器
docker exec -it mysql /bin/bash
## 登录
mysql -u root -p
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
create user 'userroot'@'%' identified by 'YU8K3HDF8SH324K2347SJ';
grant all privileges on *.* to userroot@'%' with grant option;
```

### 安装Nginx

```sh
mkdir -p /root/nginx/html
mkdir -p /root/nginx/conf/conf.d
mkdir -p /root/nginx/logs

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
## 创建nginx.id
sudo tee /root/nginx/conf/nginx.pid <<-'EOF'
1
EOF
## 创建 nginx.conf
sudo tee /root/nginx/conf/nginx.conf <<-'EOF'
user  root;
worker_processes  auto;

error_log  /var/logs/nginx/error.log warn;
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

    access_log  /var/logs/nginx/access.log  main;

    sendfile			      on;
    tcp_nodelay         on;
    keepalive_timeout   65;
    types_hash_max_size 2048;
    client_max_body_size 20m;
    include /etc/nginx/conf/conf.d/*.conf;
}
EOF
## 创建默认conf
sudo tee /root/nginx/conf/conf.d/default.conf <<-'EOF'
server {
    listen       80;
    server_name  localhost;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
    }
}
EOF
## 创建 mime.types
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
## 构建
docker pull nginx:latest
## 只读:ro
## 运行
docker run \
--restart=always \
--name nginx \
-v /root/nginx/html:/usr/share/nginx/html:ro \
-v /root/nginx/logs:/var/logs/nginx \
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



## 安装Redis

```sh
docker pull redis:latest
docker run -p 6379:6379 \
--restart=always \
--name redis \
-d redis:latest \
-- requirepass="QWER1234ASDF" \

redis-cli -h 127.0.0.1 -p 6379

auth QWER1234ASDF
```































