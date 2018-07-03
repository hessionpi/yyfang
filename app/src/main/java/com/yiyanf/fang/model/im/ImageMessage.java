package com.yiyanf.fang.model.im;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.tencent.TIMCallBack;
import com.tencent.TIMImage;
import com.tencent.TIMImageElem;
import com.tencent.TIMImageType;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.yiyanf.fang.FangApplication;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.activity.ImageViewActivity;
import com.yiyanf.fang.ui.adapter.im.ChatAdapter;
import com.yiyanf.fang.util.IMFileUtil;
import com.yiyanf.fang.util.ToastUtils;
import java.io.File;
import java.io.IOException;

/**
 * 图片消息数据
 */
public class ImageMessage extends Message {

    private static final String TAG = "ImageMessage";
    private boolean isDownloading;

    public ImageMessage(TIMMessage message){
        this.message = message;
    }

    public ImageMessage(String path){
        this(path, false);
    }

    /**
     * 图片消息构造函数
     *
     * @param path 图片路径
     * @param isOri 是否原图发送
     */
    public ImageMessage(String path, boolean isOri){
        message = new TIMMessage();
        TIMImageElem elem = new TIMImageElem();
        elem.setPath(path);
        elem.setLevel(isOri?0:1);
        message.addElement(elem);
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context 显示消息的上下文
     */
    @Override
    public void showMessage(final ChatAdapter.ViewHolder viewHolder, final Context context) {
        clearView(viewHolder);
        TIMImageElem e = (TIMImageElem) message.getElement(0);
        switch (message.status()){
            case Sending:

                ImageView imageView = new ImageView(FangApplication.getApplication());
                imageView.setImageBitmap(getThumb(e.getPath()));
                clearView(viewHolder);
                getBubbleView(viewHolder).addView(imageView);
                break;
            case SendSucc:
                for(final TIMImage image : e.getImageList()) {
                    if (image.getType() == TIMImageType.Thumb){
                        final String uuid = image.getUuid();
                        if (IMFileUtil.isCacheFileExist(uuid)){
                            showThumb(viewHolder,uuid);
                        }else{
                            image.getImage(new TIMValueCallBack<byte[]>() {
                                @Override
                                public void onError(int code, String desc) {//获取图片失败
                                    //错误码code和错误描述desc，可用于定位请求失败原因
                                    //错误码code含义请参见错误码表
                                    Log.e(TAG, "getImage failed. code: " + code + " errmsg: " + desc);
                                }

                                @Override
                                public void onSuccess(byte[] data) {//成功，参数为图片数据
                                    IMFileUtil.createFile(data, uuid);
                                    showThumb(viewHolder,uuid);
                                }
                            });
                        }
                    }
                    if (image.getType() == TIMImageType.Original){
                        final String uuid = image.getUuid();
//                        setImageEvent(viewHolder, uuid,context);
                        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                navToImageview(image, context);
                            }
                        });
                    }
                }
                break;
        }
        showStatus(viewHolder);


    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return FangApplication.getApplication().getString(R.string.summary_image);
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save(Context context) {
        final TIMImageElem e = (TIMImageElem) message.getElement(0);
        for(TIMImage image : e.getImageList()) {
            if (image.getType() == TIMImageType.Original) {
                final String uuid = image.getUuid();
                image.getImage(new TIMValueCallBack<byte[]>() {
                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG, "getFile failed. code: " + i + " errmsg: " + s);
                    }

                    @Override
                    public void onSuccess(byte[] bytes) {
                        Context mContext = FangApplication.getApplication();
                        if (IMFileUtil.isFileExist(uuid+".jpg", Environment.DIRECTORY_DOWNLOADS)) {
                            ToastUtils.show(mContext,R.string.save_exist);
                            return;
                        }
                        File mFile = IMFileUtil.createFile(bytes, uuid+".jpg", Environment.DIRECTORY_DOWNLOADS);
                        if (mFile != null) {
                            ToastUtils.show(mContext,mContext.getString(R.string.save_succ)+"path : " + mFile.getAbsolutePath());
                        } else {
                            ToastUtils.show(mContext,R.string.save_fail);
                        }
                    }
                });
            }
        }
    }

    /**
     * 生成缩略图
     * 缩略图是将原图等比压缩，压缩后宽、高中较小的一个等于198像素
     * 详细信息参见文档
     */
    private Bitmap getThumb(String path){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width=options.outWidth, height=options.outHeight;
        if (width > height){
            reqWidth = 198;
            reqHeight = (reqWidth * height)/width;
        }else{
            reqHeight = 198;
            reqWidth = (width * reqHeight)/height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try{
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            ExifInterface ei =  new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        }catch (IOException e){
            return null;
        }
    }

    private void showThumb(final ChatAdapter.ViewHolder viewHolder,String filename){
        Bitmap bitmap = BitmapFactory.decodeFile(IMFileUtil.getCacheFilePath(filename));
        ImageView imageView = new ImageView(FangApplication.getApplication());
        imageView.setImageBitmap(bitmap);
        getBubbleView(viewHolder).addView(imageView);
    }

    private void setImageEvent(final ChatAdapter.ViewHolder viewHolder, final String fileName, final Context context){
        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageViewActivity.startActivity(context,fileName);
                /*Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("filename", fileName);
                context.startActivity(intent);*/
            }
        });
    }

    private void navToImageview(final TIMImage image, final Context context){
        if (IMFileUtil.isCacheFileExist(image.getUuid())){
            ImageViewActivity.startActivity(context,image.getUuid());
            /*Intent intent = new Intent(context, ImageViewActivity.class);
            intent.putExtra("filename", image.getUuid());
            context.startActivity(intent);*/
        }else{
            if (!isDownloading){
                isDownloading = true;
                image.getImage(IMFileUtil.getCacheFilePath(image.getUuid()), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code含义请参见错误码表
                        Log.e(TAG, "getImage failed. code: " + i + " errmsg: " + s);
                        ToastUtils.show(context,R.string.download_fail);
                        isDownloading = false;
                    }

                    @Override
                    public void onSuccess() {
                        isDownloading = false;
                        ImageViewActivity.startActivity(context,image.getUuid());
                        /*Intent intent = new Intent(context, ImageViewActivity.class);
                        intent.putExtra("filename", image.getUuid());
                        context.startActivity(intent);*/
                    }
                });
            }else{
                ToastUtils.show(context,R.string.downloading);
            }
        }
    }
}
