## 安装Nexus私服

### 下载

```shell
cd /opt
wget https://sonatype-download.global.ssl.fastly.net/repository/repositoryManager/3/nexus-3.14.0-04-unix.tar.gz
```

### 部署

```shell
#解压
tar -zxvf nexus-3.14.0-04-unix.tar.gz 
#创建链接
mv nexus-3.14.0-04 nexus
#创建 nexus 用户
useradd nexus 
#授权
chown -R nexus:nexus /opt/nexus
chown -R nexus:nexus /opt/sonatype-work/
#打开 /opt/nexus/bin/nexus.rc 文件, 去掉 run_as_user 变量的注释
vi /opt/nexus/bin/nexus.rc

run_as_user="nexus"

#启动
cd nexus/bin/
./nexus run &
# 显示如下信息提示 表示启动成功
-------------------------------------------------
Started Sonatype Nexus OSS 3.14.0-04
-------------------------------------------------
```

### 开启防火墙

```shell
firewall-cmd --zone=public --permanent --add-port=8081/tcp
firewall-cmd --reload
```

### 访问测试

```shell
访问地址： http://ip:8081/
访问凭证(默认的用户名和密码)：
username: admin
password: admin123
```

### Maven使用Nexus私库

#### 创建目录

![实例](E:/IdeaWorkspace/java/doc/img/nexus1.png)

![实例](E:/IdeaWorkspace/java/doc/img/nexus2.png)

#### Setting.xml配置

```xml
<servers>
    <server>
        <id>releases</id>
        <username>user</username>
        <password>user</password>
    </server>
    <server>
        <id>Snapshots</id>
        <username>user</username>
        <password>user</password>
    </server>
</servers>
```

#### Maven pom.xml配置

```xml
<!-- 生产jar包配置 -->
<distributionManagement>
    <repository>
        <id>releases</id>
        <url>http://192.168.0.121:8081/repository/java/</url>
    </repository>
    <snapshotRepository>
        <id>Snapshots</id>
        <url>http://192.168.0.121:8081/repository/java/</url>
    </snapshotRepository>
</distributionManagement>

<build>
    <plugins>
        <!-- 打jar包插件 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.0.2</version>
            <configuration>
                <excludes>
                    <exclude>**/*.properties</exclude>
                </excludes>
            </configuration>
        </plugin>
        <!-- 打包源码插件 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-source-plugin</artifactId>
            <version>3.0.1</version>
            <configuration>
                <attach>true</attach>
            </configuration>
            <executions>
                <execution>
                    <phase>compile</phase>
                    <goals>
                       <goal>jar</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>

<!-- 使用jar包配置 -->
<!-- repository可以添加多个 -->
<!-- releases-enabled可以从这个仓库下载releases版本的构件 -->
<!-- snapshots-snapshots不要从这个仓库下载snapshot版本的构件，禁止从公共仓库下载snapshot构件是推荐的做法，因为这些构件不稳定，且不受你控制，你应该避免使用，如果你想使用局域网内组织内部的仓库，你可以激活snapshot的支持 -->
<!-- layout布局 default和legacy-->
<repositories>  
  <repository>  
    <id>neuxs</id>  
    <name>neuxs</name>  
    <url>https://repo.maven.apache.org/maven2</url>  
    <releases>  
      <enabled>true</enabled>  
    </releases>  
    <layout>default</layout>  
    <snapshots>  
      <enabled>true</enabled>  
    </snapshots>  
  </repository>  
</repositories>
<!-- pluginRepositories用来指定下载插件仓库的地址的 -->
<!-- -->
<!-- -->
<pluginRepositories>
	<pluginRepository>
		<id>neuxs</id>
        <name>neuxs</name>
		<url>http://192.168.0.121:8081/repository/java/</url>
	</pluginRepository>
</pluginRepositories>
```

