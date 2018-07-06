package com.vanz.ryim;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.weex.plugin.annotation.WeexComponent;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXFrameLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;

//usingHolder 子组件继承
//@WeexComponent(names = "vanz-im-view", usingHolder = true)
public class VanzImView extends WXVContainer<WXFrameLayout> implements RongIMClient.OnReceiveMessageListener {
    private String TAG = "===>";
    public static WXSDKInstance minstance;
    private Context mcontext;

    public VanzImView(WXSDKInstance instance, WXDomObject dom, WXVContainer parent) {
        super(instance, dom, parent);
        if (minstance == null) {
            minstance = instance;
        }
    }


    @Override
    protected WXFrameLayout initComponentHostView(@NonNull Context context) {
        if (mcontext == null) {
            mcontext = context;
        }
//        return super.initComponentHostView(context);
        WXFrameLayout layout = new WXFrameLayout(context);
        RongIM.setOnReceiveMessageListener(this);
        return layout;
    }

    /**
     * <p>启动会话界面。</p>
     * <p>使用时，可以传入多种会话类型 {@link io.rong.imlib.model.Conversation.ConversationType} 对应不同的会话类型，开启不同的会话界面。
     * 如果传入的是 {@link io.rong.imlib.model.Conversation.ConversationType#CHATROOM}，sdk 会默认调用
     * {@link RongIMClient#joinChatRoom(String, int, RongIMClient.OperationCallback)} 加入聊天室。
     */

    @JSMethod
    public void enterRoom(Conversation.ConversationType conversationType,String targetId, String title) {
        Log.i(TAG, "enterRoom: " + targetId+"///"+conversationType);
        RongIM.getInstance().startConversation(mcontext, conversationType, targetId, title);
    }

    @JSMethod
    public void selectList(final JSCallback succ) {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                try {
                    if (conversations == null) {
                        succ.invoke("");
                        return;
                    }
                    JSONArray newArray = new JSONArray();
                    for(Conversation con:conversations){
                        String conent = "";
                        MessageContent messageContent = con.getLatestMessage();
                        if (messageContent instanceof TextMessage) {//文本消息
                            TextMessage textMessage = (TextMessage) messageContent;
                            conent = textMessage.getContent();
                        }
                        Map<String, String> data = new HashMap<>();
                        data.put("userId",con.getTargetId());
                        data.put("name",con.getSenderUserName());
                        data.put("content", conent);
                        data.put("lastTime", String.valueOf(con.getSentTime()));
                        data.put("typeName", con.getObjectName());
                        data.put("portraitUri", con.getPortraitUrl());
                        data.put("unreadCount",String.valueOf(con.getUnreadMessageCount()));
                        newArray.add(data);
                    }

                    succ.invoke(newArray);
                } catch (Exception e) {
                    e.printStackTrace();
                    succ.invoke(null);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }


    @JSMethod
    public void deleteItem(Conversation.ConversationType conversationType,String targetId,JSCallback success){
        Boolean suc = RongIMClient.getInstance().removeConversation(conversationType,targetId);
        if(suc){
            success.invoke("true");
        }else{
            success.invoke("false");
        }
    }

    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param i       剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int i) {
        Map<String, Object> params = new HashMap<>();
        params.put("sentTime", message.getReceivedTime());
        params.put("content", message.getContent());
        params.put("targetId", message.getTargetId());
        minstance.fireGlobalEventCallback("ryMsg.received", params);
        return false;
    }
}
