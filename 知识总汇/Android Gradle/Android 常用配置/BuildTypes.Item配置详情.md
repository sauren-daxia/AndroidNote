```gradle
buildTypes{
    debug{}
    release{
        //版本号
        versionCode 1

        //版本名
        versionName "1.1.0"

        //定义Config常量：代码使用BuildConfig.DEBUG
        buildConfigField "String" , "DEBUG" ,true

        //定义资源：代码使用R.String.AppName
        resValue "String","AppName","MVVM"                   

        //开启Zipalign优化
        zipAlignEnabled true              
        
        //开启混淆
        minifyEnabled true                           

        //移除无用的resource文件，此项只有在开启混淆minifyEnabled=true时才生效
        shrinkResources true           

        //混淆文件所在，第一个是默认的，第二个是自定义的，默认就好
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'

        //配置签名文件
        signingConfig signingConfigs.debug      
       
    }
}
```