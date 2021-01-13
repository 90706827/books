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

-- 创建目录/opt/docker/portainer/data

```sh
docker run -d -p 9000:9000 --restart=always --name portainer -v /var/run/docker.sock:/var/run/docker.sock -v /opt/docker/portainer/data:/data docker.io/portainer/portainer
```

### 链接远程Docker

#### 配置远程Docker

```sh
## 开启docker的tcp链接方式
## 编辑docker.service
vim /usr/lib/systemd/system/docker.service
## 修改ExecStart字段
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



































