package com.yiyanf.fang.ui.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.activity.LoginActivity;
import com.yiyanf.fang.util.PixelUtil;

import butterknife.ButterKnife;

/**
 * Created by Hition on 2017/10/17.
 */

public final class DialogManager {


    /**
     *
     * @param context               上下文
     * @param msg_rid               消息内容
     * @param okResId               确定按钮
     * @param cancelResId           取消按钮
     * @param cancelable            点击外部是否可取消
     * @param listener              监听
     */
    public static AlertDialog showSelectDialog(Context context,
                                               int msg_rid, int okResId, int cancelResId, boolean cancelable, final DialogListener listener) {
        return showSelectDialog(context,context.getString(msg_rid),okResId,cancelResId,cancelable,listener);
    }

    public static AlertDialog showSelectDialog(Context context,
                                               String msg, int okResId, int cancelResId, boolean cancelable, final DialogListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_alert_select_notitle, null);

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.show();
        dialog.setContentView(layout);

        Window dialogWindow = dialog.getWindow();//获取window
        dialogWindow.setGravity(Gravity.CENTER);//
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = (int)(PixelUtil.getScreenWidth(context) * 0.9);
        params.alpha = 9f;
        dialogWindow.setAttributes(params);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView ok = (TextView) dialog.findViewById(R.id.ok);

        title.setText(msg);
        cancel.setText(cancelResId);
        ok.setText(okResId);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onNegative();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onPositive();
            }
        });
        return dialog;
    }


    /**
     * 显示被踢下线通知
     * @param context activity
     */
    public static void showKickOutDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.NormalDialog);
        builder.setTitle(context.getResources().getString(R.string.tip_force_offline));
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                /*Intent intent = new Intent(context, TCLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);*/
            }
        });
        builder.setNegativeButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginActivity.startActivity(context);
                /*TCLoginMgr.getInstance().reLogin();
                Intent intent = new Intent(context, TCMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);*/
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     * 显示loading对话框
     */
    public  static Dialog showLoadingDialog(final Context context,String text) {
    final   Dialog  loadingDialog = new Dialog(context, R.style.loading_dialog);
        LayoutInflater inflater2 = LayoutInflater.from(context);
        View v = inflater2.inflate(R.layout.dialog_common_layout, null);// 得到加载view
        TextView tv= ButterKnife.findById(v,R.id.tv_text);
        tv.setText(text);
        loadingDialog.setCancelable(false);//true 点击空白处或返回键消失   false 不消失
        loadingDialog.setContentView(v);// 设置布局
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loadingDialog.dismiss();
                }
                return false;
            }
        });
        loadingDialog.show();
        return loadingDialog;
    }
    /**
     * 显示完成对话框
     */
    public  static Dialog showFinishDialog(final Context context,String text) {
        final   Dialog  loadingDialog = new Dialog(context, R.style.loading_dialog);
        LayoutInflater inflater2 = LayoutInflater.from(context);
        View v = inflater2.inflate(R.layout.dialog_finish_layout, null);// 得到加载view
        TextView tv= ButterKnife.findById(v,R.id.tv_text);
        tv.setText(text);
        loadingDialog.setCancelable(false);//true 点击空白处或返回键消失   false 不消失
        loadingDialog.setContentView(v);// 设置布局
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    loadingDialog.dismiss();
                }
                return false;
            }
        });
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 弹出选择照片对话框
     *
     * @param context
     */
    public static void showPhotoOptionDialog(Context context){
        final Dialog dialog = new Dialog(context);
        /**
         * 全屏，不然布局周围点击无效
         * 必须放在setContentView之前
         */
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_choose_photo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView mCamera = (TextView) dialog.findViewById(R.id.tv_camera);
        TextView mAlbum = (TextView) dialog.findViewById(R.id.tv_album);
        TextView mCancel = (TextView) dialog.findViewById(R.id.tv_cancel);

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打开相机拍照


            }
        });
        mAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打开相册选择照片


                dialog.dismiss();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();//获取window
        dialogWindow.setGravity(Gravity.BOTTOM);//底部
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();//获取对话框参数
        lp.width =  PixelUtil.getScreenWidth(context);
        dialogWindow.setAttributes(lp);

        dialog.show();
    }

    public interface DialogListener extends DialogPositiveListener {
        void onNegative();//消极
    }

    public interface DialogPositiveListener {
        void onPositive(); //积极
    }
    /**
     * 自定义布局的 AlertDialog,带有标题、内容、两个按钮
     *
     * @param context
     * @param titleRid
     * @param msg
     * @param okRid
     * @param cancelRid
     * @param cancelable
     * @param listener
     * @return
     */
    public static AlertDialog showSelectDialogWithTitle(Context context, int titleRid, String msg, int okRid,
                                                        int cancelRid, boolean cancelable, final DialogListener listener) {
        //部分机型 dialog宽度不满出现灰色部分 问题解决
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.layout_alert_select_withtitle, null);

        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.show();

        Window dialogWindow = dialog.getWindow();//获取window
        dialogWindow.setContentView(layout);
        dialogWindow.setGravity(Gravity.CENTER);//

        TextView title = (TextView) dialogWindow.findViewById(R.id.tv_title);
        TextView message = (TextView) dialogWindow.findViewById(R.id.message);
        TextView cancel = (TextView) dialogWindow.findViewById(R.id.cancel);
        TextView ok = (TextView) dialogWindow.findViewById(R.id.ok);

        title.setText(titleRid);
        title.setTextColor(Color.parseColor("#333333"));
        message.setText(msg);
        cancel.setText(cancelRid);
        ok.setText(okRid);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onNegative();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                listener.onPositive();
            }
        });

        return dialog;
    }
}
