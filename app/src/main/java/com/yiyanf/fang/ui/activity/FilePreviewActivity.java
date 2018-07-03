package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yiyanf.fang.R;

import butterknife.ButterKnife;

/**
 * 文件预览页：
 *
 * 用于私信发送文件，打开文件消息下载查看
 *
 * Created by Hition on 2018/2/11.
 */

public class FilePreviewActivity extends BaseActivity {

    public static void startActivity(Context context, int label, int catId, String catName) {
        Intent intent = new Intent(context, FilePreviewActivity.class);
        intent.putExtra("category_label",label);
        intent.putExtra("category_id",catId);
        intent.putExtra("category_name",catName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_preview);
        ButterKnife.bind(this);
        parseIntent();
        initView();
    }

    private void initView() {


    }

    private void parseIntent() {


    }


}
