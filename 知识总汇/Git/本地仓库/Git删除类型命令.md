### 删除文件
```
当工作区的文件被删除掉，`git`仓库会有变化，这时候有两个选择
1. 因为是不小心删除的，所以可以将文件复原最新一次的提交，使用`$ git checkout -- README.txt`
2. 把`git`仓库中的文件也删除并且`commit`，这样在以后的版本就不会对该文件记录，使用`$ git rm README.txt`
$ git rm README.txt
```