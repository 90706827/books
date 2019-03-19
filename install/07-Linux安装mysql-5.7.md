##CentOS7 安装mysql 5.7
##[MySql官网地址](https://www.mysql.com/)

![](img\20171204202649723.png)

![](E:\IdeaWorkspace\java\doc\img\20180808221403.png)

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

##编辑shell脚本

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
编辑mysql配置文件my.cnf
[mysqld]下面增加skip-grant-tables
```
vi /etc/my.cnf
[mysqld]
skip-grant-tables
：wq! 
保存退出
```
重启mysql服务
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
CentOS 7 开启端口
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
