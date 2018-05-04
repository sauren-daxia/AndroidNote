### 克隆
```
//将远程仓库克隆到本地目录
$ git clone https://github.com/c297131019/JRatingBar.git
```

## 添加远程仓库(远程仓库必须是空的)
```
//将远程仓库和本地仓库关联
$ git remote add https://github.com/c297131019/JRatingBar.git
```

## 第一次推送到远程仓库
```
//经过上面步骤关联之后，如果不是的话参考别的推送方式
//-u表示Git不但会把本地的master分支内容推送的远程新的master分支，还会把本地的master分支和远程的master分支关联起来
$ git push -u origin master
```

## 关联后推送
```
//与第一次推送区别于`-u`命令
//表示将当前的`master`分支推送到远程仓库，也可以推送其他分支
$ git push origin master
```

## 拉取最新分支内容
```
//每次推送远程仓库之前先获取一下最新内容
$ git pull
```

## 创建分支
```
$ git branch dev
```

## 创建并切换分支
```
$ git checkout -b dev
```