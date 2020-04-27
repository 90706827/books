# 生成Gitlab、Github SSH Keys

## 创建目录

```sh
C:\Users\Administrator>
查询目录下是否存在.ssh目录
不存在创建目录
mkdir .ssh
```

## 创建 SSH Key

```sh
cd .ssh

git config --global user.name "zgq" 
git config --global user.email "90706827@163.com"
ssh-keygen -t rsa -C "90706827@163.com" 回车
输入gitlib 
回车
回车
ssh-keygen -t rsa -C "90706827@163.com" 回车
输入github 
回车
回车
```

## 配置Gitlib和Github的SSH密钥

登录gitlib和github分别配置自己的ssh密钥

## 配置

创建文件 config

	# gitlab
	Host gitlab.com
		HostName gitlab.com
		PreferredAuthentications publickey
		IdentityFile gitlab
		User zgq
	# github
	Host github.com
		HostName github.com
		PreferredAuthentications publickey
		IdentityFile github
		User 90706827
## 测试

```sh

ssh git@gitlab.com
#显示如下标示成功
	Welcome to GitLab, @zgq!
	Connection to gitlab.com closed.
	
ssh git@github.com
#显示如下标示成功
	Hi 90706827! You've successfully authenticated, but GitHub does not provide shell access.
	Connection to github.com closed.
```

## 使用

