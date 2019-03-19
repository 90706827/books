# Kafka安装手册

## 各节点下载

```shell
yum install net-tools
yum install telnet
wget http://mirrors.shu.edu.cn/apache/kafka/2.0.0/kafka_2.11-2.0.0.tgz
```

## 服务器

> 三台服务器IP地址
>
> 192.168.0.121
>
> 192.168.0.122
>
> 192.168.0.123

## 配置目录

### 节点一配置

##### 创建目录

```shell
#解压缩
tar -zxvf kafka_2.11-2.0.0.tgz
#修改目录名称
mv kafka_2.11-2.0.0 kafka
#创建目录
cd kafka
mkdir zookeeper_1
#编辑myid文件 写入节点数：1
vi zookeeper_1/myid 
mkdir kafka_logs_1
```

##### 配置zookeeper.properties

```shell
#准备配置文件
cd kafka/config
vi zookeeper_1.properties
```

```shell
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/opt/kafka/zookeeper_1
clientPort=2181
maxClientCnxns=0
server.1=192.168.0.121:2888:3888
server.2=192.168.0.122:2888:3888
server.3=192.168.0.123:2888:3888
```

##### 配置server.properties

```shell
#准备配置文件
cd kafka/config
vi zookeeper_2.properties
```

```shell
borker.id=1
delete.topic.enable=true
listeners=PLAINTEXT://192.168.0.121:50005
advertised.listeners=PLAINTEXT://192.168.0.121:50005
num.network.threads=3
num.io.threads=8
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
log.dirs=/opt/kafka/kafka_logs_1
num.partitions=6
default.replication.factor=4
num.recovery.threads.per.data.dir=1
log.retention.hours=24
log.roll.hours=1
log.segment.bytes=1073741284
log.retention.check.interval.ms=3600000
zookeeper.connect=192.168.0.121:2181,192.168.0.122:2182,192.168.0.123:2183
zookeeper.connection.timeout.ms=60000
```

### 节点二配置

##### 创建目录

```shell
#解压缩
tar -zxvf kafka_2.11-2.0.0.tgz
#修改目录名称
mv kafka_2.11-2.0.0 kafka
#创建目录
cd kafka
mkdir zookeeper_2
#编辑myid文件 写入节点数：2
vi zookeeper_2/myid 
mkdir kafka_logs_2
```

##### 配置zookeeper.properties

```shell
#准备配置文件
cd kafka/config
vi zookeeper_2.properties
```

```shell
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/opt/kafka/zookeeper_2
clientPort=2182
maxClientCnxns=0
server.1=192.168.0.121:2888:3888
server.2=192.168.0.122:2888:3888
server.3=192.168.0.123:2888:3888
```

##### 配置server.properties

```shell
#准备配置文件
cd kafka/config
vi zookeeper_2.properties
```

```shell
borker.id=2
delete.topic.enable=true
listeners=PLAINTEXT://192.168.0.122:50005
advertised.listeners=PLAINTEXT://192.168.0.122:50005
num.network.threads=3
num.io.threads=8
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
log.dirs=/opt/kafka/kafka_logs_2
num.partitions=6
default.replication.factor=4
num.recovery.threads.per.data.dir=1
log.retention.hours=24
log.roll.hours=1
log.segment.bytes=1073741284
log.retention.check.interval.ms=3600000
zookeeper.connect=192.168.0.121:2181,192.168.0.122:2182,192.168.0.123:2183
zookeeper.connection.timeout.ms=60000
```

### 节点三配置

##### 创建目录

```shell
#解压缩
tar -zxvf kafka_2.11-2.0.0.tgz
#修改目录名称
mv kafka_2.11-2.0.0 kafka
#创建目录
cd kafka
mkdir zookeeper_3
#编辑myid文件 写入节点数：3
vi zookeeper_3/myid 
mkdir kafka_logs_3
```

##### 配置zookeeper.properties

```shell
#准备配置文件
cd kafka/config
vi zookeeper_3.properties
```

```shell
tickTime=2000
initLimit=10
syncLimit=5
dataDir=/opt/kafka/zookeeper_3
clientPort=2183
maxClientCnxns=0
server.1=192.168.0.121:2888:3888
server.2=192.168.0.122:2888:3888
server.3=192.168.0.123:2888:3888
```

##### 配置server.properties

```shell
#准备配置文件
cd kafka/config
vi zookeeper_3.properties
```

```shell
borker.id=3
delete.topic.enable=true
listeners=PLAINTEXT://192.168.0.123:50005
advertised.listeners=PLAINTEXT://192.168.0.123:50005
num.network.threads=3
num.io.threads=8
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
log.dirs=/opt/kafka/kafka_logs_3
num.partitions=6
default.replication.factor=4
num.recovery.threads.per.data.dir=1
log.retention.hours=24
log.roll.hours=1
log.segment.bytes=1073741284
log.retention.check.interval.ms=3600000
zookeeper.connect=192.168.0.121:2181,192.168.0.122:2182,192.168.0.123:2183
zookeeper.connection.timeout.ms=60000
```



## 关闭防火墙

生产环境要根据实际情况开放端口

```shell
systemctl stop firewalld.service
systemctl disable firewalld.service
systemctl status firewalld.service
```

## 启动集群

### 节点一

```shell
cd kafka
sh bin/zookeeper-server-start.sh config/zookeeper_1.properties &
sh bin/kafka-server-start.sh config/server_1.properties &
#查看是否启动
ps -ef|grep java
#jps 查看启动情况
jps
```

### 节点二

```shell
cd kafka
sh bin/zookeeper-server-start.sh config/zookeeper_2.properties &
sh bin/kafka-server-start.sh config/server_2.properties &
#查看是否启动
ps -ef|grep java
#jps 查看启动情况
jps
```

### 节点三

```shell
cd kafka
sh bin/zookeeper-server-start.sh config/zookeeper_3.properties &
sh bin/kafka-server-start.sh config/server_3.properties &
#查看是否启动
ps -ef|grep java
#jps 查看启动情况
jps
```

## Kafka使用

```shell
#创建topic
sh bin/kafka-topics.sh --create --zookeeper 192.168.0.121:2181, 192.168.0.122:2181, 192.168.0.123:2181 --replication-factor 3 --partitions 3 --topic test

--replication-factor 2 // 复制两份
--partitions 1 // 创建1个分区
--topic // 主题为my-topic
--zookeeper // 此处为为zookeeper监听的地址

#查看topic列表
sh bin/kafka-topics.sh --list --zookeeper 192.168.0.121:2181
#查看某个topic信息
sh bin/kafka-topics.sh --describe --zookeeper 192.168.0.121:2181 --topic test

#模拟客户端发送消息
#节点1
sh bin/kafka-console-producer.sh --broker-list 192.168.0.121:50005 --topic test
#节点2
sh bin/kafka-console-producer.sh --broker-list 192.168.0.121:50005 --topic test
#节点3
sh bin/kafka-console-producer.sh --broker-list 192.168.0.121:50005 --topic test
#模拟服务端接收消息
sh bin/kafka-console-consumer.sh --bootstrap-server 192.168.0.121:50005,192.168.0.122:50005,192.168.0.123:50005 --topic test --from-beginning
```

## Zookeeper配置详解

| 参数           | 描述 |
| -------------- | ---- |
| tickTime       |      |
| initLimit      |      |
| syncLimit      |      |
| dataDir        |      |
| clientPort     |      |
| maxClientCnxns |      |
| server.1       |      |
| server.2       |      |
| server.3       |      |

## Kafka配置详解

| 参数                                     | 描述                                                         |
| ---------------------------------------- | ------------------------------------------------------------ |
| borker.id                                | 每一个broker在集群中的唯一表示，要求是正数。当该服务器的IP地址发生改变时，broker.id没有变化，则不会影响consumers的消息情况；同myid中的值对应。 |
| listeners                                |                                                              |
| advertised.listeners                     |                                                              |
| listener.security.protocol.map           |                                                              |
| num.network.threads                      | broker处理消息的最大线程数，一般情况下数量为cpu核数          |
| num.io.threads                           | broker处理磁盘IO的线程数，数值为cpu核数2倍                   |
| socket.send.buffer.bytes                 | socket的发送缓冲区，socket的调优参数SO_SNDBUFF               |
| socket.receive.buffer.bytes              | socket的接受缓冲区，socket的调优参数SO_RCVBUFF               |
| socket.request.max.bytes                 | socket请求的最大数值，防止serverOOM，message.max.bytes必然要小于socket.request.max.bytes，会被topic创建时的指定参数覆盖 |
| log.dirs                                 | kafka数据的存放地址，多个地址的话用逗号分割,多个目录分布在不同磁盘上可以提高读写性能  /data/kafka-logs-1，/data/kafka-logs-2 |
| num.partitions                           | 每个topic的分区个数，若是在topic创建时候没有指定的话会被topic创建时的指定参数覆盖 |
| num.recovery.threads.per.data.dir        |                                                              |
| offsets.topic.replication.factor         |                                                              |
| transaction.state.log.replication.factor |                                                              |
| transaction.state.log.min.isr            |                                                              |
| log.flush.interval.messages              | log文件”sync”到磁盘之前累积的消息条数,因为磁盘IO操作是一个慢操作,但又是一个”数据可靠性"的必要手段,所以此参数的设置,需要在"数据可靠性"与"性能"之间做必要的权衡.如果此值过大,将会导致每次"fsync"的时间较长(IO阻塞),如果此值过小,将会导致"fsync"的次数较多,这也意味着整体的client请求有一定的延迟.物理server故障,将会导致没有fsync的消息丢失.表示每当消息记录数达到设定值时flush一次数据到磁盘 |
| log.flush.interval.ms                    | 仅仅通过interval来控制消息的磁盘写入时机,是不足的.此参数用于控制"fsync"的时间间隔,如果消息量始终没有达到阀值,但是离上一次磁盘同步的时间间隔达到阀值,也将触发 |
| log.cleanup.policy                       | 日志清理策略选择有：delete和compact主要针对过期数据的处理，或是日志文件达到限制的额度，会被 topic创建时的指定参数覆盖 |
| log.retention.hours                      | 数据文件保留多长时间， 存储的最大时间超过这个时间会根据log.cleanup.policy设置数据清除策略：log.retention.bytes和log.retention.minutes或log.retention.hours任意一个达到要求，都会执行删除 有2删除数据文件方式：按照文件大小删除：log.retention.bytes 按照2中不同时间粒度删除：分别为分钟，小时 |
| log.segment.bytes                        | topic每个分区的最大文件大小，一个topic的大小限制 = 分区数*log.retention.bytes。-1没有大小限log.retention.bytes和log.retention.minutes任意一个达到要求，都会执行删除，会被topic创建时的指定参数覆盖 |
| log.retention.check.interval.ms          | 文件大小检查的周期时间，是否处罚 log.cleanup.policy中设置的策略 |
| zookeeper.connect                        | zookeeper集群的地址，可以是多个，多个之间用逗号分割 hostname1:port1,hostname2:port2,hostname3:port3 |
| zookeeper.connection.timeout.ms          | ZooKeeper的连接超时时间                                      |
| group.initial.rebalance.delay.ms         |                                                              |
| delete.topic.enable                      |                                                              |
|                                          |                                                              |







