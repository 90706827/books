# jdk安装 

## openjdk安装

安装之前先检查一下系统有没有自带open-jdk

依次执行命令：

```shell
rpm -qa |grep java

rpm -qa |grep jdk

rpm -qa |grep gcj
```

如果没有输入信息表示没有安装，如果有信息执行下面命令。

```shell
rpm -qa | grep java | xargs rpm -e --nodeps
```

首先检索包含java的列表

```shell
yum list java*
```

检索1.8的列表

```shell
yum list java-1.8*   
```

安装1.8.0的所有文件

```shell
yum install java-1.8.0-openjdk* -y
```

使用命令检查是否安装成功

```shell
java -version
```

## openjdk卸载

```sh
rpm -qa | grep openjdk
yum -y remove  java-1.8.0-openjdk*
```



## jdk 安装

```sh
#下载文件
wget https://download.oracle.com/otn/java/jdk/8u221-b11/230deb18db3e4014bb8e3e8324f81b43/jdk-8u221-linux-x64.rpm

# 创建java路径
mkdir -p /usr/local/java

#解压缩到指定路径
tar -zxvf jdk-8u221-linux-x64.tar.gz -C /usr/local/java

#文件最后追加如下内容
vi /etc/profile

export JAVA_HOME=/usr/local/java/jdk1.8.0_221
export CLASSPATH=$:CLASSPATH:$JAVA_HOME/lib/
export PATH=$PATH:$JAVA_HOME/bin
#重新加载配置文件
source /etc/profile

java -version
```

