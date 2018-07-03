package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.yiyanf.fang.R;
import com.yiyanf.fang.ui.adapter.DynamicPhotoGridAdapter;
import com.yiyanf.fang.ui.adapter.recycleadapter.XMBaseAdapter;
import com.yiyanf.fang.ui.widget.ToolbarView;
import com.yiyanf.fang.ui.widget.dialog.DialogManager;
import com.yiyanf.fang.util.LogUtil;
import com.yiyanf.fang.util.ToastUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发布动态
 * <p>
 * Created by Hition on 2017/10/27.
 */

public class PublishDynamicActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.titlebar)
    ToolbarView titlebar;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.rv_grid_photos)
    RecyclerView rvGridPhotos;
    @Bind(R.id.tv_location)
    TextView tvLocation;



    private DynamicPhotoGridAdapter mPhotoAdapter;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PublishDynamicActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_publish);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        titlebar.setRightBtnVisible(View.VISIBLE);
        titlebar.setOnLeftClickListener(new ToolbarView.OnLeftClickListener() {
            @Override
            public void onLeftImgClick() {
                // 点击返回键弹出提示框
                DialogManager.showSelectDialog(PublishDynamicActivity.this,R.string.exit_publish_dynamic_warning,R.string.exit,R.string.cancel, true,new DialogManager.DialogListener() {
                    @Override
                    public void onNegative() {

                    }

                    @Override
                    public void onPositive() {
                        finish();
                    }
                });

            }

            @Override
            public void onLeftTextClick() {

            }
        });
        titlebar.setOnRightClickListener(new ToolbarView.OnRightClickListener() {
            @Override
            public void onRightImgClick() {

            }

            @Override
            public void onRightBtnClick() {
                LogUtil.e("hition==", "发布动态正在上传服务……");


            }
        });

        GridLayoutManager mGridManager=new GridLayoutManager(this,3);
        rvGridPhotos.setLayoutManager(mGridManager);
        mPhotoAdapter = new DynamicPhotoGridAdapter(this);
        FootView footerView = new FootView();
        mPhotoAdapter.addFooter(footerView);
        rvGridPhotos.setAdapter(mPhotoAdapter);

        mPhotoAdapter.setOnItemClickListener(new XMBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {



            }
        });
        tvLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                // 去选择区域和楼盘

                break;

        }

    }

    private class FootView implements XMBaseAdapter.ItemView{

        @Override
        public View onCreateView(ViewGroup parent) {
            View footer = LayoutInflater.from(PublishDynamicActivity.this).inflate(R.layout.layout_footer_dynamic_grid, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            footer.setLayoutParams(params);
            ButterKnife.bind(footer);
            return footer;
        }

        @Override
        public void onBindView(View footerView) {
            ImageView mAddPhoto = ButterKnife.findById(footerView,R.id.iv_add_photo);
            mAddPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogManager.showPhotoOptionDialog(PublishDynamicActivity.this);
                }
            });
        }
    }



}
