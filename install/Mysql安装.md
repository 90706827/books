# CentOS7 安装mysql 5.7

[MySql官网地址](https://www.mysql.com/)

![](img\20171204202649723.png)

![](img\20180808221403.png)

```shell
wget https://cdn.mysql.com//archives/mysql-5.7/mysql-5.7.18-1.el7.x86_64.rpm-bundle.tar
```

## 已经安装了mysql卸载操作

```shell
[root@test1 files]# service mysql stop 
Shutting down MySQL.. SUCCESS!
[root@test1 files]# chkconfig  mysql  off
[root@test1 files]# rpm -qa | grep mysql
MySQL-client-5.6.24-1.el6.x86_64
MySQL-server-5.6.24-1.el6.x86_64
MySQL-devel-5.6.24-1.el6.x86_64
[root@test1 files]# rpm -e --nodeps MySQL-client-5.6.24-1.el6.x86_64 MySQL-server-5.6.24-1.el6.x86_64 MySQL-devel-5.6.24-1.el6.x86_64
```

## 编辑shell脚本

mysql-install.sh 文件内容（创建sh文件把内容写进去文件名）
文件名字不同可以修改文件内容

```shell
vi mysql-install.sh
i
```

拷贝内容  **适用5.7版本**

```
#!/bin/bash
mkdir mysql
tar -xvf mysql-5.7.18-1.el7.x86_64.rpm-bundle.tar -C ./mysql
cd mysql  && yum remove mysql-libs -y
rpm -ivh mysql-community-common-5.7.18-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-5.7.18-1.el7.x86_64.rpm 
rpm -ivh mysql-community-client-5.7.18-1.el7.x86_64.rpm 
rpm -ivh mysql-community-server-5.7.18-1.el7.x86_64.rpm
rpm -ivh mysql-community-devel-5.7.18-1.el7.x86_64.rpm
cd ../
#cp binary_log_types.h /usr/include/mysql/
rm -rf mysql
```

mysql-5.7.14-1.el7.x86_64.rpm-bundle.tar
mysql-install.sh
以上两个文件放到相同目录下

```shell
mysql-install.sh
# 没有执行权限增加执行权限
chmod +x  mysql-install.sh
sh mysql-install.sh
```

## 编辑mysql配置文件my.cnf

[mysqld]下面增加skip-grant-tables

```
vi /etc/my.cnf
[mysqld]
skip-grant-tables
：wq! 
保存退出
```

## 重启mysql服务

```
service mysqld restart
```

进入mysql控制台

```
mysql -uroot -p
```

修改root密码:newpassword

```
flush privileges;
grant all on *.* to 'root'@'localhost' identified by 'newpassword' whit grant option;
```

编辑my.cnf文件 删除skip-grant-tables 保存退出

```
vi /etc/my.cnf
[mysqld]
skip-grant-tables
：wq! 
```

## CentOS 7 开启端口

关闭firewaal类型的防火墙,安装iptables类型防火墙
关闭防火墙

```
sudo systemctl stop firewalld.service
```

关闭开机启动

```
sudo systemctl disable firewalld.service
```

安装iptables防火墙

```
sudo yum install iptables-services
```

设置iptables防火墙开机启动

```
sudo systemctl enable iptables
```

开放MySQL访问端口3306

```
vi /etc/sysconfig/iptables 
```

加入端口配置   

```
-A INPUT -m state --state NEW -m tcp -p tcp --dport 3306 -j ACCEPT
```

保存退出

```
:wq!
```

重启防火墙

```
service iptables restart  
```

登陆mysql

```
mysql -uroot -p
密码
use mysql
update user set host='%' where user='root' and host='localhost';
UPDATE user SET password=password("root") WHERE user='root'; 
flush privileges;
exit;
```

重启mysql

```
service mysqld restart;
```



## CentOS7 安装mysql 8.0





安装源文件版本：mysql-8.0.19-linux-glibc2.12-x86_64.tar.xz

mysql安装位置：/software/mysql

数据库文件数据位置：/software/data

 

注：未防止混淆，这里都用绝对路径执行命令

​    除了文件内容中的#,这里所有带#都是linux命令

　　>mysql 是mysql的命令

 

步骤：

1、在根目录下创建文件夹software和数据库数据文件/data/mysql

\#mkdir /software/

\#mkdir /data

\#mkdir /mysql

 

 

2、上传mysql-8.0.19-linux-glibc2.12-x86_64.tar.xz文件到/software下

\--------------------

这里我下载的带了router和test的mysql-8.0.19-linux-glibc2.12-x86_64.tar文件，所以多一部解压xz

\--------------------

\#cd /software/

\#xz -d mysql-8.0.19-linux-glibc2.12-x86_64.tar.xz

\#tar -xvf mysql-8.0.19-linux-glibc2.12-x86_64.tar

 

3、更改解压缩后的文件夹名称

\#mv /software/mysql-8.0.19-linux-glibc2.12-x86_64/* /software/mysql

 

4、创建mysql用户组和mysql用户

\#groupadd mysql

\#useradd -r -g mysql mysql

 

5、关联myql用户到mysql用户组中

\#chown -R mysql:mysql /software/mysql/

\#chown -R mysql:mysql /software/data/

\#chown -R mysql /software/mysql/

\#chown -R mysql  /software/data/

 

6、更改mysql安装文件夹mysql/的权限

\#chmod -R 755 /software/mysql/

 

7、安装libaio依赖包，系统自带的有这个依赖包所以不需要安装，不过自带的依赖包会报错

查询是否暗转libaio依赖包

\#yum search libaio

如果没安装，可以用下面命令安装

\#yum install libaio

 

8、初始化mysql命令

\#cd /software/mysql/bin

\#./mysqld --user=mysql --basedir=/software/mysql --datadir=/software/data --initialize

在执行上面命令时特别要注意一行内容  

```
[Note] A temporary password is generated for root@localhost: cL*3fNuIs:J2
```

root@localhost: 后面跟的是mysql数据库登录的临时密码，各人安装生成的临时密码不一样

如果初始化时报错如下：

error while loading shared libraries: libnuma.so.1: cannot open shared objec

是因为libnuma安装的是32位，我们这里需要64位的，执行下面语句就可以解决

\#yum install numactl.x86_64

执行完后重新初始化mysql命令

 

9、启动mysql服务

\# sh /software/mysql/support-files/mysql.server start

上面启动mysql服务命令是会报错的，因为没有修改mysql的配置文件，报错内容大致如下：

```
./support-files/mysql.server: line 239: my_print_defaults: command ``not` `found
./support-files/mysql.server: line 259: cd: /usr/``local``/mysql: ``No` `such file ``or` `directory
Starting MySQL ERROR! Couldn't find MySQL server (/usr/``local``/mysql/bin/mysqld_safe)
```

 

 

10、修改Mysql配置文件

\#vim /software/mysql/support-files/mysql.server

修改前

if test -z "$basedir"
then
basedir=/usr/local/mysql
bindir=/usr/local/mysql/bin
if test -z "$datadir"
then
datadir=/usr/local/mysql/data
fi
sbindir=/usr/local/mysql/bin
libexecdir=/usr/local/mysql/bin
else
bindir="$basedir/bin"
if test -z "$datadir"
then
datadir="$basedir/data"
fi
sbindir="$basedir/sbin"
libexecdir="$basedir/libexec"
fi

修改后

if test -z "$basedir"
then
basedir=/software/mysql
bindir=/software/mysql/bin
if test -z "$datadir"
then
datadir=/data/mysql
fi
sbindir=/software/mysql/bin
libexecdir=/software/mysql/bin
else
bindir="$basedir/bin"
if test -z "$datadir"
then
datadir="$basedir/data"
fi
sbindir="$basedir/sbin"
libexecdir="$basedir/libexec"
fi

保存退出

\#cp /software/mysql/support-files/mysql.server /etc/init.d/mysqld

\#chmod 755 /etc/init.d/mysqld

11、修改my.cnf文件

\#vi /etc/my.cnf

将下面内容复制替换当前的my.cnf文件中的内容 

```sh

[client]
no-beep
socket =/software/mysql/mysql.sock
# pipe
# socket=0.0
port=3306
[mysql]
#原文的utf8指向UTF8MB3，后续版本要改为UTF8MB4，一步到位吧
default-character-set=UTF8MB4
[mysqld]
default_authentication_plugin=mysql_native_passwor
basedir=/software/mysql
datadir=/software/data
port=3306
pid-file=/software/mysql/mysqld.pid
#skip-grant-tables
skip-name-resolve
socket = /software/mysql/mysql.sock
character-set-server=utf8
default-storage-engine=INNODB
explicit_defaults_for_timestamp = true
# Server Id.
server-id=1
max_connections=2000
 
#query_cache_size在8.0版本已经移除，故注释
#query_cache_size=0
 
table_open_cache=2000
tmp_table_size=246M
thread_cache_size=300
#限定用于每个数据库线程的栈大小。默认设置足以满足大多数应用
thread_stack = 192k
key_buffer_size=512M
read_buffer_size=4M
read_rnd_buffer_size=32M
innodb_data_home_dir = /software/data
innodb_flush_log_at_trx_commit=0
innodb_log_buffer_size=16M
innodb_buffer_pool_size=256M
innodb_log_file_size=128M
innodb_thread_concurrency=128
innodb_autoextend_increment=1000
innodb_buffer_pool_instances=8
innodb_concurrency_tickets=5000
innodb_old_blocks_time=1000
innodb_open_files=300
innodb_stats_on_metadata=0
innodb_file_per_table=1
innodb_checksum_algorithm=0
back_log=80
flush_time=0
join_buffer_size=128M
max_allowed_packet=1024M
max_connect_errors=2000
open_files_limit=4161
 
#query_cache_type在8.0版本已经移除，故注释
#query_cache_type=0
 
sort_buffer_size=32M
table_definition_cache=1400
binlog_row_event_max_size=8K
sync_master_info=10000
sync_relay_log=10000
sync_relay_log_info=10000
#批量插入数据缓存大小，可以有效提高插入效率，默认为8M
bulk_insert_buffer_size = 64M
interactive_timeout = 120
wait_timeout = 120
log-bin-trust-function-creators=1
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
 
 
 
#
# include all files from the config directory
#
!includedir /etc/my.cnf.d
```

保存退出

 

12、启动mysql

\#/etc/init.d/mysqld start

新版本的安装包会报错，错误内容如下：

Starting MySQL.Logging to '/data/mysql/SZY.err'.
2018-07-02T10:09:03.779928Z mysqld_safe The file /usr/local/mysql/bin/mysqld
does not exist or is not executable. Please cd to the mysql installation
directory and restart this script from there as follows:
./bin/mysqld_safe&
See http://dev.mysql.com/doc/mysql/en/mysqld-safe.html for more information
ERROR! The server quit without updating PID file (/software/mysql/mysqld.pid).

因为新版本的mysql安全启动安装包只认/usr/local/mysql这个路径。

解决办法：

方法1、建立软连接

例 #cd /usr/local/mysql

\#ln -s /sofware/mysql/bin/myslqd mysqld

 

方法2、修改mysqld_safe文件（有强迫症的同学建议这种，我用的这种）

\# vim /software/mysql/bin/mysqld_safe

将所有的/usr/local/mysql改为/software/mysql

保存退出。（可以将这个文件拷出来再修改然后替换）

#问题二 报错

Starting MySQL..... ERROR! The server quit without updating PID file (/software/mysql/mysqld.pid).

方法1：

ps -ef | grep mysql

kill 掉进程

13、登录mysql

\---------------------------------------------------------------------------------------------------------------------------

这里我登陆不了，被denny了，类似这样的错误：

ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password: NO)。

参考https://cloud.tencent.com/developer/article/1188636

改为这个操作：

1.停止mysql数据库
/etc/init.d/mysqld stop
 （或者直接 kill -9 [PID]  杀进程！）

2.执行如下命令

```sql
vi /etc/my.cnf
[mysqld]
skip-grant-tables
：wq! 
保存退出
```

3.使用root登录mysql数据库

```sql
mysql --user=root --password
```

4.更新root密码
最新版MySQL请采用如下SQL：
mysql>  ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root';

5.刷新权限
mysql> FLUSH PRIVILEGES;

6.退出mysql
mysql> quit

```sql
# 去掉skip-grant-tables
# vi /etc/my.cnf
[mysqld]
skip-grant-tables
：wq! 
#保存退出
```



7.重启mysql
/etc/init.d/mysqld restart

8.使用root用户重新登录mysql
mysql -uroot -p
 Enter password: <输入新设的密码newpassword>

\---------------------------------------------------------------------------------------------------------------------------

\#/software/mysql/bin/mysql -u root –p

 

14、

登陆mysql

```
mysql -uroot -p
密码
use mysql
update user set host='%' where user='root' and host='localhost';
UPDATE user SET password=password("root") WHERE user='root'; 
flush privileges;
exit;
```

重启mysql

```
service mysqld restart;
```
## 卸载
```sh
查看
rpm -qa |grep -i mysql

yum remove mysql-community mysql-community-server mysql-community-libs mysql-community-common mysql-community-release
卸载完了在查看一遍 没有卸载的继续执行卸载
查看文件
find / -name mysql
删除文件
rm -rf /usr/share/mysql
```

## 安装

```sh
wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm
mysql-community-release-el7-5.noarch.rpm

rpm -ivh mysql-community-release-el7-5.noarch.rpm

yum install mysql-server

firewall-cmd --zone=public --add-port=3306/tcp --permanent
firewall-cmd --reload
firewall-cmd --zone=public --list-ports

service firewalld status

# 开启

service firewalld start

# 重启

service firewalld restart

# 关闭

service firewalld stop
查看临时密码 
cat /var/log/mysqld.log
--允许外网登录
mysql -uroot -p
密码
use mysql
update user set host='%' where user='root' and host='localhost';
UPDATE user SET password=password("yishuzhijia8225") WHERE user='root';
flush privileges;
exit;

MYSQL 8 安装
rpm -qa | grep mariadb
mariadb-libs-5.5.44-2.el7.centos.x86_64
卸载
rpm -e --nodeps  mariadb-libs-5.5.44-2.el7.centos.x86_64
yum install libaio
yum install net-tools

wget https://cdn.mysql.com//Downloads/MySQL-8.0/mysql-8.0.13-1.el7.x86_64.rpm-bundle.tar
解压 安装顺序
rpm -vih mysql-community-common-8.0.13-1.el7.x86_64.rpm
rpm -vih mysql-community-libs-8.0.13-1.el7.x86_64.rpm
rpm -vih mysql-community-libs-compat-8.0.13-1.el7.x86_64.rpm
rpm -vih mysql-community-client-8.0.13-1.el7.x86_64.rpm
rpm -vih mysql-community-embedded-compat-8.0.13-1.el7.x86_64.rpm
rpm -vih mysql-community-server-8.0.13-1.el7.x86_64.rpm
rpm -vih mysql-community-devel-8.0.13-1.el7.x86_64.rpm

#让MYSQL大小写敏感(1-不敏感，0-敏感)/etc/my.cnf
lower_case_table_names=1
#加密类型
default-authentication-plugin=mysql_native_password

启动
service mysqld start
查看临时密码 
cat /var/log/mysqld.log
登录
mysql -uroot -p 
mysql-8 如下设置(密码必须包含：数字大小写字母特殊字符)
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Yishuzhijia-8225';
quit;
使用新密码重新登录
show databases;
use mysql;
使用root用户可以远程连接
update user set host = '%' where user = 'root';
select host, user, plugin from user;

查看mysql连接端口
show global variables like 'port'; 

用户创建：
mysql> create user 'arthome'@'%' identified by 'ArtHome-8225';
授权：
mysql> grant all privileges on *.* to 'arthome'@'%' with grant option;
查看用户权限：
mysql> select host, user,authentication_string,plugin from user;
```

# Windows 安装mysql8

## 下载

https://dev.mysql.com/downloads/mysql/

下载后解压到指定目录D:\Program Files\mysql

## 配置

mysql 目录下新建my.ini配置文件

```sh
[mysqld]
# 设置3306端口
port=3306
# 设置mysql的安装目录
basedir=D:\Program Files\mysql
# 设置mysql数据库的数据的存放目录
datadir=D:\Program Files\mysql\Data
# 允许最大连接数
max_connections=200
# 允许连接失败的次数。这是为了防止有人从该主机试图攻击数据库系统
max_connect_errors=10
# 服务端使用的字符集默认为UTF8
character-set-server=utf8
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB
# 默认使用“mysql_native_password”插件认证
default_authentication_plugin=mysql_native_password
[mysql]
# 设置mysql客户端默认字符集
default-character-set=utf8
[client]
# 设置mysql客户端连接服务端时默认使用的端口
port=3306
default-character-set=utf8
```

## 安装

```sh
管理员打开 cmd.exe
--配置
mysqld --initialize --console
--记住密码DDj8s_c6&;tf
--安装
mysqld --install
--启动
net start mysql
```

## 修改密码

```sh
--修改密码
mysql -u root -p

ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '新密码';
```































