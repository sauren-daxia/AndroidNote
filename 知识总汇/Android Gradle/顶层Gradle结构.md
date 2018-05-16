## 顶层build.gradle结构
```groovy
buildscript {			

    repositories {
        jcenter()
        google()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.1
    }
}
```
```groovy
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }		//需要url的表示使用某个服务器的仓库
        maven { url "https://maven.google.com" }
        google()
    }

    ext {			//全局变量
	minSdkVersion = 16
	targetSdkVersion = 24
    }
}
```
```groovy
task clean(type: Delete) {
    delete rootProject.buildDir
}
```

1、buildscript：定义Android工具的类路径，

2、dependencies：

3、allprojects：配置所有的模块build.gradle使用的仓库，一个build.gradle就是一个模块，中定义的属性会被应用到所有 moudle 中

4、repositories：仓库，可以添加多个仓库，可以添加支持maven的仓库

5、ext{} ：全局变量，可以在各个module使用，使用方法: rootProject.ext.minSdkVersion

