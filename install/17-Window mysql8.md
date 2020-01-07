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

