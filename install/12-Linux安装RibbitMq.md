

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





windows安装rabbit

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