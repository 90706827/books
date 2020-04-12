# RibbitMQ

## Linux 安装

```shell

yum -y install vim make gcc gcc-c++ kernel-devel m4 ncurses-devel openssl-devel unixODBC unixODBC-devel httpd python-simplejson wget net-tools socat

vi /etc/yum.repos.d/rabbitmq-erlang.repo

  [rabbitmq-erlang]
name=rabbitmq-erlang
baseurl=https://dl.bintray.com/rabbitmq/rpm/erlang/21/el/7
gpgcheck=1
gpgkey=https://dl.bintray.com/rabbitmq/Keys/rabbitmq-release-signing-key.asc
repo_gpgcheck=0
enabled=1

yum clean all
yum makecache

wget https://dl.bintray.com/rabbitmq/all/rabbitmq-server/3.7.8/rabbitmq-server-3.7.8-1.el7.noarch.rpm

yum -y install rabbitmq-server-3.7.8-1.el7.noarch.rpm

chkconfig rabbitmq-server on

rabbitmq-plugins enable rabbitmq_management

systemctl start rabbitmq-server

vim /etc/rabbitmq/rabbitmq-env.conf

RABBITMQ_MNESIA_BASE=/usr/local/rabbitmq-server/data
RABBITMQ_LOG_BASE=/usr/local/rabbitmq-server/log


mkdir /usr/local/rabbitmq-server
mkdir /usr/local/rabbitmq-server/data
mkdir /usr/local/rabbitmq-server/log
chmod -R 777 /usr/local/rabbitmq-server/

cp /usr/share/doc/rabbitmq-server-3.7.8/rabbitmq.config.example  /etc/rabbitmq/rabbitmq.config

vim /etc/rabbitmq/rabbitmq.config

{tcp_listeners, [5672]}, 
{loopback_users, ["admin"]},
{hipe_compile,true}


rabbitmqctl add_user admin admin
rabbitmqctl set_user_tags admin administrator

#开放端口15672
firewall-cmd --zone=public --add-port=4369/tcp --permanent
firewall-cmd --zone=public --add-port=5672/tcp --permanent
firewall-cmd --zone=public --add-port=15672/tcp --permanent
firewall-cmd --zone=public --add-port=25672/tcp --permanent
#重启防火墙
firewall-cmd --reload
#查看开放端口
firewall-cmd --zone=public --list-ports

service rabbitmq-server status
service rabbitmq-server start
service rabbitmq-server stop


#集群配置
ll -a /var/lib/rabbitmq/
chmod u+w /var/lib/rabbitmq/.erlang.cookie
##统一cookie中的值后 修改回权限
chmod u-w /var/lib/rabbitmq/.erlang.cookie
#修改机器名称
vim /etc/hostname
#两台服务器都修改
vim /etc/hosts
192.168.0.121 rabbitmq_node1
192.168.0.122 rabbitmq_node2
#验证你配置的正确不正确 你只需要在你的node1机器上ping rabbitmq_node2

rabbitmqctl stop_app
#--ram 指定内存节点类型，--disc指定磁盘节点类型
#在rabbitmq_node2节点上执行
rabbitmqctl join_cluster --ram rabbit@rabbitmq_node1
rabbitmqctl start_app
#修改节点类型
rabbitmqctl stop_app
rabbitmqctl change_cluster_node_type disc
rabbitmqctl start_app
rabbitmqctl cluster_status

```

## windows安装

- 安装erlang依赖

  http://www.erlang.org/downloads

- 下载rabbitmq

  http://www.rabbitmq.com/install-windows.html

- 安装rabbitmq管理插件

  ```
  cmd
  cd C:\Program Files\RabbitMQ Server\rabbitmq_server-3.7.9\sbin
  rabbitmq-plugins enable rabbitmq_management
  ```

- http://localhost:15672/

  用户：guest 

  密码：guest

## RabbitMQ常用的命令

- 启动监控管理器：rabbitmq-plugins enable rabbitmq_management
- 关闭监控管理器：rabbitmq-plugins disable rabbitmq_management
- 启动rabbitmq：rabbitmq-service start
- 关闭rabbitmq：rabbitmq-service stop
- 查看所有的队列：rabbitmqctl list_queues
- 清除所有的队列：rabbitmqctl reset
- 关闭应用：rabbitmqctl stop_app
- 启动应用：rabbitmqctl start_app

## 用户和权限设置
- 添加用户：rabbitmqctl add_user username password
- 分配角色：rabbitmqctl set_user_tags username administrator
- 新增虚拟主机：rabbitmqctl add_vhost  vhost_name
- 将新虚拟主机授权给新用户：`rabbitmqctl set_permissions -p vhost_name username “.*” “.*” “.*”`(后面三个”*”代表用户拥有配置、写、读全部权限)

## 角色说明

- 超级管理员(administrator)
   可登陆管理控制台，可查看所有的信息，并且可以对用户，策略(policy)进行操作。
- 监控者(monitoring)
   可登陆管理控制台，同时可以查看rabbitmq节点的相关信息(进程数，内存使用情况，磁盘使用情况等)
- 策略制定者(policymaker)
   可登陆管理控制台, 同时可以对policy进行管理。但无法查看节点的相关信息(上图红框标识的部分)。
- 普通管理者(management)
   仅可登陆管理控制台，无法看到节点信息，也无法对策略进行管理。
- 其他
   无法登陆管理控制台，通常就是普通的生产者和消费者。