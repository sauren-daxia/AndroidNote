1、先将当前工作分支隐藏，假如叫`workBranch`
`$ git stash`

2、切换到有问题的分支
`$ git checkout dev`

3、创建一个临时分支用于修补该分支的bug
`$ git branch -b issue-101`

4、提交修复的bug
`$ git add readme.txt`
`$ git commit -m"fix issue-101"`

5、修补完后切换到问题分支
`$ git checkout dev`

6、合并分支
`$ git merge --no-ff -m"fix issue-101" dev`

7、删除零时分支
`$ git branch -d issue-101`

8、把问题分支推送远程
`$ git push origin dev`

9、切换到工作分支
`$ git checkout workBranch`

10、查看隐藏分支
`$ git stash list`

11、找到隐藏分支恢复并删除
`$ git stash pop stash@{0}`