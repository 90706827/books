# MongoDB 安装

## 下载

[MongoDB下载 当前版本4.2.5](https://www.mongodb.com/download-center/community)

- Windows

  **Version：current release；OS：Windows x64；Package：MSI**

- CentOS

  **Version：current release；OS： RHEL 7.0 Linux x64；Package：TGZ**

- Ubuntu

  **Version：current release；OS： Ubuntu 16.04 Linux x64；Package：TGZ**

## Linux

- 创建用户

  ```sh
  mkdir /home/mongodb
  groupadd mongodb
  useradd -s /sbin/nologin -g mongodb -M mongodb
  chown -R mongodb:mongodb /home/mongodb
  chown 754 /home/mongodb
  #为mongodb用户添加密码
  passwd mongodb
  #修改用户信息
  usermod -s /bin/bash username
  ```
  
- 准备

  ```sh
  mkdir /usr/local/mongodb
  mkdir /usr/local/mongodb/data
  mkdir /usr/local/mongodb/log
  cd /usr/local/mongodb 
  groupadd mongodb
  useradd -s /sbin/nologin -g mongodb -M mongodb
  chown -R mongodb:mongodb /usr/local/mongodb
  chown -R mongodb:mongodb /home/mongodb
  chown 754 /home/mongodb
  #为mongodb用户添加密码
  passwd mongodb
  #修改用户信息
  usermod -s /bin/bash username
  ```
  
- 下载

  ```sh
  cd /mnt
  https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-4.2.5.tgz
  ```
  
- 安装

  ```sh
  # 解压缩到安装目录
  tar -zxvf mongodb-linux-x86_64-rhel70-4.2.5.tgz
  # 修改文件名
  cd mongodb-linux-x86_64-rhel70-4.2.5
  mv * /usr/local/mongodb
  ```


- 配置文件

  vim /usr/local/mongodb/bin/mongodb.conf

  ```sh
  dbpath=/usr/local/mongodb/data
  logpath=/usr/local/mongodb/log/mongodb.log
  bind_ip=0.0.0.0
  port=27017
  fork=true
  #auth=true
  ```

- 注册服务

   vim /usr/lib/systemd/system/mongodb.service 

  ```sh
  
  [Unit]
  Description=mongodb
  After=network.target remote-fs.target nss-lookup.target
  
  [Service]
  Type=forking
  ExecStart=/usr/local/mongodb/bin/mongod --config /usr/local/mongodb/bin/mongodb.conf
  ExecReload=/bin/kill -s HUP $MAINPID
  ExecStop=/usr/local/mongodb/bin/mongod --shutdown --config /usr/local/mongodb/bin/mongodb.conf
  PrivateTmp=true
  
  [Install]
  WantedBy=multi-user.target
  ```
  
- 文件权限

  ```sh
  chmod 754 /usr/lib/systemd/system/mongodb.service
  ```

- 开机启动

  ```sh
  systemctl daemon-reload
  systemctl enable mongodb
  ```

- 使用命令

  ```sh
  systemctl start mongodb
  systemctl stop mongodb
  systemctl status mongodb
  ```

- 配置环境变量

   vim /etc/profile

  ```sh
  export MONGODB_HOME=/usr/local/mongodb
  export PAHT=${MONGODB_HOME}/bin:$PATH
  ```

  - 生效

    ```sh
    source /etc/profile
    ```

- 查看版本

  ```sh
  mongod --version
  ```

- 开放端口

  ```sh
  firewall-cmd --zone=public --add-port=27017/tcp --permanent
  firewall-cmd --reload
  firewall-cmd --zone=public --list-ports
  ```





## Windows

执行 .msi文件

- 选择 Custom（自定义）；
- 点击Browse...(选择安装目录) ，下一步；
  
- D:\MongoDB
  
- 勾选 Install MongoD as a Service

  - 勾选 Run service as Network Service user
  - Service Name：MongoDB
  - Data Directory：自定义数据存储目录
  - Log Directory：自定义日志存储目录

- 勾掉 Install MongoDB Compass，下一步；

- 安装完成

- 添加系统变量

  ```
  系统变量Path中增加：D:\MongoDB\bin
  ```

- cmd中执行mongo命令

- 配置开机启动服务

  ```sh
  #移除开机服务
  mongod --remove --serviceName "MongoDB"
  #创建开机服务
  mongod --config D:\MongoDB\bin\mongod.cfg --serviceName MongoDB --install
  ```

  

## 数据库用户角色

### [角色](https://docs.mongodb.com/manual/reference/built-in-roles/#backup-and-restoration-roles)

| 角色                 | 描述                                                         |
| -------------------- | ------------------------------------------------------------ |
| read                 | 提供读取所有*非*系统集合和[`system.js`](https://docs.mongodb.com/manual/reference/system-collections/#.system.js)集合上的数据的功能。 |
| readWrite            | 提供[`read`](https://docs.mongodb.com/manual/reference/built-in-roles/#read)角色的所有特权以及修改所有*非*系统集合和[`system.js`](https://docs.mongodb.com/manual/reference/system-collections/#.system.js)集合上的数据的能力。 |
| dbAdmin              | 提供执行管理任务的能力，例如与模式相关的任务，索引编制和收集统计信息。该角色不授予用户和角色管理特权。 |
| dbOwner              | 数据库所有者可以对数据库执行任何管理操作。这个角色组合由授予的权限[`readWrite`](https://docs.mongodb.com/manual/reference/built-in-roles/#readWrite)， [`dbAdmin`](https://docs.mongodb.com/manual/reference/built-in-roles/#dbAdmin)和[`userAdmin`](https://docs.mongodb.com/manual/reference/built-in-roles/#userAdmin)角色。 |
| userAdmin            | 提供在当前数据库上创建和修改角色和用户的功能。由于该[`userAdmin`](https://docs.mongodb.com/manual/reference/built-in-roles/#userAdmin)角色允许用户向任何用户（包括他们自己）授予任何特权，因此该角色还间接提供了 对数据库或 集群（如果作用域为数据库）的[超级用户](https://docs.mongodb.com/manual/reference/built-in-roles/#superuser)访问权限`admin`。 |
| clusterAdmin         | 提供最大的群集管理访问。这个角色组合由授予的权限[`clusterManager`](https://docs.mongodb.com/manual/reference/built-in-roles/#clusterManager)， [`clusterMonitor`](https://docs.mongodb.com/manual/reference/built-in-roles/#clusterMonitor)和[`hostManager`](https://docs.mongodb.com/manual/reference/built-in-roles/#hostManager)角色。此外，角色提供了[`dropDatabase`](https://docs.mongodb.com/manual/reference/privilege-actions/#dropDatabase)操作。 |
| clusterManager       | 提供对集群的管理和监视操作。具有此角色的用户可以访问`config`和`local` 数据库，分别用于分片和复制。 |
| clusterMonitor       | 提供对监视工具（例如[MongoDB Cloud Manager](https://www.mongodb.com/cloud/cloud-manager/?jmp=docs) 和[Ops Manager](https://docs.opsmanager.mongodb.com/current/)监视代理）的只读访问 |
| hostManager          | 提供监视和管理服务器的功能。                                 |
| backup               | 提供备份数据所需的最小特权。该角色提供了足够的特权，可以使用[MongoDB Cloud Manager](https://www.mongodb.com/cloud/cloud-manager/?jmp=docs)备份代理， [Ops Manager](https://docs.opsmanager.mongodb.com/current/)备份代理，或用于备份 [`mongodump`](https://docs.mongodb.com/manual/reference/program/mongodump/#bin.mongodump)整个[`mongod`](https://docs.mongodb.com/manual/reference/program/mongod/#bin.mongod)实例。提供对数据库中的集合和数据库中 的集合的[`insert`](https://docs.mongodb.com/manual/reference/privilege-actions/#insert)和[`update`](https://docs.mongodb.com/manual/reference/privilege-actions/#update)操作 。 |
| restore              | *改变在3.6版本：*提供[`convertToCapped`](https://docs.mongodb.com/manual/reference/privilege-actions/#convertToCapped)对非系统集合。 |
| readAnyDatabase      | 提供相同的只读的权限[`read`](https://docs.mongodb.com/manual/reference/built-in-roles/#read)上，除了所有数据库`local`和`config`。该角色还提供[`listDatabases`](https://docs.mongodb.com/manual/reference/privilege-actions/#listDatabases)对整个集群的 操作。 |
| readWriteAnyDatabase | 提供与[`readWrite`](https://docs.mongodb.com/manual/reference/built-in-roles/#readWrite)所有数据库（`local`和除外）相同的特权`config`。该角色还提供[`listDatabases`](https://docs.mongodb.com/manual/reference/privilege-actions/#listDatabases)对整个集群的操作。 |
| userAdminAnyDatabase | 与和 [`userAdmin`](https://docs.mongodb.com/manual/reference/built-in-roles/#userAdmin)之外的所有数据库都提供对用户管理操作的相同访问权限 。 |
| dbAdminAnyDatabase   | 提供与[`dbAdmin`](https://docs.mongodb.com/manual/reference/built-in-roles/#dbAdmin)所有数据库（`local`和除外）相同的特权`config`。该角色还提供[`listDatabases`](https://docs.mongodb.com/manual/reference/privilege-actions/#listDatabases)对整个集群的操作。 |
| root                 | 多个角色提供间接或直接的系统范围的超级用户访问。             |
|                      |                                                              |
|                      |                                                              |
|                      |                                                              |
|                      |                                                              |

## 创建用户

```sh
# 创建admin数据库
use admin
# 创建root用户
db.createUser(
  {
    user: "root",
    pwd: "admin123",
    roles: [ { role: "root", db: "admin" } ]
  }
)

# 创建数据库
use zgcenv
# 创建用户并指定权限和数据库
db.createUser(
  {
    user: "zgcenv",
    pwd: "zgcenv",
    roles: [ { role: "dbOwner", db: "zgcenv" } ]
  }
)
```

## 开启验证

```
./mongod --auth
```

或修改配置文件mongod.cfg

```sh
security:
  authorization: enabled
```



## 测试连接

```sh
./mongo 127.0.0.1:27017/zgcenv -u zgcenv -p zgcenv
```



