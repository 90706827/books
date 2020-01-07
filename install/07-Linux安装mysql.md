## CentOS7 安装mysql 5.7
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
UPDATE user SET password=password("newpassword") WHERE user='root'; 
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
