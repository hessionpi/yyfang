package com.yiyanf.fang.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.yiyanf.fang.R;
import butterknife.ButterKnife;

/**
 * 好友详细资料
 *
 * Created by Hition on 2017/12/25.
 */

public class ProfileActivity extends BaseActivity {

    private String identify;

    public static void navToProfile(Context context, String identify){
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("identify", identify);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        identify = getIntent().getStringExtra("identify");
        initView();
    }

    private void initView() {

    }


}
