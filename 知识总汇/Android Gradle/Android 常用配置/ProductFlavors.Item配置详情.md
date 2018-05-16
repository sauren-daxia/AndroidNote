```gradle
productFlavors{
    huawei{}
    xiaomi{
        //版本号
        versionCode 1

        //版本名
        versionName '1.1.0'

        //设置包名
        applicationId "com.chenzhijian.mvvm"

        //设置最小支持SDK版本
        minSdkVersion 19

        //设置最大支持SDK版本，一般不设置
        maxSdkVerson 27

        //配置渠道使用，替换AndroidManifest.xml中的mate-data value属性
        manifestPlaceholders = [UMENG_APP_KEY: "你替代的内容"]

        //引用默认包名做前缀，设置包名
        applicationIdSuffix ".debug" //事实上是com.chenzhijian.mvvm.debug

        //是否开启Dex分包
        multiDexEnabled false

         //定义Config常量：代码使用BuildConfig.DEBUG
        buildConfigField "String" , "DEBUG" ,true

        //定义资源：代码使用R.String.AppName
        resValue "String","AppName","MVVM"     

    }
}
```