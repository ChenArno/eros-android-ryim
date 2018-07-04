package com.vanz.ryim;

import android.content.Context;

import com.taobao.weex.WXSDKEngine;

import io.rong.imkit.RongIM;

public class ImApp {
    private static ImApp imApp;

    private ImApp(){

    }

    public static ImApp getImApp(Context context) {
        if(imApp == null){
            imApp = new ImApp();
        }

//        融云会话初始化
        RongIM.init(context);
        WeexInit();
        return imApp;
    }

    public static void WeexInit(){
        try {
            WXSDKEngine.registerComponent("vanz-im-view", VanzImView.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
