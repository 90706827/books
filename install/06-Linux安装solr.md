# CentOS安装solr

##安装

[solr下载地址，各种版本，我们下载tgz格式 ](http://archive.apache.org/dist/lucene/solr/)

[下载速度快可以去官网下载，下载地址](http://mirror.bit.edu.cn/apache/lucene/solr/7.4.0/solr-7.4.0.tgz)

下载：

```shell
wget http://mirror.bit.edu.cn/apache/lucene/solr/7.4.0/solr-7.4.0.tgz
```

解压

```shell
tar -zxvf solr-7.4.0.tgz
```

启停命令

```shell
bin/solr stop -all 		关闭solr服务
bin/solr start -force 		开启solr服务
bin/solr status			查看solr服务状态
```

##使用

拷贝文件

```shell
cp -r /mnt/solr-7.4.0/server/solr/configsets/_default/* /mnt/solr-7.4.0/server/solr/test/
```

创建core

![](img\20180105170807961.png)

## 链接mysql数据库

1. 拷贝jar包到如下目录

   mysql-connector-java-5.1.46.jar

   solr-dataimporthandler-7.4.0.jar

   solr-dataimporthandler-extras-7.4.0.jar

   ```shell
   /mnt/solr-7.4.0/server/solr-webapp/webapp/WEB-INF/lib
   ```

   

2. 修改配置

   ```shell
   cd /mnt/solr-7.4.0/server/solr/test/conf/
   vi solrconfig.xml 
   /requestHandler #查找命令 回车后找的第一
   ```

   修改如下内容**找到第一個requestHandler修改**

   ```xml
   <requestHandler name="/dataimport"
        class="org.apache.solr.handler.dataimport.DataImportHandler"> 
          <lst name="defaults"> 
             <str name="config">data-config.xml</str> 
          </lst> 
   </requestHandler>
   ```

   创建data-config.xml配置文件

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <dataConfig>
       <dataSource type="JdbcDataSource"
                   driver="com.mysql.jdbc.Driver"
                   url="jdbc:mysql://192.168.1.105:3306/solr"
                   user="root"
                   password="root" />
       <document>
           <entity name="person" query="select * from user">
               <field column="ID" name="id" />
               <field column="name" name="name" />
               <field column="passward" name="passward" />
           </entity>
       </document>
   </dataConfig>
   ```

   

3. asdf