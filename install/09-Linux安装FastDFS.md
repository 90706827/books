# FastDFS配置

## 准备工作

```
yum -y install unzip zip wget gcc gcc-c++ perl make cmake
yum -y install pcre pcre-devel  
yum -y install zlib zlib-devel  
yum -y install openssl openssl-devel
```

分别下载master分支的zip文件 上传到制定目录

https://github.com/happyfish100/fastdfs

https://github.com/happyfish100/libfastcommon

https://github.com/happyfish100/fastdfs-nginx-module

```
mkdir /home/fast
```

将下载的zip文件上传之此目录

下载nginx

```
wget http://nginx.org/download/nginx-1.12.2.tar.gz
```

安装文件目录

```
-rw-r--r--. 1 root root 436855 10月 21 18:59 fastdfs-master.zip
-rw-r--r--. 1 root root  22236 10月 21 19:00 fastdfs-nginx-module-master.zip
-rw-r--r--. 1 root root 214652 10月 21 18:59 libfastcommon-master.zip
-rw-r--r--. 1 root root 981687 10月 20 10:46 nginx-1.12.2.tar.gz
```

## 安装Common

```shell
cd /home/fast

unzip libfastcommon-master.zip -d /usr/local/fast/

cd /usr/local/fast/libfastcommon-master/

./make.sh

./make.sh install

# FastDFS主程序设置的目录为/usr/local/lib/，而我们的安装目录为/usr/lib64,所以我们需要创建/usr/lib64/下的一些核心执行程序的软连接文件。
ln -s /usr/lib64/libfastcommon.so /usr/local/lib/libfastcommon.so
ln -s /usr/lib64/libfastcommon.so /usr/lib/libfastcommon.so
ln -s /usr/lib64/libfdfsclient.so /usr/local/lib/libfdfsclient.so
ln -s /usr/lib64/libfdfsclient.so /usr/lib/libfdfsclient.so
```



## 安装FastDFS

```shell
cd /home/fast

unzip fastdfs-master.zip -d /usr/local/fast/

cd /usr/local/fast/fastdfs-master/

./make.sh

./make.sh install

cd /etc/init.d/ && ls | grep fdfs
#服务脚本目录：
#fdfs_storaged
#fdfs_trackerd

ll /etc/fdfs/
#配置文件：
#-rw-r--r--. 1 root root 1461 10月 21 23:13 client.conf.sample
#-rw-r--r--. 1 root root 7978 10月 21 23:13 storage.conf.sample
#-rw-r--r--. 1 root root  105 10月 21 23:13 storage_ids.conf.sample
#-rw-r--r--. 1 root root 7441 10月 21 23:13 tracker.conf.sample

cd /usr/bin/ && ls | grep fdfs
#FdfsDFS执行脚本：
#fdfs_appender_test
#fdfs_appender_test1
#fdfs_append_file
#fdfs_crc32
#fdfs_delete_file
#fdfs_download_file
#fdfs_file_info
#fdfs_monitor
#fdfs_storaged
#fdfs_test
#fdfs_test1
#fdfs_trackerd
#fdfs_upload_appender
#fdfs_upload_file
```



## 配置跟踪服务

```shell
cp /etc/fdfs/tracker.conf.sample /etc/fdfs/tracker.conf

vim /etc/fdfs/tracker.conf

disabled=false #默认开启 
port=22122 #默认端口号 
base_path=/home/yuqing/fastdfs/tracker #我刚刚创建的目录 
http.server_port=8088 #默认端口是8080

mkdir -p /home/yuqing/fastdfs/tracker

#开放端口
firewall-cmd --zone=public --add-port=8088/tcp --permanent
#重启防火墙
firewall-cmd --reload
#查看开放端口
firewall-cmd --zone=public --list-ports
#配置开机启动
chmod +x /etc/rc.d/rc.local
vim /etc/rc.d/rc.local
#写入：
/etc/init.d/fdfs_trackerd start

启动tracker命令：
/etc/init.d/fdfs_trackerd start
停止tracker命令：
/etc/init.d/fdfs_trackerd stop

#查看生成的目录
ll /home/yuqing/fastdfs/storage/data/
```

## 配置存储服务

```shell
cp /etc/fdfs/storage.conf.sample /etc/fdfs/storage.conf

vim /etc/fdfs/storage.conf
disabled=false 
group_name=group1 #组名，根据实际情况修改 
port=23000 #设置storage的端口号，默认是23000，同一个组的storage端口号必须一致 
base_path=/home/yuqing/fastdfs/storage #设置storage数据文件和日志目录 
store_path_count=1 #存储路径个数，需要和store_path个数匹配 
base_path0=/home/yuqing/fastdfs/storage #实际文件存储路径 
http.server_port=8089 #设置 http 端口号
tracker_server=192.168.0.121:22122 #真实环境中，此处填写外网ip，防止客户端读取ip段错误

mkdir -p /home/yuqing/fastdfs/storage

#开放端口
firewall-cmd --zone=public --add-port=8089/tcp --permanent
firewall-cmd --zone=public --add-port=22122/tcp --permanent
firewall-cmd --zone=public --add-port=23000/tcp --permanent
#重启防火墙
firewall-cmd --reload
#查看开放端口
firewall-cmd --zone=public --list-ports

#启动命令
/etc/init.d/fdfs_storaged start
#停止命令
/etc/init.d/fdfs_storaged stop
#配置开机启动
vim /etc/rc.d/rc.local
#写入：
/etc/init.d/fdfs_storaged start

```

## 测试上传

```shell
cp /etc/fdfs/client.conf.sample /etc/fdfs/client.conf
vim /etc/fdfs/client.conf
#修改
base_path=/home/yuqing/fastdfs/tracker #跟踪器服务路径
tracker_server=192.168.0.121:22122 
##上传/home/fast/目录下fdfs.jpg图片文件 提前准备好图片文件
/usr/bin/fdfs_upload_file /etc/fdfs/client.conf /home/fast/fdfs.jpg 
#上传成功后返回访问路径
group1/M00/00/00/wKgAeVvMoSGAX5apAAL3tTRs1g0493.jpg
#查看上传文件
ll /home/yuqing/fastdfs/storage/data/00/00/
wKgAeVvMoSGAX5apAAL3tTRs1g0493.jpg
```

## 整合nginx

```shell
cd /home/fast/
unzip fastdfs-nginx-module-master.zip -d /usr/local/fast/
tar -zxvf nginx-1.12.2.tar.gz -C /usr/local/fast
vim /usr/local/fast/fastdfs-nginx-module-master/src/config
#修改
ngx_module_incs="/usr/include/fastdfs /usr/include/fastcommon/"
CORE_INCS="$CORE_INCS /usr/include/fastdfs /usr/include/fastcommon/"

cd /usr/local/fast/nginx-1.12.2/

./configure \
--prefix=/usr/local/nginx \
--add-module=/usr/local/fast/fastdfs-nginx-module-master/src
make
make install
#查看安装成功后有如下目录
ls /usr/local/nginx/
conf  html  logs  sbin
#拷贝配置文件
cp /usr/local/fast/fastdfs-nginx-module-master/src/mod_fastdfs.conf /etc/fdfs/

vim /etc/fdfs/mod_fastdfs.conf
#修改内容：比如连接超时时间、跟踪器路径配置、url的group配置、
connect_timeout=10
tracker_server=192.168.1.172:22122
url_have_group_name = true
store_path0=/fastdfs/storage

#拷贝文件
cd /usr/local/fast/fastdfs-master/conf/
cp http.conf mime.types /etc/fdfs/
#创建一个软连接，在/fastdfs/storage文件存储目录下创建软连接，将其链接到实际存放数据的目录
ln -s /home/yuqing/fastdfs/storage/data/ /home/yuqing/fastdfs/storage/data/M00

vim /usr/local/nginx/conf/nginx.conf

listen 8089;#nginx里的端口要和配置FastDFS存储中的storage.conf文件配置一致
server_name localhost;
location ~/group([0-9])/M00 {
	root /fastdfs/storage/data;
	ngx_fastdfs_module;
}
#启动命令：
/usr/local/nginx/sbin/nginx
#配置开机启动
vim /etc/rc.d/rc.local
#写入：
/usr/local/nginx/sbin/nginx
#访问图片成功则配置成功
http://192.168.0.121:8089/group1/M00/00/00/wKgAeVvMoSGAX5apAAL3tTRs1g0493.jpg
```

## 启停命令

```shell
启动命令：
启动tracker命令：/etc/init.d/fdfs_trackerd start
查看进程命令：ps -el | grep fdfs
启动storage命令：/etc/init.d/fdfs_storaged start
查看进程命令：ps -el | grep fdfs
启动nginx命令：/usr/local/nginx/sbin/nginx
停止命令：
停止tracker命令：/etc/init.d/fdfs_trackerd stop
关闭storage命令：/etc/init.d/fdfs_storaged stop
关闭nginx命令：/usr/local/nginx/sbin/nginx -s stop
删除上传文件:
/usr/bin/fdfs_delete_file /etc/fdfs/client.conf group1/M00/00/00/wKgAeVvMoSGAX5apAAL3tTRs1g0493.jpg
```





```shell
yum -y install unzip zip
yum -y install gcc-c++
yum -y install perl

https://github.com/happyfish100/fastdfs/releases

https://github.com/happyfish100/libfastcommon/releases

https://github.com/happyfish100/fastdfs-nginx-module/releases

tar -zxvf libfastcommon-1.0.39.tar.gz
cd libfastcommon-1.0.39
./make.sh
./make.sh install

tar -zxvf fastdfs-5.11.tar.gz
cd -zxvf fastdfs-5.11
./make.sh
./make.sh install

cd /etc/fdfs/

cp client.conf.sample client.conf
cp storage.conf.sample storage.conf
cp tracker.conf.sample tracker.conf

#创建目录 /usr/fastdfs/fastdfs_tracker
mkdir fastdfs
cd fastdfs
mkdir fastdfs_tracker

vi /etc/fdfs/tracker.conf

disabled=false #默认开启 
port=22122 #默认端口号 
base_path=/usr/fastdfs/fastdfs_tracker #我刚刚创建的目录 
http.server_port=6767 #默认端口是8080

service fdfs_trackerd start
cd /usr/fastdfs/fastdfs_tracker
#启动后此目录下有data logs


chmod +x /etc/rc.d/rc.local
vim /etc/rc.d/rc.local
#写入：
service fdfs_trackerd start

cd /usr/fastdfs/
mkdir fastdfs_storage  fastdfs_storage_data

vim /etc/fdfs/storage.conf
disabled=false 
group_name=group1 #组名，根据实际情况修改 
port=23000 #设置storage的端口号，默认是23000，同一个组的storage端口号必须一致 
base_path=/usr/fastdfs/fastdfs_storage #设置storage数据文件和日志目录 
store_path_count=1 #存储路径个数，需要和store_path个数匹配 
base_path0=/usr/fastdfs/fastdfs_storage_data #实际文件存储路径 
tracker_server=192.168.0.122:22122 #我CentOS7的ip地址 
http.server_port=6768 #设置 http 端口号

#修改保存后创建软引用：
ln -s /usr/bin/fdfs_storaged /usr/local/bin

#启动storage
service fdfs_storaged start

#设置开机启动： 
vim /etc/rc.d/rc.local
#写入：
service fdfs_storaged start

#查看服务是否启动
 ps -ef|grep fdfs
 
 #配置客户端
 vim /etc/fdfs/client.conf

#tracker服务器文件路径
base_path=/usr/fastdfs/fastdfs_tracker
#tracker服务器IP地址和端口号
tracker_server=192.168.0.122:22122
# tracker 服务器的 http端口号，必须和tracker的设置对应起来
http.tracker_server_port=6767


#将fdfs.jpg图片上传文件到/mnt路径下
#模拟上传：
/usr/bin/fdfs_upload_file  /etc/fdfs/client.conf /mnt/fdfs.jpg 
#返回路径：
group1/M00/00/00/wKgAelvK4vmAIws3AAL3tYX7A8I300.jpg

#组名：group1 
#磁盘：M00 
#目录：00/00 
#文件名称：wKiAg1lE9WqAWu_ZAAFaL_xdW_s943.jpg
#图片会上传到/usr/fastdfs/fastdfs_storage_data/data/00/00目录下


##配置 nginx

yum -y install pcre pcre-devel  
yum -y install zlib zlib-devel  
yum -y install openssl openssl-devel

vim mnt/fastdfs-nginx-module-1.20/src/config
#修改：
ngx_module_incs="/usr/include/fastdfs /usr/include/fastcommon/"
CORE_INCS="$CORE_INCS /usr/include/fastdfs /usr/include/fastcommon/"

useradd -M -s /sbin/nologin nginx

tar -zxvf nginx-1.12.2.tar.gz 
cd nginx-1.12.2


./configure \
--prefix=/usr/local/nginx \
--add-module=/usr/local/fast/fastdfs-nginx-module-master/src \
--with-pcre \
--with-http_v2_module \
--with-http_ssl_module \
--with-http_realip_module \
--with-http_addition_module \
--with-http_sub_module \
--with-http_dav_module \
--with-http_flv_module \
--with-http_mp4_module \
--with-http_gunzip_module \
--with-http_gzip_static_module \
--with-http_random_index_module \
--with-http_secure_link_module \
--with-http_stub_status_module \
--with-http_auth_request_module \
--with-mail \
--with-mail_ssl_module \
--with-file-aio \
--with-http_v2_module \
--with-threads \
--with-stream \
--with-stream_ssl_module \
--user=nginx1  \
--group=nginx1 


make
make install


ln -s /usr/local/nginx/sbin/nginx /usr/local/sbin/

ll /usr/local/nginx/
#显示目录
drwx------. 2 nobody root    6 10月 21 11:04 client_body_temp
drwxr-xr-x. 2 root   root 4096 10月 21 11:04 conf
drwx------. 2 nobody root    6 10月 21 11:04 fastcgi_temp
drwxr-xr-x. 2 root   root   38 10月 21 10:33 html
drwxr-xr-x. 2 root   root   39 10月 21 11:21 logs
drwx------. 2 nobody root    6 10月 21 11:04 proxy_temp
drwxr-xr-x. 2 root   root   18 10月 21 10:58 sbin
drwx------. 2 nobody root    6 10月 21 11:04 scgi_temp
drwx------. 2 nobody root    6 10月 21 11:04 uwsgi_temp

###配置storage nginx
#修改nginx.conf配置 
vim /usr/local/nginx/conf/nginx.conf
#server中增加
server {
	#修改1
    listen       9999; 
    #增加2
    location ~/group1/M00 {
        root /usr/fastdfs/fastdfs_storage_data/data;
        ngx_fastdfs_module;
     }
#拷贝文件
cd /mnt/fastdfs-5.11/conf/
cp http.conf /etc/fdfs/
cp mime.types /etc/fdfscd
mnt/fastdfs-nginx-module-master/src/
cp mod_fastdfs.conf /etc/fdfs/
vim /etc/fdfs/mod_fastdfs.conf

base_path=/usr/fastdfs/fastdfs_storage  #保存日志目录
tracker_server=192.168.0.122:22122 #tracker服务器的IP地址以及端口号
storage_server_port=23000 #storage服务器的端口号
url_have_group_name = true #文件 url 中是否有 group 名
store_path0=/usr/fastdfs/fastdfs_storage_data   #存储路径
group_count = 3 #设置组的个数，事实上这次只使用了group1
#文件最后写入
[group1]
group_name=group1
storage_server_port=23000
store_path_count=1
store_path0=/home/fastdfs
store_path1=/usr/fastdfs/fastdfs_storage_data

[group2]
group_name=group2
storage_server_port=23000
store_path_count=1
store_path0=/usr/fastdfs/fastdfs_storage_data

[group3]
group_name=group3
storage_server_port=23000
store_path_count=1
store_path0=/usr/fastdfs/fastdfs_storage_data


#创建M00至storage存储目录的符号连接
ln -s /usr/fastdfs/fastdfs_storage_data/data/ /usr/fastdfs/fastdfs_storage_data/data/M00
#启动nginx:
/usr/local/nginx/sbin/nginx


源码安装nginx后启动，报错 
[root@localhost sbin]# /usr/local/nginx/sbin/nginx 
nginx: [emerg] getgrnam(“nginx”) failed
解决方法： 
vim /usr/local/nginx/conf/nginx.conf 
去掉user nobody之前的#号 

##配置tracker nginx
tar -zxvf nginx-1.12.2.tar.gz 
cd nginx-1.12.2


./configure \
--prefix=/usr/local/nginx2 \
--add-module=/mnt/fastdfs-nginx-module-master/src \
--with-pcre \
--with-http_v2_module \
--with-http_ssl_module \
--with-http_realip_module \
--with-http_addition_module \
--with-http_sub_module \
--with-http_dav_module \
--with-http_flv_module \
--with-http_mp4_module \
--with-http_gunzip_module \
--with-http_gzip_static_module \
--with-http_random_index_module \
--with-http_secure_link_module \
--with-http_stub_status_module \
--with-http_auth_request_module \
--with-mail \
--with-mail_ssl_module \
--with-file-aio \
--with-http_v2_module \
--with-threads \
--with-stream \
--with-stream_ssl_module \
--user=nobody  \
--group=nobody 

make
make install

vim /usr/local/nginx2/conf/nginx.conf
#增加1
upstream fdfs_group1 {
        server 127.0.0.1:9999;
    }
server {
	#增加2
	location /group1/M00 {
        proxy_pass http://fdfs_group1;
    }
}
#启动nginx2
/usr/local/nginx2/sbin/nginx


#查看开放端口
firewall-cmd --zone=public --list-ports
#开放端口
firewall-cmd --zone=public --add-port=9999/tcp --permanent
firewall-cmd --zone=public --add-port=20880/tcp --permanent
firewall-cmd --zone=public --add-port=23000/tcp --permanent
#重启防火墙
firewall-cmd --reload

在我的CentOS上这些端口都是开放的。 
storage:20880 
tracker:23000 
这两个端口要开启，到时候下一篇讲fastdfs-client-javas可能会造成无法连接。 
9999和80端口是提供给nginx访问的。 
开放端口号命令：–permanent表示永久生效，不加的话，重启后不生效

#开启防火墙
--systemctl enable firewalld.service
#关闭防火墙(开机会仍会启动)
--systemctl stop firewalld.service
#禁用防火墙(开机后不再启动)
--systemctl disable firewalld.service

```

## 帮助文档

1. [tracker.conf配置文件详解](http://bbs.chinaunix.net/thread-1941456-1-1.html)

