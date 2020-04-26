# 搭建NodeJs环境

[Node安装文件下载路径](https://nodejs.org/zh-cn/download/ )

# Linux系统

## 下载

```sh
wget https://nodejs.org/dist/v12.16.2/node-v12.16.2-linux-x64.tar.xz
```

## 解压

```sh
tar -xvf node-v12.16.2-linux-x64.tar.xz
```

## 复制

```sh
cd node-v12.16.2-linux-x64
mkdir /usr/local/src/node-v12.16.2
mv * /usr/local/src/node-v12.16.2
```

## 软连接

```sh
ln -s /usr/local/src/node-v12.16.2/bin/npm  /usr/local/bin/
ln -s /usr/local/src/node-v12.16.2/bin/node /usr/local/bin/
```

##  验证

```sh
npm -v
node -v
```

