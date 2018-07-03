package com.yiyanf.fang.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tencent.ugc.TXVideoInfoReader;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.PickCoverAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.ToolbarView;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.ui.widget.imgloader.ImageLoader;
import com.yiyanf.fang.util.BitmapUtils;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 编辑封面
 *
 * Created by Hition on 2017/10/26.
 */

public class EditPublishCoverActivity extends BaseActivity implements View.OnClickListener,TXVideoInfoReader.OnSampleProgrocess {


    @Bind(R.id.iv_close_cover)
    ImageView mCloseEditCover;
    @Bind(R.id.btn_cover_done)
    Button mDone;
    @Bind(R.id.iv_cover)
    ImageView ivCover;
    @Bind(R.id.rv_front_cover)
    RecyclerView mCoverRv;


    private static final int THUMB_COUNT = 5;

    private TXVideoInfoReader mTXVideoInfoReader;
    private String mFilePath;
//    private PickCoverAdapter mCoverAdapter;
    private Bitmap coverBitmap;

    private PickCoverAdapter mCoverAdapter;

    public static void startActivityForResult(Activity activity,String videoPath) {
        Intent intent = new Intent(activity, EditPublishCoverActivity.class);
        intent.putExtra("video_path",videoPath);
        activity.startActivityForResult(intent,100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_publish_edit_cover);
        ButterKnife.bind(this);
        mFilePath = getIntent().getStringExtra("video_path");
        initView();
        getCovers();
    }

    private void getCovers() {
        mTXVideoInfoReader = TXVideoInfoReader.getInstance();
        mTXVideoInfoReader.getSampleImages(THUMB_COUNT, mFilePath, this);
    }

    private void initView() {
        ImageLoader.loadFromFile(this,mFilePath,ivCover);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCoverRv.setLayoutManager(layoutManager);
        mCoverAdapter = new PickCoverAdapter(this);
        mCoverRv.setAdapter(mCoverAdapter);

        mCoverAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                coverBitmap = mCoverAdapter.getItem(position);
                ivCover.setImageBitmap(coverBitmap);
            }
        });

        mCloseEditCover.setOnClickListener(this);
        mDone.setOnClickListener(this);
    }

    @Override
    public void sampleProcess(int num, Bitmap bitmap) {
        if (num == 0) {
            mCoverAdapter.clear();
        }

        Bitmap bmp;
        if(bitmap.getWidth() > bitmap.getHeight()){
            // 横屏视频
            if(bitmap.getHeight()>1080){
                bmp =  BitmapUtils.compress(bitmap,0.25f,0.25f);
            }else if(bitmap.getHeight() == 1080){
                bmp =  BitmapUtils.compress(bitmap,0.5f,0.5f);
            }else{
                bmp = bitmap;
            }
        }else{
            // 竖屏视频
            if(bitmap.getWidth()>1080){
                bmp =  BitmapUtils.compress(bitmap,0.25f,0.25f);
            }else if(bitmap.getWidth() == 1080){
                bmp =  BitmapUtils.compress(bitmap,0.5f,0.5f);
            }else{
                bmp = bitmap;
            }
        }

        mCoverAdapter.add(num, bmp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cover_done:
                if(null == coverBitmap){
                    ToastUtils.show(this,"您未选择封面，请先在下方选择一张封面");
                    return;
                }
                String coverFilename = BitmapUtils.bitmap2File(coverBitmap, File.separator + "video_cover",false);
                Intent intent = new Intent();
                intent.putExtra("cover_path",coverFilename);
                setResult(0,intent);
                finish();
                break;

            case R.id.iv_close_cover:
                if(null == coverBitmap){
                    finish();
                }else{
                    DialogManager.showSelectDialog(this, R.string.edit_cover_warning, R.string.ok, R.string.cancel, false, new DialogManager.DialogListener() {
                        @Override
                        public void onNegative() {

                        }

                        @Override
                        public void onPositive() {
                            finish();
                        }
                    });
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTXVideoInfoReader.cancel();
    }

}
