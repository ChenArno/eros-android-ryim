package com.vanz.ryim;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


@WeexModule(name = "ryIm", lazyLoad = true)
public class VanzIm extends WXModule{

    @JSMethod(uiThread = false)
    public void init(String token, final JSCallback succ, final JSCallback error){
        logout();

        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                if(error != null) error.invoke("Token 错误");
            }
            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
                RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
                succ.invoke(userid);
            }
            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                if(error != null) error.invoke("连接融云失败,"+errorCode);
            }
        });
    }

    @JSMethod(uiThread = false)
    public void logout(){
        RongIM.getInstance().logout();
    }

    /**
     * <p>启动会话界面。</p>
     * <p>使用时，可以传入多种会话类型 {@link io.rong.imlib.model.Conversation.ConversationType} 对应不同的会话类型，开启不同的会话界面。
     * 如果传入的是 {@link io.rong.imlib.model.Conversation.ConversationType#CHATROOM}，sdk 会默认调用
     * {@link RongIMClient#joinChatRoom(String, int, RongIMClient.OperationCallback)} 加入聊天室。
     * 如果你的逻辑是，只允许加入已存在的聊天室，请使用接口 {@link #startChatRoomChat(Context, String, boolean)} 并且第三个参数为 true</p>
     *
     * @param context          应用上下文。
     * @param conversationType 会话类型。
     * @param targetId         根据不同的 conversationType，可能是用户 Id、群组 Id 或聊天室 Id。
     * @param title            聊天的标题，开发者可以在聊天界面通过 intent.getData().getQueryParameter("title") 获取该值, 再手动设置为标题。
     */

    @JSMethod(uiThread = true)
    public void enterRoom(String targetId,String title){
        RongIM.getInstance().startConversation(mWXSDKInstance.getContext(), Conversation.ConversationType.PRIVATE,targetId,title);
    }
}
