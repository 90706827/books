## 安装elasticsearch

https://www.elastic.co/downloads/elasticsearch

```bash
#配置三台机器的hosts文件内容如下：
vim /etc/hosts
192.168.0.121 master-node
192.168.0.122 data-node1
192.168.0.123 data-node2
```

```bash
rpm -ivh elasticsearch-6.5.2.rpm
ll /etc/sysconfig/elasticsearch

ll /etc/elasticsearch
-rw-rw----. 1 root elasticsearch   207 12月  9 23:14 elasticsearch.keystore
-rw-rw----. 1 root elasticsearch  2869 11月 30 08:02 elasticsearch.yml
-rw-rw----. 1 root elasticsearch  3266 11月 30 08:02 jvm.options
-rw-rw----. 1 root elasticsearch 12423 11月 30 08:02 log4j2.properties
-rw-rw----. 1 root elasticsearch   473 11月 30 08:02 role_mapping.yml
-rw-rw----. 1 root elasticsearch   197 11月 30 08:02 roles.yml
-rw-rw----. 1 root elasticsearch     0 11月 30 08:02 users
-rw-rw----. 1 root elasticsearch     0 11月 30 08:02 users_roles
```

elasticsearch.yml 文件用于配置集群节点等相关信息的

elasticsearch 文件则是配置服务本身相关的配置，例如某个配置文件的路径以及java的一些路径配置什么的。

```bash
vim /etc/elasticsearch/elasticsearch.yml 
##主节点配置如下内容
cluster.name: master-node  # 集群中的名称
node.name: master  # 该节点名称
#node.data: true  #不存储数据 使kibana访问此节点
network.host: 192.168.0.121  # 监听全部ip，在实际环境中应设置为一个安全的ip
http.port: 9200  # es服务的端口号
discovery.zen.ping.unicast.hosts: ["192.168.0.121", "192.168.77.122", "192.168.77.123"] # 配置自动发现
discovery.zen.minimum_master_nodes: 3 #主节点分裂 程序自主选择主节点

##支节点配置如下内容，
cluster.name: master-node  # 集群中的名称
node.name: data-node1
node.data: true
network.host: 192.168.0.122  # 监听全部ip，在实际环境中应设置为一个安全的ip
http.port: 9200  # es服务的端口号
discovery.zen.ping.unicast.hosts: ["192.168.0.121", "192.168.0.122", "192.168.0.123"]  # 配置自动发现
discovery.zen.minimum_master_nodes: 3 #主节点分裂 程序自主选择主节点

##支节点配置如下内容，
cluster.name: master-node  # 集群中的名称
node.name: data-node2
node.data: true
network.host: 192.168.0.123  # 监听全部ip，在实际环境中应设置为一个安全的ip
http.port: 9200  # es服务的端口号
discovery.zen.ping.unicast.hosts: ["192.168.0.121", "192.168.0.122", "192.168.0.123"]  # 配置自动发现
discovery.zen.minimum_master_nodes: 3 #主节点分裂 程序自主选择主节点
```

启动

```bash
systemctl start elasticsearch.service
ps aux |grep elasticsearch

/etc/elasticsearch/elasticsearch.yml     # els的配置文件
/etc/elasticsearch/jvm.options           # JVM相关的配置，内存大小等等
/etc/elasticsearch/log4j2.properties     # 日志系统定义
/var/lib/elasticsearch                   # 数据的默认存放位置

```

开放端口

firewall-cmd --zone=public --add-port=9200/tcp --permanent

firewall-cmd --zone=public --add-port=9300/tcp --permanent

firewall-cmd --reload

firewall-cmd --zone=public --list-ports



访问集群的状态信息

```bash
http://192.168.0.121:9200/_cluster/health?pretty
http://192.168.0.121:9200/_cluster/state?pretty
http://192.168.0.121:9200/_cat/indices?v
```





## 安装kibana

https://www.elastic.co/downloads/kibana

只有主节点需要安装kibana 

```bash
rpm -ivh kibana-6.5.2-x86_64.rpm
vim /etc/kibana/kibana.yml 
server.port: 5601  # 配置kibana的端口
server.host: 192.168.0.122  # Kibana安装的本机ip
elasticsearch.url: "http://192.168.0.121:9200"  # 配置es集群 非存贮数据的节点的ip
logging.dest: /var/log/kibana.log  # 配置kibana的日志文件路径，不然默认是messages里记录日志

touch /var/log/kibana.log; chmod 777 /var/log/kibana.log


systemctl start kibana
ps aux |grep kibana
netstat -lntp |grep 5601
```

开放端口

firewall-cmd --zone=public --add-port=5601/tcp --permanent
firewall-cmd --reload
firewall-cmd --zone=public --list-ports



http://192.168.0.121:5601/



## 安装logstash

目前logstash不支持JDK1.9

https://www.elastic.co/downloads/logstash

```bash
#第二台服务器
rpm -ivh rpm -ivh logstash-6.5.2.rpm

vim /etc/logstash/conf.d/syslog.conf 

input {  # 定义日志源
  syslog {
    type => "system-syslog"  # 定义类型
    port => 10514    # 定义监听端口
  }
}
output {  # 定义日志输出
  stdout {
    codec => rubydebug  # 将日志输出到当前的终端上显示
  }
}


cd /usr/share/logstash/bin
 ./logstash --path.settings /etc/logstash/ -f /etc/logstash/conf.d/syslog.conf --config.test_and_exit
 Sending Logstash's logs to /var/log/logstash which is now configured via log4j2.properties
Configuration OK  # 为ok则代表配置文件没有问题 虚拟机可以调整处理器数量 1个会报错
##配置kibana服务器的ip以及配置的监听端口：
vim /etc/rsyslog.conf
*.* @@192.168.0.123:10514

systemctl restart rsyslog

cd /usr/share/logstash/bin
./logstash --path.settings /etc/logstash/ -f /etc/logstash/conf.d/syslog.conf
Sending Logstash's logs to /var/log/logstash which is now configured via log4j2.properties
# 这时终端会停留在这里，因为我们在配置文件中定义的是将信息输出到当前终端
netstat -lntp |grep 10514

cd /usr/share/logstash/bin
./logstash --path.settings /etc/logstash/ -f /etc/logstash/conf.d/syslog.conf

配置logstash
以上只是测试的配置，这一步我们需要重新改一下配置文件，让收集的日志信息输出到es服务器中，而不是当前终端：

vim /etc/logstash/conf.d/syslog.conf 

input {
  syslog {
    type => "system-syslog"
    port => 10514
  }
}
output {
  elasticsearch {
    hosts => ["192.168.0.121:9200"]  # 定义es服务器的ip
    index => "system-syslog-%{+YYYY.MM}" # 定义索引
  }
}

cd /usr/share/logstash/bin
./logstash --path.settings /etc/logstash/ -f /etc/logstash/conf.d/syslog.conf --config.test_and_exit
Sending Logstash's logs to /var/log/logstash which is now configured via log4j2.properties
Configuration OK
显示没有问题

systemctl start logstash
ps aux |grep logstash

查看有没有信息
http://192.168.0.121:9200/_cat/indices?v

http://192.168.0.121:9200/system-syslog-2018.12?pretty

firewall-cmd --zone=public --add-port=10514/tcp --permanent
firewall-cmd --reload
firewall-cmd --zone=public --list-ports
```

## 安装filebeat

https://www.elastic.co/downloads/beats/filebeat



```bash
rpm -ivh filebeat-6.5.2-x86_64.rpm

vim /etc/filebeat/filebeat.yml 

filebeat.prospectors:
- type: log
   enabled: true 这一句要注释掉
   paths:
      - /var/log/*/*.log  # 指定需要收集的日志文件的路径

output.elasticsearch:
  hosts: ["192.168.77.128:9200"]  # 并配置es服务器的ip地址
  
  systemctl start filebeat
```

```shell
###################### Filebeat 配置示例 #########################

# This file is an example configuration file highlighting only the most common
# options. The filebeat.reference.yml file from the same directory contains all the
# supported options with more comments. You can use it as a reference.
#
# You can find the full configuration reference here:
# https://www.elastic.co/guide/en/beats/filebeat/index.html

# For more available modules and options, please see the filebeat.reference.yml sample
# configuration file.

#=========================== Filebeat inputs =============================

filebeat.inputs:

# Each - is an input. Most options can be set at the input level, so
# you can use different inputs for various configurations.
# Below are the input specific configurations.

- type: log

  # Change to true to enable this input configuration.
  #enabled: true

  # Paths that should be crawled and fetched. Glob based paths.
  paths:
    - /var/log/messages/*
    #- c:\programdata\elasticsearch\logs\*

  # Exclude lines. A list of regular expressions to match. It drops the lines that are
  # matching any regular expression from the list.
  #exclude_lines: ['^DBG']

  # Include lines. A list of regular expressions to match. It exports the lines that are
  # matching any regular expression from the list.
  #include_lines: ['^ERR', '^WARN']

  # Exclude files. A list of regular expressions to match. Filebeat drops the files that
  # are matching any regular expression from the list. By default, no files are dropped.
  #exclude_files: ['.gz$']

  # Optional additional fields. These fields can be freely picked
  # to add additional information to the crawled log files for filtering
  #fields:
  #  level: debug
  #  review: 1

  ### Multiline options

  # Multiline can be used for log messages spanning multiple lines. This is common
  # for Java Stack Traces or C-Line Continuation

  # The regexp Pattern that has to be matched. The example pattern matches all lines starting with [
  #multiline.pattern: ^\[

  # Defines if the pattern set under pattern should be negated or not. Default is false.
  #multiline.negate: false

  # Match can be set to "after" or "before". It is used to define if lines should be append to a pattern
  # that was (not) matched before or after or as long as a pattern is not matched based on negate.
  # Note: After is the equivalent to previous and before is the equivalent to to next in Logstash
  #multiline.match: after


#============================= Filebeat modules ===============================

filebeat.config.modules:
  # Glob pattern for configuration loading
  path: ${path.config}/modules.d/*.yml

  # Set to true to enable config reloading
  reload.enabled: false

  # Period on which files under path should be checked for changes
  #reload.period: 10s

#==================== Elasticsearch template setting ==========================

setup.template.settings:
  index.number_of_shards: 3
  #index.codec: best_compression
  #_source.enabled: false

#================================ General =====================================

# The name of the shipper that publishes the network data. It can be used to group
# all the transactions sent by a single shipper in the web interface.
#name:

# The tags of the shipper are included in their own field with each
# transaction published.
#tags: ["service-X", "web-tier"]

# Optional fields that you can specify to add additional information to the
# output.
#fields:
#  env: staging


#============================== Dashboards =====================================
# These settings control loading the sample dashboards to the Kibana index. Loading
# the dashboards is disabled by default and can be enabled either by setting the
# options here, or by using the `-setup` CLI flag or the `setup` command.
#setup.dashboards.enabled: false

# The URL from where to download the dashboards archive. By default this URL
# has a value which is computed based on the Beat name and version. For released
# versions, this URL points to the dashboard archive on the artifacts.elastic.co
# website.
#setup.dashboards.url:

#============================== Kibana =====================================

# Starting with Beats version 6.0.0, the dashboards are loaded via the Kibana API.
# This requires a Kibana endpoint configuration.
setup.kibana:

  # Kibana Host
  # Scheme and port can be left out and will be set to the default (http and 5601)
  # In case you specify and additional path, the scheme is required: http://localhost:5601/path
  # IPv6 addresses should always be defined as: https://[2001:db8::1]:5601
  #host: "localhost:5601"

  # Kibana Space ID
  # ID of the Kibana Space into which the dashboards should be loaded. By default,
  # the Default Space will be used.
  #space.id:

#============================= Elastic Cloud ==================================

# These settings simplify using filebeat with the Elastic Cloud (https://cloud.elastic.co/).

# The cloud.id setting overwrites the `output.elasticsearch.hosts` and
# `setup.kibana.host` options.
# You can find the `cloud.id` in the Elastic Cloud web UI.
#cloud.id:

# The cloud.auth setting overwrites the `output.elasticsearch.username` and
# `output.elasticsearch.password` settings. The format is `<user>:<pass>`.
#cloud.auth:

#================================ Outputs =====================================

# Configure what output to use when sending the data collected by the beat.

#-------------------------- Elasticsearch output ------------------------------
output.elasticsearch:
  # Array of hosts to connect to.
  hosts: ["192.168.0.122:9200"]

  # Optional protocol and basic auth credentials.
  #protocol: "https"
  #username: "elastic"
  #password: "changeme"

#----------------------------- Logstash output --------------------------------
#output.logstash:
  # The Logstash hosts
  #hosts: ["localhost:5044"]

  # Optional SSL. By default is off.
  # List of root certificates for HTTPS server verifications
  #ssl.certificate_authorities: ["/etc/pki/root/ca.pem"]

  # Certificate for SSL client authentication
  #ssl.certificate: "/etc/pki/client/cert.pem"

  # Client Certificate Key
  #ssl.key: "/etc/pki/client/cert.key"

#================================ Procesors =====================================

# Configure processors to enhance or manipulate events generated by the beat.

processors:
  - add_host_metadata: ~
  - add_cloud_metadata: ~

#================================ Logging =====================================

# Sets log level. The default log level is info.
# Available log levels are: error, warning, info, debug
#logging.level: debug

# At debug level, you can selectively enable logging only for some components.
# To enable all selectors use ["*"]. Examples of other selectors are "beat",
# "publish", "service".
#logging.selectors: ["*"]

#============================== Xpack Monitoring ===============================
# filebeat can export internal metrics to a central Elasticsearch monitoring
# cluster.  This requires xpack monitoring to be enabled in Elasticsearch.  The
# reporting is disabled by default.

# Set to true to enable the monitoring reporter.
#xpack.monitoring.enabled: false

# Uncomment to send the metrics to Elasticsearch. Most settings from the
# Elasticsearch output are accepted here as well. Any setting that is not set is
# automatically inherited from the Elasticsearch output configuration, so if you
# have the Elasticsearch output configured, you can simply uncomment the
# following line.
#xpack.monitoring.elasticsearch:
```

