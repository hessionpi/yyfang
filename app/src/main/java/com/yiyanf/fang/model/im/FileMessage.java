package com.yiyanf.fang.model.im;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tencent.TIMCallBack;
import com.tencent.TIMFileElem;
import com.tencent.TIMMessage;
import com.yiyanf.fang.FangApplication;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.im.ChatAdapter;
import com.yiyanf.fang.util.CommonUtils;
import com.yiyanf.fang.util.OpenFileUtil;
import com.yiyanf.fang.util.PixelUtil;
import com.yiyanf.fang.util.ToastUtils;
import java.io.File;


/**
 * 文件消息
 */
public class FileMessage extends Message {

    String downloadPath;

    public FileMessage(TIMMessage message){
        this.message = message;
    }

    public FileMessage(String filePath){
        message = new TIMMessage();
        TIMFileElem elem = new TIMFileElem();
        elem.setPath(filePath);
        elem.setFileName(filePath.substring(filePath.lastIndexOf("/")+1));
        message.addElement(elem);
    }



    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        clearView(viewHolder);
        TIMFileElem fileElem = (TIMFileElem) message.getElement(0);
        String fileName = fileElem.getFileName();
        long fileSize = fileElem.getFileSize();

        LinearLayout fileLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fileLayout.setLayoutParams(layoutParams);
        fileLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout fverticalLayout = new LinearLayout(context);
        LinearLayout.LayoutParams fvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fileLayout.setLayoutParams(fvParams);
        fverticalLayout.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(txtParams);
        tv.setMaxWidth(540);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(ContextCompat.getColor(context,isSelf() ? R.color.white : R.color.black));
        tv.setText(fileName);
        fverticalLayout.addView(tv);
        TextView tvSize = new TextView(context);
        tvSize.setPadding(0,15,0,15);
        tvSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tvSize.setTextColor(ContextCompat.getColor(context,R.color.text_gray2));
        tvSize.setText(CommonUtils.formatSize(fileSize));
        fverticalLayout.addView(tvSize);

        fileLayout.addView(fverticalLayout);

        ImageView mImage = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(PixelUtil.dp2px(48), PixelUtil.dp2px(60),1.0f);
        imageParams.setMargins(20,0,0,0);
        mImage.setLayoutParams(imageParams);
        if(fileName.trim().endsWith(".jpg") || fileName.trim().endsWith(".png")){
            // 图片文件
            mImage.setBackgroundResource(R.drawable.ic_file);
        }else if(fileName.trim().endsWith(".mp4")){
            // 视频文件
            mImage.setBackgroundResource(R.drawable.ic_file);
        }/*else if(fileName.trim().endsWith(".txt")){
            // 文本文件

        }else if(fileName.trim().endsWith(".doc") || fileName.trim().endsWith(".docx")){
            // world文件

        }else if(fileName.trim().endsWith(".xls") || fileName.trim().endsWith(".xlsx")){
            // xls文件

        }else if(fileName.trim().endsWith(".ppt") || fileName.trim().endsWith(".pptx")){
            // PPT文件

        }else if(fileName.trim().endsWith(".pdf")){
            // PDF文件

        }*/else{
            // 未知文件

        }
        fileLayout.addView(mImage);

        getBubbleView(viewHolder).addView(fileLayout);
        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 去到文件预览界面
                save(context);
            }
        });
        showStatus(viewHolder);
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return FangApplication.getApplication().getString(R.string.summary_file);
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save(Context context) {
        if (message == null) {
            return ;
        }

        final TIMFileElem e = (TIMFileElem) message.getElement(0);
        String[] str = e.getFileName().split("/");
        String filename = str[str.length-1];
        File storageDirectory = Environment.getExternalStorageDirectory();
        File saveToFile = new File(storageDirectory,"/yiyanf/im");
        if(!saveToFile.exists()){
            saveToFile.mkdirs();
        }
        downloadPath = saveToFile.getAbsolutePath()+"/"+filename;
        File pathFile = new File(downloadPath);
        if(pathFile.exists()){
            // 直接打开，因为已经下载过了
            Intent intent = OpenFileUtil.openFile(downloadPath);
            context.startActivity(intent);
            return;
        }

        e.getToFile(downloadPath, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "getFile failed. code: " + i + " errmsg: " + s);
                ToastUtils.show(FangApplication.getApplication(), R.string.save_fail);
            }

            @Override
            public void onSuccess() {
                ToastUtils.show(FangApplication.getApplication(),FangApplication.getApplication().getString(R.string.save_succ) +
                        "path : " + downloadPath);
                // 下载完成后直接打开文件
                Intent intent = OpenFileUtil.openFile(downloadPath);
                context.startActivity(intent);
            }
        });

        /*e.getFile(new TIMValueCallBack<byte[]>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "getFile failed. code: " + i + " errmsg: " + s);
            }

            @Override
            public void onSuccess(byte[] bytes) {
                String[] str = e.getFileName().split("/");
                String filename = str[str.length-1];
                if (IMFileUtil.isFileExist(filename, Environment.DIRECTORY_DOWNLOADS)) {
                    ToastUtils.show(FangApplication.getApplication(),R.string.save_exist);
                    return;
                }
                java.io.File mFile = IMFileUtil.createFile(bytes, filename, Environment.DIRECTORY_DOWNLOADS);
                if (mFile != null){
                    ToastUtils.show(FangApplication.getApplication(),FangApplication.getApplication().getString(R.string.save_succ) +
                            "path : " + mFile.getAbsolutePath());
                }else{
                    ToastUtils.show(FangApplication.getApplication(), R.string.save_fail);
                }
            }
        });*/

    }
}
