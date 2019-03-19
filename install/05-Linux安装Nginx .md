# CentOS 7 安装Nginx

互联网的开放性成就了程序员的未来，I Love Internet！

> **一、安装依赖包**
```
顺序安装依赖包：遇到【y/n】都选y回车
yum -y install gcc*
yum -y install pcre*
yum -y install zlib*
yum -y install openssl*
yum -y install links*
yum -y install lsb
查看系统版本：lsb_release -a
```
> **二、下载安装包**

	进入opt目录，创建software文件夹 存放Nginx下载的安装程序
```
mkdir /opt/software
cd /opt/software/
wget http://nginx.org/download/nginx-1.10.2.tar.gz
tar -zxvf nginx-1.10.2.tar.gz
cd nginx-1.10.2/
mkdir -p /var/cache/nginx
wget https://www.openssl.org/source/openssl-1.0.2j.tar.gz
tar -zxvf openssl-1.0.2j.tar.gz
```
> **四、编译 安装**
```
cd /opt/software/nginx-1.10.2/

 ./configure --prefix=/usr/local/nginx --sbin-path=/usr/sbin/nginx --conf-path=/etc/nginx/nginx.conf --error-log-path=/var/log/nginx/error.log --http-log-path=/var/log/nginx/access.log --pid-path=/var/run/nginx.pid --lock-path=/var/run/nginx.lock --http-client-body-temp-path=/var/cache/nginx/client_temp --http-proxy-temp-path=/var/cache/nginx/proxy_temp --http-fastcgi-temp-path=/var/cache/nginx/fastcgi_temp --http-uwsgi-temp-path=/var/cache/nginx/uwsgi_temp --http-scgi-temp-path=/var/cache/nginx/scgi_temp --user=nobody --group=nobody --with-pcre --with-http_v2_module --with-http_ssl_module --with-http_realip_module --with-http_addition_module --with-http_sub_module --with-http_dav_module --with-http_flv_module --with-http_mp4_module --with-http_gunzip_module --with-http_gzip_static_module --with-http_random_index_module --with-http_secure_link_module --with-http_stub_status_module --with-http_auth_request_module --with-mail --with-mail_ssl_module --with-file-aio --with-ipv6 --with-http_v2_module --with-threads --with-stream --with-stream_ssl_module --with-openssl=/var/cache/nginx/openssl-1.0.2j

make & make install
```
> **五、启动验证安装成功**
```
/usr/sbin/nginx
links 127.0.0.1
```
显示welcome to nginx！安装成功。
> **六、开放80访问端口**
```
开放80端口
firewall-cmd --zone=public --add-port=8080/tcp --permanent
firewall-cmd --reload
firewall-cmd --zone=public --list-ports

service firewalld stop
```
> **七、编辑启动脚本**
```
vim /etc/init.d/nginx
复制一下代码到nginx文件中
--------------------开始-----------------
#!/bin/sh
# chkconfig:        2345 80 20
# Description:        Start and Stop Nginx
# Provides:        nginx
# Default-Start:    2 3 4 5
# Default-Stop:        0 1 6
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin
NAME=nginx
NGINX_BIN=/usr/local/nginx/sbin/$NAME
CONFIGFILE=/usr/local/nginx/conf/$NAME.conf
PIDFILE=/usr/local/nginx/logs/pid/$NAME.pid
SCRIPTNAME=/etc/init.d/$NAME
case "$1" in
start)
echo -n "Starting $NAME... "
if netstat -tnpl | grep -q nginx;then
echo "$NAME (pid `pidof $NAME`) already running."
exit 1
fi
$NGINX_BIN -c $CONFIGFILE
if [ "$?" != 0 ] ; then
echo " failed"
exit 1
else
echo " done"
fi
;;
stop)
echo -n "Stoping $NAME... "
if ! netstat -tnpl | grep -q nginx; then
echo "$NAME is not running."
exit 1
fi
$NGINX_BIN -s stop
if [ "$?" != 0 ] ; then
echo " failed. Use force-quit"
exit 1
else
echo " done"
fi
;;
status)
if netstat -tnpl | grep -q nginx; then
PID=`pidof nginx`
echo "$NAME (pid $PID) is running..."
else
echo "$NAME is stopped"
exit 0       
fi
;;
force-quit)
echo -n "Terminating $NAME... "
if ! netstat -tnpl | grep -q nginx; then
echo "$NAME is not running."
exit 1
fi
kill `pidof $NAME`
if [ "$?" != 0 ] ; then
echo " failed"
exit 1
else
echo " done"   
fi
;;
restart)
$SCRIPTNAME stop
sleep 1
$SCRIPTNAME start
;;
reload)                                                                                      
echo -n "Reload service $NAME... "
if netstat -tnpl | grep -q nginx; then
$NGINX_BIN -s reload
echo " done"
else
echo "$NAME is not running, can't reload."
exit 1
fi
;;
configtest)
echo -n "Test $NAME configure files... "
$NGINX_BIN -t
;;
*)
echo "Usage: $SCRIPTNAME {start|stop|force-quit|restart|reload|status|configtest}"
exit 1
;;
esac
-----------------结束---------------------
:wq! 保存
分配可执行权限
chmod a+x /etc/init.d/nginx
重启服务器：
reboot
启动
service nginx start
停止
service nginx stop
重启
service nginx reconfigure
查看状态
service nginx status
```
> **八、配置开机启动Nginx**
```
新增nginx.service文件
vim /lib/systemd/system/nginx.service
------------------内容--------------------
[Unit]
Description=nginx 
After=network.target 

[Service] 
Type=forking 
ExecStart=/etc/init.d/nginx start        
ExecReload=/etc/init.d/nginx restart        
ExecStop=/etc/init.d/nginx  stop        
PrivateTmp=true 
  
[Install] 
WantedBy=multi-user.target
------------------结束--------------------
保存：wq!
启用：
systemctl enable nginx.service
测试配置是否成功：
systemctl start nginx.service
查看是否启动：
netstat -lntp|grep nginx
```
> **九、配置nginx.conf**

   **重点内容根据一下内容修改nginx配置项，没有无需改动，相同无需改动.**
```
vim /etc/nginx/nginx.conf
-------------------开始---------------------
events
{
    use epoll;
        worker_connections 51200;
        multi_accept on;
    }
	http
    {
        server_names_hash_bucket_size 128;
        client_header_buffer_size 32k;
        large_client_header_buffers 4 32k;
        client_max_body_size 50m;
        sendfile on;
        tcp_nopush on;
        keepalive_timeout 60;
        tcp_nodelay on;

        fastcgi_connect_timeout 300;
        fastcgi_send_timeout 300;
        fastcgi_read_timeout 300;
        fastcgi_buffer_size 64k;
        fastcgi_buffers 4 64k;
        fastcgi_busy_buffers_size 128k;
        fastcgi_temp_file_write_size 256k;
       
        gzip on;
        gzip_min_length 1k;
        gzip_buffers 4 16k;
        gzip_http_version 1.0;
        gzip_comp_level 2;
        gzip_types text/plain application/x-javascript text/css application/xml;
        gzip_vary on;
        gzip_proxied expired no-cache no-store private auth;
        gzip_disable "MSIE [1-6]\.";

        server_tokens off;
        log_format access '$remote_addr - $remote_user [$time_local] "$request" '
        '$status $body_bytes_sent "$http_referer" '
        '"$http_user_agent" $http_x_forwarded_for';
	    server
        {
        }
    }
-------------------结束---------------------
```
> **十、部署Tomcat集群**
```
vim /usr/local/nginx/conf/nginx.conf
-------------------开始---------------------
未完待续
-------------------结束---------------------
```

**十一、配置 二级域名**

```
	upstream web_app { 
      server localhost:8180 weight=1 max_fails=2 fail_timeout=30s; 
    }
	
	server {
        listen       80;
        server_name  pay.arts.com;
        location / {
           proxy_pass http://172.24.129.22:8181;
           proxy_headers_hash_max_size 51200;
           proxy_headers_hash_bucket_size 6400;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header REMOTE-HOST $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
    }
	server {
        listen       80;
        server_name  gitlab.arts.com;
        location / {
           proxy_pass http://172.24.129.22:8787;
           proxy_headers_hash_max_size 51200;
           proxy_headers_hash_bucket_size 6400;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header REMOTE-HOST $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
    }
    server {
        listen       80;
        server_name  www.arts.com;
		location / {
			proxy_headers_hash_max_size 51200;
			proxy_headers_hash_bucket_size 6400;
			proxy_set_header Host $host;
			proxy_set_header X-Real-IP $remote_addr;
			proxy_set_header REMOTE-HOST $remote_addr;
			proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
			proxy_pass http://web_app; 
			expires  3d;
		}
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

```

