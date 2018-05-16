### 查看仓库中文件修改装填
```
可以查看新增，未添加，待提交等状态
Untracked : 未跟踪，表示未add到管理，或者不参与管理
Unmodify  : 文件参与管理，但没修改过
Modified  : 文件参与管理，并且修改过，下一步可以add到暂存区或者checkout丢弃修改内容
Staged    : 已经添加到暂存区，下一步可以commit或者reset HEAD移除暂存区

$ git status
```

### 未`add`或`commit`之前，查看某个文件被修改的具体内容
```
-表示被删除 +表示新增的内容
$ git diff README.txt
```

### 查看历史操作
```
可以查看任何修改的操作历史记录，指的是修改git的命令，不是文件内容
$ git reflog
```

### 查看提交日志(所有查看日志之后需要输入q，才能退出查看日志功能)
```
$ git log
```

### 查看提交日志筛选
```
oneline是一种筛选类型，表示一行显示一条提交信息，short表示简要信息，还有很多，常用的就是这两种
$ git log --pretty=online
```