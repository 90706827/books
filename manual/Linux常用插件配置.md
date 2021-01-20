## Linux常用插件配置

### Git配置

```sh
## 下载
wget https://mirrors.edge.kernel.org/pub/software/scm/git/git-2.30.0.tar.gz
## 解压
tar -zxvf git-2.30.0.tar.gz
## 进入目录
cd git-2.30.0
## 编译
make prefix=/usr/local/git all
## 运行
make prefix=/usr/local/git install 
## 修改环境变量
vi /etc/profile
## 加入
export PATH=$PATH:/usr/local/git/bin
## 更新环境变量
source /etc/profile
## 配置成功后可查看git版本
git --version 
## 配置用户信息
git config --global user.name "yourname" #引号里面输入你的名字
git config --global user.email "youremail" #输入邮箱
git config --global core.autocrlf false #消除由于Windows和Linux平台中换行符的差异导致的问题
git config --global core.quotepath off #消除由于路径或者是文件名包含中文导致的乱码问题
git config --global gui.encoding utf-8 #消除gui界面中文乱码问题(如果全程使用命令行的话不用担心这个问题)
ssh-keygen -t rsa -C "youremail" #配置ssh的密钥，输完之后一路回车
eval `ssh-agent` #启用ssh-agent
ssh-add ~/.ssh/id_rsa #添加密钥
ssh-add -l #将它添加到已知的key列表中
cat ~/.ssh/id_rsa.pub #把这个公钥添加到自己的Github账户上去

##实例
git config --global user.name "Mr.J"
git config --global user.email "90706827@163.com"
git config --global core.autocrlf false
git config --global core.quotepath off
git config --global gui.encoding utf-8
```

### Yarn配置

```sh
wget https://dl.yarnpkg.com/rpm/yarn.repo -O /etc/yum.repos.d/yarn.repo



```

### Docker compose

```sh
## 下载 
curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
## 下载慢可自行下载拷贝到/user/local/bin目录下改名 
## 下载页面 https://github.com/docker/compose/releases
cp docker-compose-Linux-x86_64 /usr/local/bin/docker-compose
## 将可执行权限应用于二进制文件
chmod +x /usr/local/bin/docker-compose
## 建立软连接 
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
## 查看版本
docker-compose -version

```



