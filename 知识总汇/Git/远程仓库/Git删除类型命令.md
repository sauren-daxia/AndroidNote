## 删除分支
```
$ git branch -d dev
```

## 强行删除分支
```
//可以删除未合并分支
$ git branch -D dev
```

## 恢复隐藏分支并删除隐藏分支
```
//隐藏分支存储在`stash list` 中，即使恢复后也会存在`stash list`中
//如果`stash list`存在多个，可以指定某个恢复并删除`git stash pop start@{0}`，具体编号查看`$ git stash list`
$ git stash pop
```