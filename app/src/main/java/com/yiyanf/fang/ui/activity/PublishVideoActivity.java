package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.ugc.TXVideoEditConstants;
import com.tencent.ugc.TXVideoInfoReader;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.api.model.SimpleVideo;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.service.UploadVODService;
import com.yiyanf.fang.ui.widget.CountEditText;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.BitmapUtils;
import com.yiyanf.fang.util.CommonUtils;
import com.yiyanf.fang.util.LocationHelper;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.NetWorkUtil;
import com.yiyanf.fang.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发布视频
 * <p>
 * Created by Hition on 2017/10/26.
 */

public class PublishVideoActivity extends TCBaseActivity implements View.OnClickListener, LocationHelper.OnLocationListener{

    @Bind(R.id.iv_close_publish)
    ImageView mClosePublish;
    @Bind(R.id.iv_publish_cover)
    ImageView mCoverView;
    @Bind(R.id.tv_edit_cover)
    TextView mEditCover;
    @Bind(R.id.et_video_title)
    EditText mTitleEdit;
    @Bind(R.id.et_description)
    CountEditText mVideoDesc;
    @Bind(R.id.btn_video_publish)
    Button mPublishBtn;

    private String mVideoPath = null;
    private String coverpath;
    private Bitmap coverImg;
    private int coverWidth;
    private int coverHeight;
    private long videoSize;

    /*private int areaId;
    private int buildingId;
    private String areaName;
    private String lastAreaName="";
    private String buildingName;
    private int categoryId;*/

    private LoginModel userinfo;
    private double mLocalLng;
    private double mLocalLat;

    private String currentLocal;

    public static void startActivity(Context context, String videopath, String cover) {
        Intent intent = new Intent(context, PublishVideoActivity.class);
        intent.putExtra("path", videopath);
        intent.putExtra("cover", cover);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_publish_video);
        ButterKnife.bind(this);
        parseIntent();
        userinfo = UserInfoCenter.getInstance().getLoginModel();

        initView();
        // 根据定位获取当前的位置
        LocationHelper.getMyLocation(this, this);
    }

    private void parseIntent() {
        mVideoPath = getIntent().getStringExtra("path");
        coverpath = getIntent().getStringExtra("cover");
    }

    private void initView() {
        TXVideoInfoReader mTXVideoInfoReader = TXVideoInfoReader.getInstance();
        if (TextUtils.isEmpty(coverpath)) {
            TXVideoEditConstants.TXVideoInfo videoInfo = mTXVideoInfoReader.getVideoFileInfo(mVideoPath);
            if (videoInfo == null) {
                ToastUtils.show(PublishVideoActivity.this, "暂不支持Android 4.3以下的系统");
                ImageLoader.loadFromFile(this, mVideoPath, mCoverView);
                return;
            }
            coverImg = videoInfo.coverImage;
            if (null != coverImg) {
                mCoverView.setImageBitmap(coverImg);
            }
            coverWidth = videoInfo.width;
            coverHeight = videoInfo.height;
            videoSize = videoInfo.fileSize;
        } else {
            ImageLoader.loadFromFile(this, coverpath, mCoverView);
        }

        mClosePublish.setOnClickListener(this);
        mEditCover.setOnClickListener(this);
        mPublishBtn.setOnClickListener(this);

        mTitleEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                wordLimit(mTitleEdit,48);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*mVideoDesc.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                wordLimit(mVideoDesc,100);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    void wordLimit(EditText editText, int maxNum) {
        int mTextMaxlenght = 0;
        Editable editable = editText.getText();
        String str = editable.toString().trim();
        //得到最初字段的长度大小，用于光标位置的判断
        int selEndIndex = Selection.getSelectionEnd(editable);
        // 取出每个字符进行判断，如果是字母数字和标点符号则为一个字符加1，
        //如果是汉字则为两个字符
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            //32-122包含了空格，大小写字母，数字和一些常用的符号，
            //如果在这个范围内则算一个字符，
            //如果不在这个范围比如是汉字的话就是两个字符
            if (charAt >= 32 && charAt <= 122) {
                mTextMaxlenght++;
            } else {
                mTextMaxlenght += 2;
            }
            // 当最大字符大于40时，进行字段的截取，并进行提示字段的大小
            if (mTextMaxlenght > maxNum) {
                // 截取最大的字段
                String newStr = str.substring(0, i);
                editText.setText(newStr);
                // 得到新字段的长度值
                editable = editText.getText();
                int newLen = editable.length();
                if (selEndIndex > newLen) {
                    selEndIndex = editable.length();
                }
                // 设置新光标所在的位置
                Selection.setSelection(editable, selEndIndex);
                //  Toast.makeText(PublishVideoActivity.this,"最大长度为40个字符或20个汉字！",Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 发布视频
     */
    private void doPublishVideo() {
        if (null != coverImg) {
            coverpath = BitmapUtils.bitmap2File(coverImg, File.separator + "yiyanf/video_cover", true);
        }

        /**
         * 发布视频分流程
         *
         * 1、调用业务服务器PublishVOD，返回VODSign
         * 2、利用VODSign向腾讯云上传视频文件
         * 3、接收到腾讯云publishComple()方法回调后调用FinishPublishVOD接口，通知业务服务器
         */

        String vodDesc = mVideoDesc.getInputText();
        String title = mTitleEdit.getText().toString();
        if (TextUtils.isEmpty(title)) {
            ToastUtils.show(this, "请填写标题");
            return;
        }

        Intent uploadService = new Intent(PublishVideoActivity.this, UploadVODService.class);
        uploadService.putExtra("upload_again", false);
        uploadService.putExtra("video_path", mVideoPath);
        uploadService.putExtra("title", title);
        uploadService.putExtra("cover_path", coverpath);
        uploadService.putExtra("longitude", mLocalLng);
        uploadService.putExtra("latitude", mLocalLat);
        uploadService.putExtra("videodesc", vodDesc);
        startService(uploadService);

        FangConstants.isRecordFinish = true;
        SimpleVideo video = new SimpleVideo();
        video.setCoverheight(coverHeight);
        video.setCoverwidth(coverWidth);
        video.setFrontcover(coverpath);
        EventBus.getDefault().post(video);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close_publish:
                showCloseDialog();
                break;

            case R.id.tv_edit_cover:
                EditPublishCoverActivity.startActivityForResult(this, mVideoPath);
                break;

            case R.id.btn_video_publish:
                if (NetWorkUtil.NETWORKTYPE_WIFI != NetWorkUtil.getNetWorkType(PublishVideoActivity.this)) {
                    String msg = getString(R.string.publish_no_wifi_warning);
                    DialogManager.showSelectDialog(PublishVideoActivity.this,
                            String.format(msg, CommonUtils.formatSize(videoSize)), R.string.ok, R.string.cancel,
                            false, new OnPublishNoWifiListener());
                    return;
                }
                doPublishVideo();
                break;
        }
    }

    private void showCloseDialog() {
        DialogManager.showSelectDialog(this, R.string.quit_publish_warning, R.string.ok, R.string.cancel, false, new DialogManager.DialogListener() {
            @Override
            public void onNegative() {

            }

            @Override
            public void onPositive() {
                finish();
                FangConstants.isRecordFinish = true;
            }
        });
    }

    @Override
    public void onLocationChanged(int code, double lat1, double long1, String location) {
        if (0 == code) {
            String[] localArry = location.split(";");
            String province = "";
            if(localArry.length >=2 ){
                province = localArry[0];
                currentLocal = localArry[1];
            }
            // 经度信息 long1    纬度信息 lat1
            mLocalLng = long1;
            mLocalLat = lat1;

            /*if(!TextUtils.isEmpty(province) && !TextUtils.isEmpty(currentLocal)){
                mAttachArea.setText(currentLocal);
                mDeleteRegion.setVisibility(View.VISIBLE);
                mAttachArea.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                buildingPresenter.getCityIdByName(province,currentLocal);
            }*/
        } else {
            ToastUtils.show(this, "定位失败");
        }
    }

    /**
     * 发布视频没有wifi弹出框提示
     */
    private class OnPublishNoWifiListener implements DialogManager.DialogListener {

        @Override
        public void onNegative() {

        }

        @Override
        public void onPositive() {
            doPublishVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case FangConstants.LOCATION_PERMISSION_REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!LocationHelper.getMyLocation(this, this)) {
                        LogUtil.v("hition==",getString(R.string.locate_fail));
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 编辑封面返回结果
            case 100:
                if (null != data) {
                    coverpath = data.getStringExtra("cover_path");
                    ImageLoader.load(this, coverpath, mCoverView);
                }
                break;
        }
    }
}
