#### 多渠道配置
```gradle
//生成的文件都在app/build/output/apk/
android{
    android.applicationVariants.all { variant ->
        variant.outputs.all { output ->
            outputFileName  = "MVVMTest-${releaseTime()}-${defaultConfig.versionName}.apk"
        }
    }
}

def outputTime{
    return new Data().format("yyyyMMDD")
}
```

#### 常规配置用于没有多渠道
```gradle
android{
    versionName  "1.0.0"
    versionCode  1
    defaultConfig{
         setProperty("archivesBaseName", "feige_v${versionName}_${versionCode}")
    }
}
```