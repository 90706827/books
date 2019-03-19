# JVM性能调优

## 一、JDK自带监控工具使用

远程程序启动设置

```shell
java -cp . -Dcom.sun.management.jmxremote.port=8999 -Dcom.sun.managent.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar app.jar
```

设置端口：-Dcom.sun.management.jmxremote.port

无密连接：-Dcom.sun.managent.jmxremote.authenticate=false

关闭注册认证：-Dcom.sun.management.jmxremote.ssl=false

启动JDK目录bin中的jconsole.exe执行文件

![](img\20180809144719.png)

