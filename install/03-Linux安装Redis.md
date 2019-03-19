#CentOS 7 安装Redis

互联网的开放性成就了程序员的未来，I Love Internet！

##安装运行环境

```
yum install gcc*
```
##下载Redis并解压

```
wget http://download.redis.io/redis-stable.tar.gz
tar -zxvf redis-stable.tar.gz
```
##编译 安装

```
cd redis-stable
make 
make install
```
##修改配置文件

```
mkdir /var/redis
cd /var/redis/
mkdir data log run
mkdir /etc/redis
cd redis-stable
cp redis.conf /etc/redis/redis.conf
vim /etc/redis/redis.conf
```
```
daemonize yes 	--目的使进程在后台运行
bind 192.168.1.215 --ip改为实际ip地址
port 6379 		--监听的端口号
pidfile /var/redis/run/redis.pid 	--pid路径
dir /var/redis/data 					--dump目录
logfile /var/redis/log/redis.log	--日志文件
requirepass myredis 	--设置访问redis密码：myredis
Ctrl+c 保存退出：wq
```
```
daemonize：	是否以后台daemon方式运行
pidfile：	pid文件位置
port：		监听的端口号
timeout：	请求超时时间
loglevel：	log信息级别
logfile：	log文件位置
databases：	开启数据库的数量
save * *：	保存快照的频率，第一个*表示多长时间，第三个*表示执行多少次写操作。在一定时间内执行一定数量的写操作时，自动保存快照。可设置多个条件。
rdbcompression：	是否使用压缩
dbfilename：	数据快照文件名（只是文件名，不包括目录）
dir：		数据快照的保存目录（这个是目录）
appendonly：是否开启appendonlylog，开启的话每次写操作会记一条log，这会提高数据抗风险能力，但影响效率。
appendfsync：appendonlylog	如何同步到磁盘（三个选项，分别是每次写都强制调用fsync、每秒启用一次fsync、不调用fsync等待系统自己同步）
```
##启动Redis

```
redis-server /etc/redis/redis.conf
ps -ef | grep redis --查看启动
kill -9 进程号  --关闭服务
```
##创建启动脚本

```
--拷贝安装目录redis-stable文件redis_init_script到/etc/init.d目录下
cp utils/redis_init_script /etc/init.d/
cd /etc/init.d
mv redis_init_script redis --重名名为redis
vim redis	--编辑redis文件

# chkconfig: 2345 90 10
# description: Redis is a persistent key-value database

REDISPORT=6379
EXEC=/usr/local/bin/redis-server
CLIEXEC=/usr/local/bin/redis-cli

PIDFILE=/var/redis/run/redis_${REDISPORT}.pid
CONF="/etc/redis/redis_${REDISPORT}.conf"

$CLIEXEC -a myredis -p $REDISPORT shutdown
Ctrl+c 保存退出：wq
chmod +x /etc/init.d/redis
chkconfig redis on
```
##启动 关闭命令

```
service redis start
service redis stop

reboot --重启服务器命令 查看重启服务器后redis是启动成功！
```
##安装过程中如有疑问评论留言！

个人邮箱： 90706827@163.com