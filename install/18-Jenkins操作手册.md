# Jenkins操作手册

## 安装

### Docker系统

```dockerfile
#启动
docker run -u root --rm -d -p 8080:8080 -p 50000:50000 -v jenkins-data:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkinsci/blueocean
#查看运行jenkins ID
docker ps 
#查看日志
docker logs ID
#日志中0af1bb2dff284b8787154e7784c6e840为第一次管理员登录密码
*************************************************************
*************************************************************
*************************************************************

Jenkins initial setup is required. An admin user has been created and a password generated.
Please use the following password to proceed to installation:

0af1bb2dff284b8787154e7784c6e840

This may also be found at: /var/jenkins_home/secrets/initialAdminPassword

*************************************************************
*************************************************************
*************************************************************

```

### Linux系统

```sh
#安装jdk1.8版本以上

#下载https://pkg.jenkins.io/redhat-stable/
wget https://prodjenkinsreleases.blob.core.windows.net/redhat-stable/jenkins-2.190.1-1.1.noarch.rpm
#安装
rpm -ih jenkins-2.190.1-1.1.noarch.rpm
# 启动开启
chkconfig jenkins on
# 启动jenkins
service jenkins stop
service jenkins start
service jenkins restart
# 修改端口
vim /etc/sysconfig/jenkins
JENKINS_PORT="8080"
#查看管理员密码
cat /var/lib/jenkins/secrets/initialAdminPassword
#开放8080端口
firewall-cmd --zone=public --add-port=8081/tcp --permanent
firewall-cmd --reload
firewall-cmd --zone=public --list-ports

## jenkins目录
/usr/lib/jenkins/jenkins.war     WAR包 
/etc/sysconfig/jenkins       　　 配置文件
/var/lib/jenkins/        　　　　   默认的JENKINS_HOME目录
/var/log/jenkins/jenkins.log      Jenkins日志文件



##卸载
rpm -qa |grep jenkins
rpm -e jenkins-2.190.1-1.1.noarch
```

## 设置

http://localhost:8080

### 解锁

输入管理员密码

### 插件

安装建议插件

```sh
Maven Integration
Git plugin
Publish Over SSH
SSH Pipeline Steps
Pipeline Maven Integration
Static Analysis Utilities
FindBugs
Checkstyle
pmd
```

### SSH

```sh
ssh-keygen -t rsa -C "90706827@163.com"
#输入名字
id_rsa
## 配置jenkins时使用
cat id_rsa
## 配置github时使用
cat id_rsa.pub
```



### 设置管理员

设置管理员用户名 密码 邮箱等；

### 邮箱

![](img\jenkins-email.png)

### 配置

```sh
# 配置jdk
## 查看java路径
echo $JAVA_HOME
/usr/local/java/jdk1.8.0_221
## 名称路径
jdk1.8.0_221
/usr/local/java/jdk1.8.0_221


# 配置maven
## 名称路径
maven3.6.2
/usr/local/maven3

# 配置git
git
/usr/local/git

# 配置ant
ant
/usr/local/ant


```

#### Maven

[官网下载](http://maven.apache.org/download.cgi)

```sh

#下载 apache-maven-3.6.2-bin.tar.gz
tar -zxvf apache-maven-3.6.2-bin.tar.gz -C /usr/local

mv apache-maven-3.6.2 maven3
#配置环境变量
vi /etc/profile
#追加
MAVEN_HOME=/usr/local/maven3
export MAVEN_HOME
export PATH=${PATH}:${MAVEN_HOME}/bin
#生效
source /etc/profile

#验证
mvn -v

/usr/local/maven3
```

#### Git

[源码下载地址](https://mirrors.edge.kernel.org/pub/software/scm/git/)

```sh
# yum 安装git
yum install git

# 源码安装
#解压缩
tar -zxvf git-2.9.5.tar.gz
#进入路径
cd git-2.9.5
#配置git安装路径
./configure prefix=/usr/local/git/
#编译并且安装
make && make install

#配置环境变量
vi /etc/profile
# 添加环境变量
export PATH=$PATH:/usr/local/git/bin
#生效
source /etc/profile

#验证
git --version
 
```

#### Ant

[官网下载](http://ant.apache.org/bindownload.cgi)

```sh
#前提安装jdk
#解压
tar -zxvf apache-ant-1.10.7-bin.tar.gz -C /usr/local
# 修改路径
mv apache-ant-1.10.7 ant 

#配置环境变量
vi /etc/profile
# 添加环境变量
export ANT_HOME=/usr/local/ant
export PATH=$PATH:$ANT_HOME/bin

#生效
source /etc/profile

#验证
ant -version
Apache Ant(TM) version 1.10.7 compiled on September 1 2019
```



## 部署

### Pull 项目

```sh
#服务器上创建目录
mkdir git
cd git
#将项目pull到服务器上
git clone https://gitlab.com/hzx/javatest.git

```



## 发布

### Maven构建

#### General



#### 源码管理

![](img\jenkins-ymgl01.png)

#### 构建触发器

![](img\jenkins-gjcfq01.png)

```sh

## 每行由 5 个值组成(空格或TAB分隔)，分别表示分(0-59)、时(0-23)、日(1-31)、月(1-12)、周(0-7, 0/7=周日)
## "M,N" 表示M和N；"M-N" 表示范围[M,N]；"M-N/X" 表示范围[M,N]内每隔X；"*/X" 表示整个范围内每隔X
## 前面提到的M/N/X的值都可以用H(意为Hash)代替，散列值起到随机值的效果，且同一项目取值稳定，这对于项目多时分散压力很有用。
H/10  H(0-8)  *  *  1-5   ## 触发时间: 工作日、Hour为0~8按哈希随机、Minute以10为间隔
H/10  H       *  *  0,6,7 ## 触发时间: 周末、Hour为全天按哈希随机、Minute以10为间隔
## “日程表”修改后，下方会给出下次执行时间点的预告。
```



#### 构建环境



#### Pre Steps



#### Build

![](img\jenkins-build01.png)

#### Post Steps



##### shell脚本

```sh
rm -rf /root/app/javatest-0.0.1-SNAPSHOT.jar
cp -f /var/lib/jenkins/workspace/javatest/target/javatest-0.0.1-SNAPSHOT.jar /root/app
cd /root/app
echo '#!/bin/bash' > start.sh
echo "export BUILD_ID=dontKillMe" >> start.sh
echo  -e "\nAPP_NAME=javatest-0.0.1-SNAPSHOT.jar \npid=\$(ps -ef | grep \${APP_NAME} | grep -v grep | awk '{print \$2}')  \nif [ -z \${pid} ]; then \n nohup java -jar \${APP_NAME} > /dev/null 2>&1 & \n  psjava=\$(ps -ef|grep java) \n  echo \${psjava}  \nelse \n  kill -9 \${pid} \n  echo "\${pid} 进程被杀掉" \nfi" >> start.sh
sh start.sh

```



```sh

#!/bin/bash
#这里可替换为你自己的执行程序，其他代码无需更改
APP_NAME=demo.jar
 
#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh demo.sh [start|stop|restart|status]"
    exit 1
}
 
#检查程序是否在运行
is_exist() { 
    pid=`ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}' `
    #如果不存在返回1，存在返回0
    if [ -z ${pid} ]; then
      nohup java -jar $APP_NAME > /dev/null 2>&1 &
    else
      kill -9 $pid
    fi
}
 
#启动方法
start() {
   is_exist
   if [ $? -eq "0" ]; then
     echo "${APP_NAME} is already running. pid=${pid} ."
   else
     nohup java -jar $APP_NAME > /dev/null 2>&1 &
   fi
}
 
#停止方法
stop() {
   is_exist
   if [ $? -eq "0" ]; then
     kill -9 $pid
   else
     echo "${APP_NAME} is not running"
   fi
}
 
#输出运行状态
status() {
   is_exist
   if [ $? -eq "0" ]; then
     echo "${APP_NAME} is running. Pid is ${pid}"
   else
     echo "${APP_NAME} is not running."
   fi
}
 
#重启
restart() {
   stop
   start
}
 
#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
   "start")
     start
     ;;
   "stop")
     stop
     ;;
   "status")
     status
     ;;
   "restart")
     restart
     ;;
   *)
     usage
     ;;
esac
```



#### 构建设置

![](img\jenkins-gjsz01.png)

#### 构建后操作



## 问题

- Starting Jenkins bash: /usr/bin/java: 是一个目录

  ```sh
  #修改jenkins执行java的路径
  vim  /etc/init.d/jenkins
  candidates="
  /usr/local/java/jdk1.8.0_221/bin/java #此处为加入的java路径
  /etc/alternatives/java
  /usr/lib/jvm/java-1.8.0/bin/java
  /usr/lib/jvm/jre-1.8.0/bin/java
  /usr/lib/jvm/java-1.7.0/bin/java
  /usr/lib/jvm/jre-1.7.0/bin/java
  /usr/bin/java
  
  #重新执行
  systemctl daemon-reload
  service jenkins start 
  ```

  

- 123