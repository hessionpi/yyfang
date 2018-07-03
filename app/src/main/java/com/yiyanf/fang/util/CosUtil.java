package com.yiyanf.fang.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.tencent.cos.COSClient;
import com.tencent.cos.COSConfig;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.yiyanf.fang.model.UserInfoCenter;
import static com.yiyanf.fang.FangConstants.COS_APPID;
import static com.yiyanf.fang.FangConstants.COS_BUCKET;
import static com.yiyanf.fang.FangConstants.COS_REGION;

/**
 * COS上传工具类
 *
 * Created by Administrator on 2017/10/24.
 */

public class CosUtil {

    private Context mContext;
    private OnUploadListener mListener;
    private Handler mMainHandler;


    public CosUtil(Context mContext, OnUploadListener listener) {
        this.mContext = mContext;
        this.mListener = listener;

        mMainHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (mListener != null) {
                            mListener.onSuccess(msg.arg1, (String) msg.obj);
                        }
                        break;

                    default:
                        if (mListener != null) {
                            mListener.onFailed(msg.arg1, (String) msg.obj);
                        }
                        break;
                }
                return false;
            }
        });

    }



    //上传图片
    public void upLoad(String sign, String path, boolean isAgain) {
        COSConfig config = new COSConfig();
        //设置园区
        config.setEndPoint(COS_REGION);
        //创建COSlient对象，实现对象存储的操作
        COSClient cos = new COSClient(mContext, COS_APPID, config, "");
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(COS_BUCKET);
        putObjectRequest.setCosPath("/user"+"/" + UserInfoCenter.getInstance().getUserId() + "/" + System.currentTimeMillis());
        putObjectRequest.setSrcPath(path);
        putObjectRequest.setSign(sign);
        putObjectRequest.setListener(new IUploadTaskListener() {

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                PutObjectResult result = (PutObjectResult) cosResult;
                Message msg = new Message();
                msg.what = result.code;
                msg.obj = result.access_url;
                mMainHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
                Message errmsg = new Message();
                errmsg.what = cosResult.code;
                errmsg.obj = cosResult.msg;
                mMainHandler.sendMessage(errmsg);
                LogUtil.w("Hition==", "上传出错： ret =" + cosResult.code + "; msg =" + cosResult.msg);
            }

            @Override
            public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
                //  float progress = (float) currentSize / totalSize;
                //    progress = progress * 100;
                //    LogUtil.w("TEST", "进度：  " + (int) progress + "%");
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {

            }
        });
        PutObjectResult result = cos.putObject(putObjectRequest);

        // 是否重试，如果是，则重试一次结束，这里是一次递归调用
        if(0 != result.code && isAgain){
            upLoad(sign,path,false);
        }
    }

    public interface OnUploadListener {
        void onSuccess(int code,String url);

        void onFailed(int errorCode,String msg);
    }
}
