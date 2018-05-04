1. 使用初始化一个仓库，然后使用`Linux`命令创建一个文本文件名为`README.txt`，输入`爱老虎油`，接着提交到暂存区，然后查看状态是否添加到了暂存区，最后提交到`.git`仓库。提交信息为==提交新建文件夹==
```
关键命令
touch README.txt
$ git init
$ git add README.txt
$ git status`
$ git commit -m"提交新文件"
```

2. 对`README.txt`新增一行文字为`stupid boss`，然后查看本次修改和上次的版本的差异是否多了一行`stupid boss`，接着撤销这次的修改，随后新增一行`爱你中国`，最后添加到到暂存并且提交到仓库，提交信息为==表白==
```
关键命令
$ git diff README.txt
$ git checkout -- README.txt
$ git add README.txt
$ git commit -m"表白"
```

3. 对`README.txt`新增一行`学习Git好苦逼啊`，然后添加到暂存区，接着再添加一句`现在是凌晨1:30分`，接着撤回修改，然后提交到仓库，虽然查看一下提交日志，回滚到提交信息为==表白==的记录
```
关键命令
$ git add README.txt
$ git checkout -- README.txt
$ git add README.txt
$ git commit -m"表白"
$ git log --pretty=oneline` 或 `$ git log short
$ git reset --hard HEAD^` 或 `$ git reset --hard <具体日志编号>
```

4. 对`README.txt`新增一行`无聊不想工作`并且添加到暂存区，然后再将文件移除暂存区并撤销这次修改
```
关键命令
$ git add README.txt
$ git reset HEAD README.txt
$ git checkout -- README.txt
```

5. 新建一个文件名为`test.txt`，先添加到暂存区然后提交到仓库，提交信息为==测试删除==，然后使用`Linux`命令删除文件，接着再恢复文件，再用命令删除文件，最后在`git`仓库中也把这个文件删除，最后提交，提交信息为==彻底删除==
```
关键命令
touch test.txt
$ git add test.txt
$ git commit -m"测试删除"
rm test.txt
$ git checkout -- test.txt
rm test.txt
$ git rm test.txt
$ git commit -m"删除成功"
```

6. 新建一个分支`dev`，然后查看当前分支，接着对`README.txt`新增一行`测试分支合并`，然后提交，提交信息为==测试分支合并==，最后切换到主分支，进行合并，合并后删除分支
```
关键命令
$ git checkout -b dev
$ git branch
$ git add README.txt
$ git commit -m"测试分支合并"
$ git checkout master
$ git merge --no-ff dev
$ git branch -d dev
```