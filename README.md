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
	  compile 'com.github.ChenArno:eros-android-ryim:1.0.0'
	}
```
引用插件，构造完毕以后，
在gradle.properties中增加该配置
融云appid从官方获取
- [https://developer.rongcloud.cn/app](https://developer.rongcloud.cn/app)
- appid 分测试和正式版本，在上线之后一定要跟测试版本分开来
```Html
RONGYUN_APPID=xxxxxx
```
并在主app文件夹找到App.java
添加
```Java
public class App extends BMWXApplication {
    public Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        ImApp.getImApp(this);
    }

}
```

在融云官网注册以后，填入userId获取token(改token应当通过后台获取，测试阶段可以从官网获取)
当应用启动后，获取token，通过ryIm方法注册调用
```Js
const ryIm = weex.requireModule('ryIm')
ryIm.init(token,res => {
          console.log('===>连接成功，userId:' + res)
        },
        err => {
          console.log('===>' + err)
        }
      )
```

聊天列表组件，子组件需包含在该组件下
获取当前聊天对象列表

```Js
<vanz-im-view ref="imview">
  ...
  <div @click="enterRoom(item)">111</div>
  ...
</vanz-im-view>

this.$refs.imview.selectList(r => {
  console.log('===>获取数据成功' + JSON.stringify(r))
})
let type = 1; //单聊模式
//userId为注册时的唯一id，name为进入聊天窗口的标题
this.$refs.imview.enterRoom(type, userId, name)
```

删除该聊天记录

```Js
let type = 1; //单聊模式
this.$refs.imview.deleteItem(type,userId,res=>{
  console.log(res)
})
```