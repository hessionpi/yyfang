package com.yiyanf.fang.ui.activity;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.rtmp.TXLog;
import com.tencent.ugc.TXVideoEditConstants;
import com.tencent.ugc.TXVideoEditer;
import com.tencent.ugc.TXVideoInfoReader;
import com.yiyanf.fang.FangConstants;
import com.yiyanf.fang.R;
import com.yiyanf.fang.entity.SimpleVideo;
import com.yiyanf.fang.model.UserInfoCenter;
import com.yiyanf.fang.ui.widget.videoeditor.TCVideoEditView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.yiyanf.fang.FangConstants.DEFAULT_MEDIA_PACK_FOLDER;


/**
 * UGC短视频裁剪
 */
public class TCVideoEditerActivity extends BaseActivity implements View.OnClickListener, TXVideoEditer.TXVideoGenerateListener, TXVideoInfoReader.OnSampleProgrocess, TXVideoEditer.TXVideoPreviewListener {


    @Bind(R.id.video_view)
    FrameLayout videoView;

    @Bind(R.id.layout_player)
    FrameLayout layoutPlayer;

    @Bind(R.id.editView)
    TCVideoEditView editView;
    @Bind(R.id.cb_speed)
    CheckBox cbSpeed;
    @Bind(R.id.cut_ll)
    LinearLayout cutLl;

    @Bind(R.id.tv_back)
    ImageView tvBack;
    @Bind(R.id.next_step)
    TextView nextStep;
    @Bind(R.id.tv_upload_progress)
    TextView tvUploadProgress;
    @Bind(R.id.fl_loading)
    FrameLayout flLoading;


    private BackGroundHandler mHandler;
    private HandlerThread mBGMHandlerThread;
    private TXVideoInfoReader mTXVideoInfoReader;
    private SimpleVideo mTCVideoFileInfo;
    private TXVideoEditConstants.TXVideoInfo mTXVideoInfo;
    private PhoneStateListener mPhoneListener = null;

    private static final int MSG_LOAD_VIDEO_INFO = 1000;
    private static final int MSG_RET_VIDEO_INFO = 1001;

    public static final int STATE_CUT = 1;
    public static final int DO_PLAY_VIDEO = 2;
    public static final int DO_PAUSE_VIDEO = 3;

    public static final int DO_SEEK_VIDEO = 4;
    public static final int DO_CUT_VIDEO = 5;
    public static final int DO_CANCEL_VIDEO = 6;

    public static final int STATE_NONE = 7;
    public static final int STATE_PLAY = 8;
    public static final int STATE_PAUSE = 9;

    int minDur;

    @Override
    public void sampleProcess(int i, Bitmap bitmap) {
        int num = i;
        Bitmap bmp = bitmap;
        editView.addBitmap(num, bmp);
        TXLog.d("zsj", "number = " + i + ",bmp = " + bitmap);
    }

    @Override
    public void onPreviewProgress(int i) {

    }

    @Override
    public void onPreviewFinished() {

    }

    @Override
    public void onGenerateProgress(float v) {
        final int prog = (int) (v * 100);
        // mWorkProgressDialog.setProgress(prog);
        tvUploadProgress.setText(prog + "%");
    }

    private void updateMediaStore() {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(mVideoOutputPath)));
        sendBroadcast(scanIntent);
    }

    private TXVideoEditConstants.TXGenerateResult mResult;
    private boolean mPublish = false;


    private void createThumbFile() {
        AsyncTask<Void, String, String> task = new AsyncTask<Void, String, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                File outputVideo = new File(mVideoOutputPath);
                if (outputVideo == null || !outputVideo.exists())
                    return null;
                Bitmap bitmap = mTXVideoInfoReader.getSampleImage(0, mVideoOutputPath);
                if (bitmap == null)
                    return null;
                String mediaFileName = outputVideo.getAbsolutePath();
                if (mediaFileName.lastIndexOf(".") != -1) {
                    mediaFileName = mediaFileName.substring(0, mediaFileName.lastIndexOf("."));
                }
                String folder = mediaFileName;
                File appDir = new File(folder);
                if (!appDir.exists()) {
                    appDir.mkdirs();
                }

                String fileName = "thumbnail" + ".jpg";
                File file = new File(appDir, fileName);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mTCVideoFileInfo.setPath(file.getAbsolutePath());
                return null;
            }

            @Override
            protected void onPostExecute(String s) {

            }
        };
        task.execute();
    }

    @Override
    public void onGenerateComplete(TXVideoEditConstants.TXGenerateResult txGenerateResult) {
      /*  if (mWorkProgressDialog != null && mWorkProgressDialog.isAdded()) {
            mWorkProgressDialog.dismiss();
        }*/

        if (txGenerateResult.retCode == TXVideoEditConstants.GENERATE_RESULT_OK) {
            updateMediaStore();
            if (mTXVideoInfo != null) {
                mResult = txGenerateResult;
            }
            if (mPublish) {
                createThumbFile();
                mPublish = false;
            } else {
                //dismissLoading();
                flLoading.setVisibility(View.GONE);
                PublishVideoActivity.startActivity(this, mVideoOutputPath, "");
                finish();

            }
        } else {
            TXVideoEditConstants.TXGenerateResult ret = txGenerateResult;
            Toast.makeText(TCVideoEditerActivity.this, ret.descMsg, Toast.LENGTH_SHORT).show();
        }

        mCurrentState = STATE_NONE;
    }


    class BackGroundHandler extends Handler {

        public BackGroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_VIDEO_INFO:
                    TXVideoEditConstants.TXVideoInfo videoInfo = mTXVideoInfoReader.getVideoFileInfo(mTCVideoFileInfo.getPath());
                    if (videoInfo == null) {
                        showUnSupportDialog("暂不支持Android 4.3以下的系统");
                        return;
                    }
                    Message mainMsg = new Message();
                    mainMsg.what = MSG_RET_VIDEO_INFO;
                    mainMsg.obj = videoInfo;
                    mMainHandler.sendMessage(mainMsg);
                    break;
            }

        }
    }

    int maxDur;
    private TXVideoEditer mTXVideoEditer;
    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RET_VIDEO_INFO:
                    mTXVideoInfo = (TXVideoEditConstants.TXVideoInfo) msg.obj;

                    TXVideoEditConstants.TXPreviewParam param = new TXVideoEditConstants.TXPreviewParam();
                    param.videoView = videoView;
                    param.renderMode = TXVideoEditConstants.PREVIEW_RENDER_MODE_FILL_EDGE;
                    int ret = mTXVideoEditer.setVideoPath(mTCVideoFileInfo.getPath());
                    mTXVideoEditer.initWithPreview(param);
                    if (ret < 0) {
                        showUnSupportDialog("本机型暂不支持此视频格式");
                        finish();
                        return;
                    }

                    if ((int) mTXVideoInfo.duration <= maxDur) {
                        handleOp(DO_SEEK_VIDEO, 0, (int) mTXVideoInfo.duration);
                    } else {
                        handleOp(DO_SEEK_VIDEO, 0, maxDur);
                    }
                    editView.setMediaFileInfo(mTXVideoInfo, minDur, maxDur);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_video_editer);
        ButterKnife.bind(this);


        initViews();
        initData();
    }


    private void initViews() {
        showLoadingView();
        maxDur = Long.valueOf(UserInfoCenter.getInstance().getLoginModel().getVideomaxduration()).intValue();
        minDur = Long.valueOf(UserInfoCenter.getInstance().getLoginModel().getVideominduration()).intValue();
        tvBack.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        editView.setCutChangeListener(new TCVideoEditView.OnCutChangeListener() {

            @Override
            public void onCutChangeKeyDown() {
            }

            @Override
            public void onCutChangeKeyUp(int startTime, int endTime) {

                handleOp(DO_SEEK_VIDEO, editView.getSegmentFrom(), editView.getSegmentTo());
            }
        });

        //initWorkProgressPopWin();
    }

    private void initData() {
        mTCVideoFileInfo = (SimpleVideo) getIntent().getSerializableExtra(FangConstants.INTENT_KEY_SINGLE_CHOOSE);
        mTXVideoInfoReader = TXVideoInfoReader.getInstance();
        //初始化后台Thread线程
        mBGMHandlerThread = new HandlerThread("LoadData");
        mBGMHandlerThread.start();
        mHandler = new BackGroundHandler(mBGMHandlerThread.getLooper());


        //初始化SDK编辑
        mTXVideoEditer = new TXVideoEditer(this);
        mTXVideoEditer.setTXVideoPreviewListener(this);

        //加载视频基本信息
        mHandler.sendEmptyMessage(MSG_LOAD_VIDEO_INFO);

        //设置电话监听
        mPhoneListener = new TXPhoneStateListener(this);
        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        //加载缩略图
        if (maxDur != 0 && mTCVideoFileInfo.getDuring() <= maxDur) {
            mTXVideoInfoReader.getSampleImages(10, mTCVideoFileInfo.getPath(), this);
        } else {
            mTXVideoInfoReader.getSampleImages((int) mTCVideoFileInfo.getDuring() / (maxDur / 10), mTCVideoFileInfo.getPath(), this);
        }
        //导入水印
        //  mWaterMarkLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

    }

    private int mCurrentState = STATE_NONE;

    /**
     * 错误框方法
     */
    private void showUnSupportDialog(String text) {
        final Dialog dialog = new Dialog(TCVideoEditerActivity.this, R.style.ConfirmDialogStyle);
        View v = LayoutInflater.from(TCVideoEditerActivity.this).inflate(R.layout.dialog_ugc_tip, null);
        dialog.setContentView(v);
        TextView title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView msg = (TextView) dialog.findViewById(R.id.tv_msg);
        Button ok = (Button) dialog.findViewById(R.id.btn_ok);
        title.setText("视频编辑失败");
        msg.setText(text);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private synchronized boolean handleOp(int state, int startPlayTime, int endPlayTime) {
        switch (state) {
            case DO_PLAY_VIDEO:
                if (mCurrentState == STATE_NONE) {

                    mTXVideoEditer.startPlayFromTime(startPlayTime, endPlayTime);
                    mCurrentState = STATE_PLAY;
                    return true;
                } else if (mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.resumePlay();
                    mCurrentState = STATE_PLAY;
                    return true;
                }
                break;
            case DO_PAUSE_VIDEO:
                if (mCurrentState == STATE_PLAY) {
                    mTXVideoEditer.pausePlay();
                    mCurrentState = STATE_PAUSE;
                    return true;
                }
                break;
            case DO_SEEK_VIDEO:
                if (mCurrentState == STATE_CUT) {
                    return false;
                }
                if (mCurrentState == STATE_PLAY || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                }
                dismissLoading();
                mTXVideoEditer.startPlayFromTime(startPlayTime, endPlayTime);
                mCurrentState = STATE_PLAY;
                return true;
            case DO_CUT_VIDEO:
                if (mCurrentState == STATE_PLAY || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                }
                startTranscode();
                mCurrentState = STATE_CUT;

                return true;
            case DO_CANCEL_VIDEO:
                if (mCurrentState == STATE_PLAY || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                } else if (mCurrentState == STATE_CUT) {
                    mTXVideoEditer.cancel();
                }
                mCurrentState = STATE_NONE;
                return true;
        }
        return false;
    }


    // private VideoWorkProgressFragment mWorkProgressDialog;

    private void initWorkProgressPopWin() {
      /*  if (mWorkProgressDialog == null) {
            mWorkProgressDialog = new VideoWorkProgressFragment();
            mWorkProgressDialog.setOnClickStopListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mWorkProgressDialog.dismiss();
                    Toast.makeText(TCVideoEditerActivity.this, "取消视频生成", Toast.LENGTH_SHORT).show();
                    mWorkProgressDialog.setProgress(0);
                    mCurrentState = STATE_NONE;
                    if (mTXVideoEditer != null) {
                        mTXVideoEditer.cancel();
                    }
                }
            });
        }
        mWorkProgressDialog.setProgress(0);*/
    }

    private String mVideoOutputPath;

    private void startTranscode() {
        flLoading.setVisibility(View.VISIBLE);
        tvUploadProgress.setText(0 + "%");
      /*  mWorkProgressDialog.setProgress(0);
        mWorkProgressDialog.setCancelable(false);
        mWorkProgressDialog.show(getFragmentManager(), "progress_dialog");*/
        try {
            mTXVideoEditer.setCutFromTime(editView.getSegmentFrom(), editView.getSegmentTo());

            String outputPath = Environment.getExternalStorageDirectory() + File.separator + DEFAULT_MEDIA_PACK_FOLDER;
            File outputFolder = new File(outputPath);

            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }
            String current = String.valueOf(System.currentTimeMillis() / 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String time = sdf.format(new Date(Long.valueOf(current + "000")));
            String saveFileName = String.format("TXVideo_%s.mp4", time);
            mVideoOutputPath = outputFolder + "/" + saveFileName;
            mTXVideoEditer.setVideoGenerateListener(this);
            mTXVideoEditer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_540P, mVideoOutputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TCVideoEditerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                mTXVideoInfoReader.cancel();
                mTXVideoEditer.stopPlay();
                finish();
                break;
            case R.id.next_step:
                nextStep.setClickable(false);
                tvBack.setClickable(false);
                editView.setChildrenEnabled(false);
                // showLoadingView();
                mTXVideoInfoReader.cancel();
                mTXVideoEditer.stopPlay();
                handleOp(DO_CUT_VIDEO, 0, 0);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTXVideoInfoReader.cancel();
        mTXVideoEditer.stopPlay();
    }


    /*********************************************监听电话状态**************************************************/
    static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TCVideoEditerActivity> mJoiner;

        public TXPhoneStateListener(TCVideoEditerActivity joiner) {
            mJoiner = new WeakReference<TCVideoEditerActivity>(joiner);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TCVideoEditerActivity joiner = mJoiner.get();
            if (joiner == null) return;
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:  //电话等待接听
                case TelephonyManager.CALL_STATE_OFFHOOK:  //电话接听
                    if (joiner.mCurrentState == STATE_CUT) {
                        joiner.handleOp(DO_CANCEL_VIDEO, 0, 0);
                      /*  if (joiner.mWorkProgressDialog != null && joiner.mWorkProgressDialog.isAdded()) {
                            joiner.mWorkProgressDialog.dismiss();
                        }*/
                        joiner.flLoading.setVisibility(View.GONE);
                    } else {
                        joiner.handleOp(DO_PAUSE_VIDEO, 0, 0);

                    }
                    if (joiner.nextStep != null) {
                        joiner.nextStep.setClickable(true);
                        joiner.nextStep.setEnabled(true);
                    }
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (joiner.mTXVideoEditer != null && joiner.editView != null)
                        joiner.handleOp(DO_PLAY_VIDEO, joiner.editView.getSegmentFrom(), joiner.editView.getSegmentTo());
                    break;
            }
        }
    }

}
