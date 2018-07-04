# eros-android-ryim
安卓融云通信

Step 1. Add the JitPack repository to your build file 
Add it in your root build.gradle at the end of repositories:

```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency

```Java
dependencies {
	  compile 'com.github.ChenArno:eros-android-ryim:-SNAPSHOT'
	}
```
引用插件，构造完毕以后，
在gradle.properties中增加该配置
```Html
RONGYUN_APPID=82hegw5u8ympx
```
并在主app文件夹找到App.java
添加
```Java
ImApp.getImApp(this);
```

