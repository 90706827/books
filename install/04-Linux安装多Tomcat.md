##CentOS 7 多Tomcat安装
[Tomcat官网](http://tomcat.apache.org)

下载tomcat并拷贝tomcat到制定路径 拷贝两份
```
cd /var
wget https://mirrors.tuna.tsinghua.edu.cn/apache/tomcat/tomcat-8/v8.5.32/bin/apache-tomcat-8.5.32.tar.gz 
tar -zxvf apache-tomcat-8.5.32.tar.gz 
mv apache-tomcat-8.5.32 tomcat1
tar -zxvf apache-tomcat-8.5.32.tar.gz 
mv apache-tomcat-8.5.32 tomcat2
rm -rf apache-tomcat-8.5.32.tar.gz 
```
修改server.xml文件
```
vim /mnt/tomcat2/conf/server.xml
8005端口修改为8006
8009端口修改为8010
8080端口修改为8081
Ctrl+c 保存退出 wq
```
启动tomcat
```
sh /mnt/tomcat1/bin/startsh.sh
sh /mnt/tomcat1/bin/startsh.sh
```
查看启动进程
```shell
ps -ef|grep java

# 也可以使用curl命令检查
curl localhost：8080
```
开放端口

```shell
firewall-cmd --zone=public --add-port=8080/tcp --permanent
```

