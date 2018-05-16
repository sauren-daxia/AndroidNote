```gradlew
signingConfigs{
    debug{}
    release{
        //签名文件路径，在项目中可以写相对路径，不在则全路径
        storeFile file("MyStore.jks")
        //签名密码
        storePassword ""
        //签名别名
        keyAlias ""
        //别名密码
        keyPassword ""
    }
}
```

#### TIPS USE
```gradle
android{
    buildTypes{
        debug{
            signingConfig signingConfigs.debug
        }
    }
}
```