package com.yiyanf.fang.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.yiyanf.fang.ui.activity.MainActivity;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.SPUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Iterator;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "Fang-JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();

		LogUtil.d(TAG, "[MyReceiver] onReceive - " + action + ", extras: " + printBundle(bundle));
		if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
			LogUtil.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: ");
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
			// 接收到通知消息
			String jsonExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			LogUtil.v(TAG, "[MyReceiver] 接收到推送下来的通知;"+jsonExtra);
			try {
				JSONObject jsonObject = new JSONObject(jsonExtra);
				String pushTag = jsonObject.optString("tag");
				if(pushTag.equals(MainActivity.DELETE_VIDEO)){
					// 删除视频什么也不做
					return ;
				}else if(pushTag.equals(MainActivity.ATTENTIONS)){
					int unreadSilenceCount = (int) SPUtils.get("unread_silence",0);
					unreadSilenceCount++;
					sendAttentionsBroadcast(context,unreadSilenceCount);
					SPUtils.put("unread_silence",unreadSilenceCount);
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			int unreadCount = (int) SPUtils.get("unread_msg",0);
			unreadCount++;
			sendBroadcast(context,unreadCount);
			SPUtils.put("unread_msg",unreadCount);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			// 用户点击打开了通知
			LogUtil.e(TAG, "[MyReceiver] 用户点击打开了通知");
			Intent i = new Intent(context, MainActivity.class);
			i.putExtras(bundle);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
			context.startActivity(i);
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			// 收到富文本消息通知
			LogUtil.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			//在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
					LogUtil.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					LogUtil.e(TAG, "Get message extra JSON error!");
				}
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private void sendBroadcast(Context mContext,int unread){
		Intent intent = new Intent("com.yiyanf.fang.msg");
		intent.putExtra("msg_unread",unread);
		mContext.sendOrderedBroadcast(intent, null);
	}


	private void sendAttentionsBroadcast(Context mContext,int unread){
		Intent intent = new Intent("com.yiyanf.fang.attentions");
		intent.putExtra("attention_videos_unread",unread);
		mContext.sendOrderedBroadcast(intent, null);
	}
}
