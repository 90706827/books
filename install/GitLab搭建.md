## 安装GitLab

### 环境准备

```shell
yum -y install vim wget cmake make nodejs gcc gcc-c++ build-essential zlib1g-devel libssl-devel libgdbm-devel libreadline-devel libncurses5-devel  openssh-server redis-server checkinstall libxml2-devel libxslt-devel libcurl4-openssl-devel libicu-devel logrotate python-docutils pkg-config autoconf libyaml-devel gdbm-devel ncurses-devel openssl-devel zlib-devel readline-devel curl curl-devel expat-devel gettext-devel  tk-devel libffi-devel sendmail patch libyaml* pcre-devel policycoreutils  openssh-clients postfix policycoreutils-python 
```

### 安装jdk

```shell
yum install java-1.8.0-openjdk* -y
java -version
```

### 下载安装文件

```shell
#centos 6系统的下载地址:https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/yum/el6
#centos 7系统的下载地址:https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/yum/el7
wget https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/yum/el7/gitlab-ce-8.0.0-ce.0.el7.x86_64.rpm
```

### 安装

```shell
rpm -i gitlab-ce-8.0.0-ce.0.el7.x86_64.rpm
vim  /etc/gitlab/gitlab.rb
#修改访问路径
external_url 'http://192.168.0.122：8787'

gitlab-ctl reconfigure
# 显示如下表示配置成功
#Running handlers:
#Running handlers complete
#Chef Client finished, 427/611 resources updated in 02 minutes 47 seconds
#gitlab Reconfigured!

gitlab-ctl restart
# 提示“ok: run:”表示启动成功
#ok: run: alertmanager: (pid 8002) 0s
#ok: run: gitaly: (pid 8010) 1s
#ok: run: gitlab-monitor: (pid 8025) 0s
#ok: run: gitlab-workhorse: (pid 8034) 1s
#ok: run: logrotate: (pid 8043) 0s
#ok: run: nginx: (pid 8049) 1s
#ok: run: node-exporter: (pid 8055) 0s
#ok: run: postgres-exporter: (pid 8059) 0s
#ok: run: postgresql: (pid 8141) 0s
#ok: run: prometheus: (pid 8149) 0s
#ok: run: redis: (pid 8158) 1s
#ok: run: redis-exporter: (pid 8165) 0s
#ok: run: sidekiq: (pid 8184) 0s
#ok: run: unicorn: (pid 8204) 0s



vim /opt/gitlab/embedded/service/gitlab-rails/config/gitlab.yml
##编辑IP地址 外网地址
gitlab:
	host: gitlab.arts.com
	port: 80
	https: false
	
vim /etc/gitlab/gitlab.rb 
##编辑IP地址 外网地址
external_url 'http://192.168.0.122:8787'

gitlab-ctl restart
```

### 开放端口

```shell
firewall-cmd --zone=public --add-port=8787/tcp --permanent
firewall-cmd --reload
firewall-cmd --zone=public --list-ports

service firewalld status
# 开启
service firewalld start
# 重启
service firewalld restart
# 关闭
service firewalld stop
```

### 访问

```shell
#访问：http://192.168.0.122:8087
#初始账户: root 密码: 5iveL!fe
```

### 启动异常处理

```shell
#一.登录502报错
##一般是权限问题，解决方法：chmod -R 755 /var/log/gitlab
#二.gitlab-ctl reconfigure
##报错n itdb: could not obtain information about current user: Permission denied
##Error executing action `run` on resource ##'execute[/opt/gitlab/embedded/bin/initdb -D /var/opt/gitlab/postgresql/data -E UTF8]'
##根据报错信息大概锁定用户的权限问题,安装gitlab-ce会自动添加用户四个用户:
##gitlab-www:x:497:498::/var/opt/gitlab/nginx:/bin/false
##git:x:496:497::/var/opt/gitlab:/bin/sh
##gitlab-redis:x:495:496::/var/opt/gitlab/redis:/bin/nologin
##gitlab-psql:x:494:495::/var/opt/gitlab/postgresql:/bin/sh
##google和百度都搜索不到解决方法,既然出错提示到权限问题，那么按照这个方向去查就不会有问题，后来查了文件/etc/passwd的权限是600,给予644权限后,成功解决报错问题

#配饰SSH KEY
#1.打开本地git bash,使用如下命令生成ssh公钥和私钥对
ssh-keygen -t rsa -C 'xxx@xxx.com'
#2.然后打开~/.ssh/id_rsa.pub文件(~表示用户目录，比如我的windows就是C:\Users\Administrator)
#3.打开gitlab,找到Profile Settings-->SSH Keys--->Add SSH Key,并把上一步中复制的内容粘贴到Key所对应的文本框，在Title对应的文本框中给这个sshkey设置一个名字，点击Add key按钮
#4.使用git下载项目
```

