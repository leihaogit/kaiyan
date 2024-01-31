plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.hal.kaiyan"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.hal.kaiyan"
        minSdk = 26
        targetSdk = 33
        versionCode = 101
        versionName = "1.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters.add("armeabi-v7a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.preference:preference:1.2.0")//偏好设置
    implementation("com.tencent:mmkv:1.2.13") // 键值对存储
    implementation("com.scwang.smart:refresh-layout-kernel:2.0.0") //下拉刷新
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-java:v8.4.0-release-jitpack") // java 支持
    implementation("com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-armv7a:v8.4.0-release-jitpack") // armv7a
    implementation("de.hdodenhof:circleimageview:3.1.0") // 圆形的 ImageView
    implementation("com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar") // 第三方 TabLayout
    implementation("androidx.room:room-paging:2.5.1") // Room 的分页支持依赖
    kapt("androidx.room:room-compiler:2.5.1") // Room 的注解处理器，用于生成部分代码
    implementation("androidx.room:room-runtime:2.5.1") // Room 运行时库
    implementation("io.supercharge:shimmerlayout:2.1.0") // 用于实现闪烁效果的库
    implementation("androidx.appcompat:appcompat:1.6.1") // AppCompat 库，提供了向下兼容的 UI 组件
    implementation("com.google.android.material:material:1.8.0") // Material Design 库，提供了丰富的 UI 组件和样式
    implementation("androidx.constraintlayout:constraintlayout:2.1.4") // ConstraintLayout 布局库，提供了灵活的布局管理方式
    implementation("androidx.core:core-ktx:1.9.0") // Kotlin 标准库的扩展函数库
    implementation("androidx.paging:paging-runtime-ktx:3.1.1") // 分页库，提供了对分页数据加载的支持
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3") // Navigation 库的 Fragment 扩展函数库
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3") // Navigation 库的 UI 扩展函数库
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0") // 下拉刷新控件
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Retrofit 库的 Gson 转换器
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // 网络请求库
    implementation("com.github.bumptech.glide:glide:4.13.2") // 图片加载库
    implementation("com.github.chrisbanes:PhotoView:2.3.0") // 图片缩放库
    implementation("com.geyifeng.immersionbar:immersionbar:3.2.2") // 沉浸式状态栏和导航栏库
    implementation("com.airbnb.android:lottie:6.1.0") // 动画库，提供了 JSON 格式动画文件的解析和播放功能
    implementation("com.github.GrenderG:Toasty:1.5.2") // 提示框库，提供了多种样式的提示框
}