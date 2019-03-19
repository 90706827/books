# CentOS安装jdk1.8

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



