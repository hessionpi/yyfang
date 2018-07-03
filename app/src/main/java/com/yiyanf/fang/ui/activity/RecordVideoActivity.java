package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.ugc.TXRecordCommon;
import com.tencent.ugc.TXUGCRecord;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.LoginModel;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.ui.widget.progress.RecordProgressView;
import com.yiyanf.fang.util.DateUtil;
import com.yiyanf.fang.util.ToastUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 录制视频或导入视频
 *
 * Created by Hition on 2018/03/06.
 */

public class RecordVideoActivity extends BaseActivity implements View.OnClickListener, TXRecordCommon.ITXVideoRecordListener {

    @Bind(R.id.iv_close_record)
    ImageView mCloseRecord;
    @Bind(R.id.video_view)
    TXCloudVideoView mVideoView;
    @Bind(R.id.pb_record)
    RecordProgressView mRecordProgress;
    @Bind(R.id.fl_record_bar)
    FrameLayout mRecordBarLayout;
    @Bind(R.id.iv_flash)
    ImageView mFlashView;
    @Bind(R.id.iv_switch_camera)
    ImageView ivSwitchCamera;
    @Bind(R.id.ll_record_timer)
    LinearLayout mTimerLayout;
    @Bind(R.id.tv_timer)
    TextView tvTimer;
    @Bind(R.id.iv_record)
    ImageView liveRecord;
    @Bind(R.id.iv_record_done)
    ImageView mRecordDone;
    @Bind(R.id.iv_delete_part)
    ImageView mDeletePart;
    @Bind(R.id.rg_video_source)
    RadioGroup mRgVideo;
    @Bind(R.id.rb_record)
    RadioButton mRbRecord;
    /*@Bind(R.id.rb_import)
    RadioButton mRbImport;*/

    //录制相关
    private boolean mRecording = false;
    private TXUGCRecord mTXCameraRecord = null;
    private long mStartRecordTimeStamp = 0;
    private long recordDuration = 0;

    private int minDura;
    private int maxDura;
    private LoginModel userModel;

    private boolean mFront = false;
    private boolean mFlashOn = false;
//    TXRecordCommon.TXRecordResult mTXRecordResult = null;


    AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusListener;
    private boolean mStartPreview = false;
    private boolean mPause = false;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RecordVideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_record_video);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        userModel = UserInfoCenter.getInstance().getLoginModel();
        minDura = Long.valueOf(userModel.getVideominduration()).intValue();
        maxDura = Long.valueOf(userModel.getVideomaxduration()).intValue();

        FangConstants.isRecordFinish = false;
        mTXCameraRecord = TXUGCRecord.getInstance(this);
        mVideoView.enableHardwareDecode(true);
        mRecordProgress.setMaxDuration(maxDura);
        mRecordProgress.setMinDuration(minDura);

        mCloseRecord.setOnClickListener(this);
        mFlashView.setOnClickListener(this);
        ivSwitchCamera.setOnClickListener(this);
        liveRecord.setOnClickListener(this);
        mDeletePart.setOnClickListener(this);
        mRecordDone.setOnClickListener(this);
        mRgVideo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_import:
                        PickVideoActivity.startActivity(RecordVideoActivity.this);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mRecording ){
            List<String> partsPathList = mTXCameraRecord.getPartsManager().getPartsPathList();
            if(partsPathList.isEmpty()){
                mDeletePart.setVisibility(View.INVISIBLE);
                mRecordDone.setVisibility(View.INVISIBLE);
                mRgVideo.setVisibility(View.VISIBLE);
                mRbRecord.setChecked(true);
            }
        }

        if(FangConstants.isRecordFinish){
            back();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startCameraPreview();
    }

    private void startCameraPreview() {
        if (mStartPreview) {
            return;
        }
        mStartPreview = true;
        mTXCameraRecord.setVideoRecordListener(this);

        TXRecordCommon.TXUGCSimpleConfig simpleConfig = new TXRecordCommon.TXUGCSimpleConfig();
        simpleConfig.videoQuality = TXRecordCommon.VIDEO_QUALITY_HIGH;
        simpleConfig.isFront = mFront;
        simpleConfig.minDuration = minDura;    //视频录制的最小时长ms
        simpleConfig.maxDuration = maxDura;    //视频录制的最大时长ms
        /*if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            simpleConfig.mHomeOriention = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        } else {
            simpleConfig.mHomeOriention = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
        }*/
        mTXCameraRecord.startCameraSimplePreview(simpleConfig, mVideoView);

        // 设置美颜 和 美白 效果级别
        //美颜级别取值范围 0 ~ 9； 0 表示关闭 1 ~ 9值越大效果越明显。当前没眼等级8
        //美白级别取值范围 0 ~ 9； 0 表示关闭 1 ~ 9值越大效果越明显。当前美白等级6
        mTXCameraRecord.setBeautyDepth(TXLiveConstants.BEAUTY_STYLE_SMOOTH, 5, 6, 0);
        // 设置水印
        /*TXVideoEditConstants.TXRect mRect = new TXVideoEditConstants.TXRect();
        mRect.x = 0.78f;
        mRect.y = 0.01f;
        mRect.width = 0.2f;
        mTXCameraRecord.setWatermark(BitmapFactory.decodeResource(getResources(),R.drawable.ic_watermark),mRect);*/
    }

    private String getCustomVideoOutputPath() {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.CHINA);
        String time = sdf.format(new Date(currentTime));
        String outputDir = Environment.getExternalStorageDirectory() + File.separator + FangConstants.DIR_PATH ;
        File outputFolder = new File(outputDir);
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        String tempOutputPath = outputDir + File.separator + "UGC_" + time + ".mp4";
        return tempOutputPath;
    }

    private void startRecord() {
        mRecordBarLayout.setVisibility(View.GONE);
        mRecordDone.setVisibility(View.INVISIBLE);
        mDeletePart.setVisibility(View.INVISIBLE);
        mRgVideo.setVisibility(View.GONE);

        String customVideoPath = getCustomVideoOutputPath();
        String customCoverPath = customVideoPath.replace(".mp4", ".jpg");

        int result = mTXCameraRecord.startRecord(customVideoPath, customCoverPath);
        if (0 != result) {
            ToastUtils.show(RecordVideoActivity.this,"录制失败，错误码：" + result);
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord.stopRecord();
            return;
        }
        mRecording = true;
        liveRecord.setImageResource(R.drawable.ic_record_stop);
        mStartRecordTimeStamp = System.currentTimeMillis();
        requestAudioFocus();
    }


    private void deleteLastPart() {
        if (mRecording && !mPause) {
            return;
        }

        DialogManager.showSelectDialog(this, R.string.delete_part_warning, R.string.ok, R.string.cancel, true, new DialogManager.DialogListener() {
            @Override
            public void onNegative() {

            }

            @Override
            public void onPositive() {
                mRecordProgress.deleteLast();
                mTXCameraRecord.getPartsManager().deleteLastPart();
                int timeSecond = mTXCameraRecord.getPartsManager().getDuration();
                tvTimer.setText(DateUtil.formatSecond(timeSecond));
                if (timeSecond < minDura) {
                    mRecordDone.setVisibility(View.INVISIBLE);
                } else {
                    mRecordDone.setVisibility(View.VISIBLE);
                }

                List<String> partsPathList = mTXCameraRecord.getPartsManager().getPartsPathList();
                if (partsPathList.isEmpty()) {
                    mDeletePart.setVisibility(View.INVISIBLE);
                    mRecordDone.setVisibility(View.INVISIBLE);
                    mRgVideo.setVisibility(View.VISIBLE);
                    recordDuration = 0;
                }
            }
        });

    }

    private void switchRecord() {
        if (mRecording) {
            if (mPause){
                mRgVideo.setVisibility(View.GONE);
                resumeRecord();
            } else {
                pauseRecord();
            }
        } else {
            startRecord();
        }
    }

    private void resumeRecord() {
//        mRecordDone.setVisibility(View.GONE);
        mRecordBarLayout.setVisibility(View.GONE);
        mDeletePart.setVisibility(View.INVISIBLE);

        liveRecord.setImageResource(R.drawable.ic_record_stop);
        mPause = false;
        if (mTXCameraRecord != null) {
            mTXCameraRecord.resumeRecord();
        }
        mStartRecordTimeStamp = System.currentTimeMillis();
        requestAudioFocus();
    }

    private void pauseRecord() {
        if(recordDuration == 0){
            return ;
        }
        liveRecord.setImageResource(R.drawable.ic_record_start);
        mRecordBarLayout.setVisibility(View.VISIBLE);
        mDeletePart.setVisibility(View.VISIBLE);
        mPause = true;
        if (mTXCameraRecord != null) {
            mTXCameraRecord.pauseRecord();
        }
        abandonAudioFocus();
    }

    private void stopRecord() {
        // 录制时间要大于9s
        /*if (System.currentTimeMillis() <= mStartRecordTimeStamp + FangConstants.RECORD_MIN_DURATION) {
            if (showToast) {
                ToastUtils.show(this,"视频拍摄时长不能少于9秒");
                return;
            } else {
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setVideoRecordListener(null);
                }
            }
        }*/

        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopRecord();
        }

        liveRecord.setImageResource(R.drawable.ic_record_start);
        mRecording = false;
        mPause = false;
        abandonAudioFocus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_close_record:
                if(recordDuration > 1){
                    DialogManager.showSelectDialog(this, R.string.record_warning, R.string.ok, R.string.cancel, false, new DialogManager.DialogListener() {
                        @Override
                        public void onNegative() {

                        }

                        @Override
                        public void onPositive() {
                            back();
                        }
                    });
                }else{
                    finish();
                }
                break;

            case R.id.iv_flash:
                mFlashOn = !mFlashOn;
                if(!mFront){
                    mTXCameraRecord.toggleTorch(mFlashOn);
                }
                if(mFlashOn){
                    mFlashView.setImageResource(R.drawable.ic_flash_enable);
                }else{
                    mFlashView.setImageResource(R.drawable.ic_flash_disable);
                }
                break;

            case R.id.iv_switch_camera:
                mFront = !mFront;
                if(mFront){
                    mFlashOn = false;
                    mFlashView.setImageResource(R.drawable.ic_flash_disable);
                    mFlashView.setVisibility(View.GONE);
                }else{
                    mFlashView.setVisibility(View.VISIBLE);
                }
                mTXCameraRecord.switchCamera(mFront);
                break;

            case R.id.iv_delete_part:
                deleteLastPart();
                break;

            case R.id.iv_record:
                switchRecord();
                break;

            case R.id.iv_record_done:
                stopRecord();
                break;

        }
    }


    @Override
    public void onRecordEvent(int event, Bundle bundle) {
        if (event == TXRecordCommon.EVT_ID_PAUSE) {
            mRecordProgress.clipComplete();
        } else if(event == TXRecordCommon.EVT_CAMERA_CANNOT_USE){
            Toast.makeText(this, "摄像头打开失败，请检查权限", Toast.LENGTH_SHORT).show();
        }else if(event == TXRecordCommon.EVT_MIC_CANNOT_USE){
            Toast.makeText(this, "麦克风打开失败，请检查权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRecordProgress(long milliSecond) {
        tvTimer.setVisibility(View.VISIBLE);
        mTimerLayout.setVisibility(View.VISIBLE);
        if(milliSecond >= minDura){
            mRecordDone.setVisibility(View.VISIBLE);
        }

        recordDuration = milliSecond;
        mRecordProgress.setProgress((int) milliSecond);
        tvTimer.setText(DateUtil.formatSecond(milliSecond));
    }

    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult result) {
        mDeletePart.setVisibility(View.VISIBLE);
        if (result.retCode < TXRecordCommon.RECORD_RESULT_OK) {
            liveRecord.setImageResource(R.drawable.ic_record_start);
            mRecording = false;
            int timeSecond = mTXCameraRecord.getPartsManager().getDuration();
            tvTimer.setText(DateUtil.formatSecond(timeSecond));
            ToastUtils.show(this, "录制失败，原因：" + result.descMsg);
        } else if (result != null && (result.retCode == TXRecordCommon.RECORD_RESULT_OK
                || result.retCode == TXRecordCommon.RECORD_RESULT_OK_REACHED_MAXDURATION
                || result.retCode == TXRecordCommon.RECORD_RESULT_OK_LESS_THAN_MINDURATION)) {

            /*if (mTXCameraRecord != null) {
                mTXCameraRecord.getPartsManager().deleteAllParts();
            }*/
            // 录制完成去发布视频
            PublishVideoActivity.startActivity(this, result.videoPath, result.coverPath);
        }
    }

    private void requestAudioFocus() {
        if (null == mAudioManager) {
            mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        }

        if (null == mOnAudioFocusListener) {
            mOnAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

                @Override
                public void onAudioFocusChange(int focusChange) {
                    try {
                        switch (focusChange){
                            case AudioManager.AUDIOFOCUS_LOSS:
                                pauseRecord();
                                break;

                            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                                pauseRecord();
                                break;

                            case AudioManager.AUDIOFOCUS_GAIN:

                                break;

                            default:
                                pauseRecord();
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        try {
            mAudioManager.requestAudioFocus(mOnAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abandonAudioFocus() {
        try {
            if (null != mAudioManager && null != mOnAudioFocusListener) {
                mAudioManager.abandonAudioFocus(mOnAudioFocusListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopCameraPreview();
            mStartPreview = false;
        }
        if (mRecording && !mPause) {
            pauseRecord();
        }
    }

    private void back(){
        List<String> partsPathList = mTXCameraRecord.getPartsManager().getPartsPathList();
        if(!partsPathList.isEmpty()){
            mTXCameraRecord.getPartsManager().deleteAllParts();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecordProgress.release();
        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopCameraPreview();
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord.release();
            mTXCameraRecord = null;
            mStartPreview = false;
        }
        abandonAudioFocus();
    }
}
