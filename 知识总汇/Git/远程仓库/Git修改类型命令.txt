## 切换分支
```
$ git checkout dev
```

## 合并分支
```
//是当前分支与目标分支合并
//如果有冲突，先解决冲突后再`add`、`commit`
//`--on-ff`表示不使用`Fast forward`模式合并
//`Fast forward`模式合并后不会有分支记录，`--no-ff`则有，建议使用
$ git merge --no-ff -m"" dev
```

## 隐藏分支
```
//用于当前文件有修改时，并不能直接切换分支
//将当前分支的状态隐藏，便于临时需要切换其他分支工作
$git stash
```

## 恢复隐藏分支
```
//只恢复隐藏分支，并不删除`stash list`中的隐藏分支
//如果`stash list`存在多个，可以指定某个恢复并删除`git stash apply start@{0}`，具体编号查看`$ git stash list`
$ git stash apply
```



