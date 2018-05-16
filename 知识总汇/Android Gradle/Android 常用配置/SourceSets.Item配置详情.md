```gradle
/**
* source是对app/src目录下的文件做配置，一般都只配置main和test目录下的，他们的结构是一样的
*/
sourceSets {
    test{}

    main {
        //根据变量判断Debug模式的时候指定AndroidMainifest文件，其他目录也可以这样判断设置
        if (isDebug) {
            manifest.srcFile 'src/main/AndroidManifest.xml'
        } else {
            manifest.srcFile 'src/main/java2/AndroidManifest.xml'
        }

        //配置Java代码文件目录
        java.srcDirs = ['src/main/java']

        //如果指定的目录在某些情况下打包编译是不需要某些文件，可以这样去掉
        java {
            exclude '/test/**'  // 不想包含文件的路径
        }

        //配置AIDL文件目录
        aidl.srcDir = ['src/main/aidl']

        //配置assets资源目录
        assets.srcDirs = ['src/main/assets']

        //配置res资源目录
        res.srcDirs = ['src/mian/res']

        //配置jniLibs文件目录，未知与jni区别
        jniLibs.srcDirs = ['libs'] //默认指appModel下的libs文件目录

        //配置jni文件目录，未知与jniLibs区别
        jni.srcDir = ['libs']//默认指appModel下的libs文件目录

        //配置resources文件目录，未知功能
        resources.srcDirs = ['src']

        //配置renderscript文件目录，未知功能
        renderscript.srcDirs = ['src']
    }
}
```