package com.yiyanf.fang.im;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.tencent.TIMConversationType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.yiyanf.fang.FangApplication;
import com.yiyanf.fang.R;
import com.yiyanf.fang.model.im.CustomMessage;
import com.yiyanf.fang.model.im.Message;
import com.yiyanf.fang.model.im.MessageFactory;
import com.yiyanf.fang.ui.activity.MessageActivity;
import com.yiyanf.fang.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 在线消息通知展示
 */
public class PushUtil implements Observer {

    private static int pushNum=0;

    private final int pushId=1;

    private static PushUtil instance = new PushUtil();

    private PushUtil() {
        MessageEvent.getInstance().addObserver(this);
    }

    public static PushUtil getInstance(){
        return instance;
    }

    private void PushNotify(TIMMessage msg,String senderName){
        //系统消息，自己发的消息，程序在前台的时候不通知 || Foreground.get().isForeground()
        if (msg == null ||(msg.getConversation().getType() != TIMConversationType.Group && msg.getConversation().getType()!= TIMConversationType.C2C)
                ||msg.isSelf() || msg.getRecvFlag() == TIMGroupReceiveMessageOpt.ReceiveNotNotify
                ||MessageFactory.getMessage(msg) instanceof CustomMessage) {
            return;
        }
        String contentStr;
        Message message = MessageFactory.getMessage(msg);
        if (message == null) {
            return;
        }

        contentStr = message.getSummary();
        NotificationManager mNotificationManager = (NotificationManager) FangApplication.getApplication().getSystemService(FangApplication.getApplication().NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(FangApplication.getApplication());
        Intent notificationIntent = new Intent(FangApplication.getApplication(), MessageActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(FangApplication.getApplication(), 0,
                notificationIntent, 0);
        mBuilder.setContentTitle(senderName)//设置通知栏标题
                .setContentText(contentStr)
                .setContentIntent(intent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                .setTicker(senderName+":"+contentStr) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(pushId, notify);
    }

    public static void resetPushNum(){
        pushNum=0;
    }

    public void reset(){
        NotificationManager notificationManager = (NotificationManager)FangApplication.getApplication().getSystemService(FangApplication.getApplication().NOTIFICATION_SERVICE);
        notificationManager.cancel(pushId);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            final TIMMessage msg = (TIMMessage) data;
            if (msg != null){
                final String identify = msg.getSender();
                //待获取用户资料的用户列表
                List<String> users = new ArrayList<>();
                users.add(identify);

                //获取用户资料
                TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>(){
                    @Override
                    public void onError(int code, String desc){
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        LogUtil.e("hition==", code+" "+desc);
                        PushNotify(msg,identify);
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> result){
                        if(null!=result && !result.isEmpty()){
                            PushNotify(msg,result.get(0).getNickName());
                        }else{
                            PushNotify(msg,identify);
                        }
                    }
                });
            }
        }
    }
}
